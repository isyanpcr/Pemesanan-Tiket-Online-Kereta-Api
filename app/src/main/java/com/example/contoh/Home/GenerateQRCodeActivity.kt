package com.example.contoh.Home

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contoh.R
import com.example.contoh.databinding.ActivityGenerateQrcodeBinding
import com.example.contoh.utils.NotificationHelper
import com.example.contoh.utils.PermissionHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar
import java.text.SimpleDateFormat

class GenerateQRCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateQrcodeBinding

    // register notification permission launcher
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notifikasi diizinkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGenerateQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Generate QR Code"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Request notification permission if required
        if (PermissionHelper.isNotificationPermissionRequired()) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (!PermissionHelper.hasPermission(this, permission)) {
                PermissionHelper.requestPermission(notificationPermissionLauncher, permission)
            }
        }

        // Sample data for spinners
        val origins = listOf("Jakarta", "Bandung", "Surabaya", "Yogyakarta")
        val destinations = listOf("Semarang", "Malang", "Bali", "Solo")
        val classes = listOf("Economy", "Business", "Executive")

        val originAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, origins).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val destAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, destinations).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val classAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerOrigin.adapter = originAdapter
        binding.spinnerDestination.adapter = destAdapter
        binding.spinnerClass.adapter = classAdapter

        // Date field: non-editable, open DatePicker on click (either icon or field)
        binding.etDate.isFocusable = false
        binding.etDate.isClickable = true
        val openDatePicker = {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val month1 = month + 1
                binding.etDate.setText(String.format("%04d-%02d-%02d", year, month1, dayOfMonth))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.etDate.setOnClickListener { openDatePicker() }
        binding.ivCalendar.setOnClickListener { openDatePicker() }

        // Submit handling -- replaced placeholder bitmap with ZXing-based QR creation
        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty().trim()
            val origin = if (binding.spinnerOrigin.selectedItemPosition >= 0) binding.spinnerOrigin.selectedItem.toString() else ""
            val destination = if (binding.spinnerDestination.selectedItemPosition >= 0) binding.spinnerDestination.selectedItem.toString() else ""
            val travelDate = binding.etDate.text?.toString().orEmpty().trim()
            val travelClass = if (binding.spinnerClass.selectedItemPosition >= 0) binding.spinnerClass.selectedItem.toString() else ""

            if (name.isEmpty()) {
                binding.etName.error = "Masukkan nama"
                return@setOnClickListener
            } else binding.etName.error = null

            if (origin.isEmpty()) {
                // simple feedback: set prompt on spinner by using requestFocus on a helper view
                binding.textOriginError.visibility = View.VISIBLE
                return@setOnClickListener
            } else binding.textOriginError.visibility = View.GONE

            if (destination.isEmpty()) {
                binding.textDestinationError.visibility = View.VISIBLE
                return@setOnClickListener
            } else binding.textDestinationError.visibility = View.GONE

            if (travelDate.isEmpty()) {
                binding.etDate.error = "Pilih tanggal berangkat"
                return@setOnClickListener
            } else binding.etDate.error = null

            if (travelClass.isEmpty()) {
                binding.textClassError.visibility = View.VISIBLE
                return@setOnClickListener
            } else binding.textClassError.visibility = View.GONE

            // build QR content (single-line, change as desired)
            val qrContent = "Nama:$name;Dari:$origin;Ke:$destination;Tanggal:$travelDate;Kelas:$travelClass"

            // compute pixel size matching the ImageView 250dp
            val density = resources.displayMetrics.density
            val sizePx = (250 * density).toInt().coerceAtLeast(200)

            // create QR using ZXing
            val qrBitmap = createQR(qrContent, sizePx)
            binding.ivQrCode.setImageBitmap(qrBitmap)
            binding.ivQrCode.visibility = View.VISIBLE
            binding.scrollView.post { binding.scrollView.fullScroll(View.FOCUS_DOWN) }

            // save to Supabase (background thread)
            saveToSupabase(name, origin, destination, travelDate, travelClass)
        }
    }

    // new helper using ZXing QRCodeWriter
    private fun createQR(text: String, sizePx: Int): Bitmap {
        val writer = QRCodeWriter()
        val hints = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
        val matrix = writer.encode(text, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
        val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.RGB_565)
        for (x in 0 until sizePx) {
            for (y in 0 until sizePx) {
                bmp.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    // New helper to save the ticket data to Supabase REST API
    private fun saveToSupabase(
        nama: String,
        stasiunAsal: String,
        stasiunTujuan: String,
        tanggalBerangkat: String,
        kelas: String
    ) {
        // Supabase REST endpoint and publishable key (as provided)
        val supabaseUrl = "https://neqbkthtpcyzfdysvonb.supabase.co/rest/v1/data_tiket"
        val apiKey = "sb_publishable_6tgOY1pTag4IiIvum1Ljxg_BDChCwq1"

        Thread {
            var conn: HttpURLConnection? = null
            try {
                val url = URL(supabaseUrl)
                conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("apikey", apiKey)
                    setRequestProperty("Authorization", "Bearer $apiKey")
                    // ask for inserted row back (optional)
                    setRequestProperty("Prefer", "return=representation")
                    connectTimeout = 15000
                    readTimeout = 15000
                }

                val body = JSONObject().apply {
                    put("nama", nama)
                    put("stasiun_asal", stasiunAsal)
                    put("stasiun_tujuan", stasiunTujuan)
                    put("tanggal_berangkat", tanggalBerangkat) // yyyy-MM-dd
                    put("kelas", kelas)
                }

                OutputStreamWriter(conn.outputStream).use { it.write(body.toString()) }

                val code = conn.responseCode
                if (code in 200..299) {
                    // success: show toast and notification on UI thread
                    runOnUiThread {
                        Toast.makeText(this, "Data tersimpan ke server", Toast.LENGTH_SHORT).show()
                        // build a simple intent that opens this activity when user taps notification
                        val intent = Intent(this, GenerateQRCodeActivity::class.java)
                        NotificationHelper.showNotification(
                            this,
                            "Tiket Tersimpan",
                            "Data tiket berhasil disimpan ke server",
                            intent
                        )

                        val triggerAt = System.currentTimeMillis() + 10_000L
                        val req = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
                        scheduleReminder(
                            req,
                            triggerAt,
                            "Pengingat Perjalanan",
                            "Tiket $nama: $stasiunAsal → $stasiunTujuan pada $tanggalBerangkat"
                        )
                    }
                } else {
                    // read error (optional)
                    val errStream = conn.errorStream
                    val errMsg = errStream?.bufferedReader()?.use { it.readText() } ?: "HTTP $code"
                    runOnUiThread {
                        Toast.makeText(this, "Gagal menyimpan: $errMsg", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                conn?.disconnect()
            }
        }.start()
    }

    private fun scheduleReminder(requestCode: Int, triggerAtMillis: Long, title: String, message: String) {
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        else PendingIntent.FLAG_UPDATE_CURRENT
        val pending = PendingIntent.getBroadcast(this, requestCode, intent, flags)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (am.canScheduleExactAlarms()) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
                } else {
                    // fallback ke inexact alarm untuk mencegah SecurityException
                    am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
                }
            } else {
                // pre-S: safe untuk gunakan exact; gunakan setExactAndAllowWhileIdle jika tersedia
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
                }
            }
            Toast.makeText(this, "Reminder dijadwalkan", Toast.LENGTH_SHORT).show()
        } catch (se: SecurityException) {
            am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
            Toast.makeText(this, "Reminder dijadwalkan (inexact)", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal jadwalkan reminder: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}