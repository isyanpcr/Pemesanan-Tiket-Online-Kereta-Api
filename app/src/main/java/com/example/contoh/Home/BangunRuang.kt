package com.example.contoh.Home

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contoh.R
import android.view.Menu


class BangunRuang : AppCompatActivity() {

    companion object {
        const val TAG = "BangunRuang"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bangun_ruang)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "Activity BangunRuang dibuat")

        // Toolbar setup
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Kalkulator Kubus"
            subtitle = "Hitung Volume & Luas Permukaan"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val etSisi = findViewById<EditText>(R.id.etSisi)
        val btnHitung = findViewById<Button>(R.id.btnHitung)
        val tvVolume = findViewById<TextView>(R.id.tvVolume)
        val tvLuasPermukaan = findViewById<TextView>(R.id.tvLuasPermukaan)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvDesc = findViewById<TextView>(R.id.tvDesc)

        val judul = intent.getStringExtra("judul")
        val deskripsi = intent.getStringExtra("deskripsi")

        if (judul != null) {
            tvTitle.text = judul
            Log.d(TAG, "Judul diterima: $judul")
        }

        if (deskripsi != null) {
            tvDesc.text = deskripsi
            Log.d(TAG, "Deskripsi diterima")
        }

        btnHitung.setOnClickListener {
            Log.d(TAG, "Tombol Hitung diklik")

            val sisiStr = etSisi.text.toString()
            Log.d(TAG, "Input sisi: $sisiStr")

            val sisi = sisiStr.toDoubleOrNull()
            if (sisi != null && sisi > 0) {
                val volume = sisi * sisi * sisi
                val luas = 6 * sisi * sisi

                tvVolume.text = "Volume: %.2f cm³".format(volume)
                tvLuasPermukaan.text = "Luas Permukaan: %.2f cm²".format(luas)

                Log.d(TAG, "Perhitungan berhasil | Volume=$volume | Luas=$luas")
            } else {
                tvVolume.text = "Volume: -"
                tvLuasPermukaan.text = "Luas Permukaan: -"
                etSisi.error = "Masukkan sisi yang valid"

                Log.e(TAG, "Input sisi tidak valid")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "Tombol back ditekan")
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.menu_search -> {
                Log.d(TAG, "Menu Search diklik")
                true
            }
            R.id.menu_settings -> {
                Log.d(TAG, "Menu Settings diklik")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bangun_ruang, menu)
        return true
    }

}
