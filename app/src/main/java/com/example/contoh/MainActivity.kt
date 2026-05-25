package com.example.contoh

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contoh.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.chipGroupRole.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                Toast.makeText(this, "Login sebagai: ${chip.text}", Toast.LENGTH_SHORT).show()

                sharedPref.edit().putString("selectedRole", chip.text.toString()).apply()
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val selectedRole = sharedPref.getString("selectedRole", "Penumpang")

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) binding.etUsername.error = "Isi username"
                if (password.isEmpty()) binding.etPassword.error = "Isi password"
                Toast.makeText(this, "Lengkapi username dan password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username == password) {
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                editor.putString("userRole", selectedRole)
                editor.apply()

                Toast.makeText(this, "Login berhasil sebagai $selectedRole", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HalamanAwal::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.etPassword.error = "Password tidak sama dengan username"
                Toast.makeText(this, "Username dan password harus sama", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
