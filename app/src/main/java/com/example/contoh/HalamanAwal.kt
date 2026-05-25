package com.example.contoh

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contoh.Home.APIView1
import com.example.contoh.Home.APIView2
import com.example.contoh.Home.BangunRuang
import com.example.contoh.Home.GenerateQRCodeActivity
import com.example.contoh.Home.Halaman1
import com.example.contoh.Home.Halaman2
import com.example.contoh.Home.Halaman3
import com.example.contoh.Home.PemesananActivity
import com.example.contoh.Home.ScanQRCodeActivity
import com.example.contoh.Home.WebView
import com.example.contoh.WikiONNX.WikiActivity
import com.example.contoh.databinding.ActivityWelcomeBinding
import com.google.android.material.snackbar.Snackbar

class HalamanAwal : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button.setOnClickListener {
            Toast.makeText(this, "Selamat datang", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BangunRuang::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            val intent = Intent(this, Halaman1::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button3.setOnClickListener {
            val intent = Intent(this, Halaman2::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button4.setOnClickListener {
            val intent = Intent(this, Halaman3::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button6.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button7.setOnClickListener {
            val intent = Intent(this, WebView::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button9.setOnClickListener {
            val intent = Intent(this, WikiActivity::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button10.setOnClickListener {
            val intent = Intent(this, APIView1::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button11.setOnClickListener {
            val intent = Intent(this, APIView2::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button12.setOnClickListener {
            val intent = Intent(this, PemesananActivity::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button13.setOnClickListener {
            val intent = Intent(this, GenerateQRCodeActivity::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }

        binding.button14.setOnClickListener {
            val intent = Intent(this, ScanQRCodeActivity::class.java)
            intent.putExtra("judul", binding.tvAppTitle.text.toString())
            intent.putExtra("deskripsi", binding.tvWelcomeDesc.text.toString())
            startActivity(intent)
        }


        binding.button5.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->
                    val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    Snackbar.make(binding.root, "Logout dibatalkan", Snackbar.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .show()
        }

    }
}