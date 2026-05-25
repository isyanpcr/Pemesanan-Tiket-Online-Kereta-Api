package com.example.contoh

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.contoh.Booking.Booking
import com.example.contoh.Home.Home
import com.example.contoh.Inbox.Inbox
import com.example.contoh.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // muat Home fragment sebagai default
        replaceFragment(Home())

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(Home())
                    true
                }
                R.id.message -> {
                    replaceFragment(Booking())
                    true
                }
                R.id.inbox -> {
                    replaceFragment(Inbox())
                    true
                }
                R.id.profile -> {
                    Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            // .addToBackStack(null) // sengaja nonaktifkan agar back keluar aplikasi
            .commit()
    }
}
