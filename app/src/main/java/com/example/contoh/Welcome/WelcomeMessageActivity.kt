package com.example.contoh.Welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.contoh.MainActivity
import com.example.contoh.R
import com.example.contoh.databinding.ActivityWelcomeMessageBinding

class WelcomeMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWelcomeMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentsList = listOf(WelcomeFragment1(), WelcomeFragment2(), WelcomeFragment3())
        val adapter = WelcomeFragmentAdapter(this, fragmentsList)
        binding.welcomeMessageViewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.welcomeMessageViewPager)

        binding.btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.welcomeMessageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnSkip.visibility = if (position == fragmentsList.size - 1) View.GONE else View.VISIBLE
            }
        })
    }
}