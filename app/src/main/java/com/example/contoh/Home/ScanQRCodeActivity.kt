package com.example.contoh.Home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contoh.R
import com.example.contoh.databinding.ActivityScanQrcodeBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanQRCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrcodeBinding
    private lateinit var cameraExecutor: ExecutorService
    private var lastScanned: String? = null

    private val scanner by lazy {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScanQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Scan Qr Code"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) {
            startCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy: ImageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("ScanQRCodeActivity", "Gagal mulai kamera", e)
                runOnUiThread {
                    Toast.makeText(this, "Gagal memulai kamera", Toast.LENGTH_SHORT).show()
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val rawValue = barcodes[0].rawValue ?: ""
                        if (rawValue.isNotEmpty() && rawValue != lastScanned) {
                            lastScanned = rawValue
                            val formatted = formatScanResult(rawValue)
                            runOnUiThread {
                                binding.resultContainer.visibility = View.VISIBLE
                                if (formatted.contains("\n")) {
                                    binding.tvScanResult.text = "Hasil scan"
                                    binding.tvScanDetails.visibility = View.VISIBLE
                                    binding.tvScanDetails.text = formatted
                                } else {
                                    binding.tvScanResult.text = formatted
                                    binding.tvScanDetails.visibility = View.GONE
                                    binding.tvScanDetails.text = ""
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ScanQRCodeActivity", "Scanner error", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun formatScanResult(raw: String): String {
        return try {
            val parts = raw.split(';').mapNotNull { it.trim().takeIf { it.isNotEmpty() } }
            val kvs = parts.mapNotNull { part ->
                val idx = part.indexOf(':')
                if (idx > 0) {
                    val k = part.substring(0, idx).trim().replaceFirstChar { it.uppercase() }
                    val v = part.substring(idx + 1).trim()
                    "$k: $v"
                } else null
            }
            if (kvs.isNotEmpty()) {
                kvs.joinToString(separator = "\n")
            } else {
                raw
            }
        } catch (t: Throwable) {
            raw
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            scanner.close()
        } catch (t: Throwable) {
            Log.w("ScanQRCodeActivity", "Error closing scanner", t)
        }
        cameraExecutor.shutdown()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
