package com.example.contoh.Welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contoh.R
import com.example.contoh.MainActivity
import com.google.android.material.button.MaterialButton

class WelcomeFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol menuju halaman login (MainActivity)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btn_go_login)
        btnLogin?.setOnClickListener {
            val ctx = requireContext()
            val intent = Intent(ctx, MainActivity::class.java)
            // Clear stack supaya user tidak kembali ke welcome setelah login
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}