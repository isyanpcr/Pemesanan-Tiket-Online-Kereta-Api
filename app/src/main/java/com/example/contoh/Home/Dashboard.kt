package com.example.contoh.Home

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contoh.R
import com.example.contoh.menu_1
import com.example.contoh.menu_2

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Toolbar setup (sama gaya seperti Halaman1) — sekarang menampilkan tombol back
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "SumBar ekspres"
            subtitle = "Dashboard Utama"
            // tampilkan tombol back di dashboard
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // --- Perubahan: baca username dari shared preferences dan tampilkan di pill ---
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Username")
        val tvUsernamePill = findViewById<TextView>(R.id.tv_username_pill)
        tvUsernamePill?.text = username
        // --- end perubahan ---

        // muat fragment default (map ke One way)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_offers, menu_1())
            .commit()

        val segOne = findViewById<Button>(R.id.seg_one)
        val segRound = findViewById<Button>(R.id.seg_round)
        val segMulti = findViewById<Button>(R.id.seg_multi)

        val blue = ContextCompat.getColor(this, R.color.app_primary)
        val grayText = ContextCompat.getColor(this, R.color.app_text_secondary)
        val white = ContextCompat.getColor(this, R.color.app_white)

        fun activate(btn: Button) {
            btn.setTextColor(white)
            btn.setBackgroundColor(blue)
            btn.invalidate()
        }
        fun deactivate(btn: Button) {
            btn.setTextColor(grayText)
            btn.setBackgroundColor(Color.TRANSPARENT)
            btn.invalidate()
        }

        activate(segOne)
        deactivate(segRound)
        deactivate(segMulti)

        segOne.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, menu_1())
                .commit()
            activate(segOne)
            deactivate(segRound)
            deactivate(segMulti)
        }

        segRound.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, menu_2())
                .commit()
            activate(segRound)
            deactivate(segOne)
            deactivate(segMulti)
        }

        segMulti.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, menu_1())
                .commit()
            activate(segMulti)
            deactivate(segOne)
            deactivate(segRound)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}