package com.example.contoh.Home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.contoh.R
import com.example.contoh.menu_1
import com.example.contoh.menu_2

class Home : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Edge-to-edge via activity
        (requireActivity()).enableEdgeToEdge()

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "SumBar ekspres"
            subtitle = "Dashboard Utama"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val sharedPref = requireActivity().getSharedPreferences("user_pref", AppCompatActivity.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Username")
        val tvUsernamePill = view.findViewById<TextView>(R.id.tv_username_pill)
        tvUsernamePill?.text = username

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_offers, menu_1())
            .commit()

        val segOne = view.findViewById<Button>(R.id.seg_one)
        val segRound = view.findViewById<Button>(R.id.seg_round)
        val segMulti = view.findViewById<Button>(R.id.seg_multi)
        val blue = ContextCompat.getColor(requireContext(), R.color.app_primary)
        val grayText = ContextCompat.getColor(requireContext(), R.color.app_text_secondary)
        val white = ContextCompat.getColor(requireContext(), R.color.app_white)

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
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, menu_1())
                .commit()
            activate(segOne); deactivate(segRound); deactivate(segMulti)
        }

        segRound.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, menu_2())
                .commit()
            activate(segRound); deactivate(segOne); deactivate(segMulti)
        }

        segMulti.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, menu_1())
                .commit()
            activate(segMulti); deactivate(segOne); deactivate(segRound)
        }

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}