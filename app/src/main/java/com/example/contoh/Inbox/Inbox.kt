package com.example.contoh.Inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.contoh.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [Inbox.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inbox : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Inbox"
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        // Data dummy inbox (minimal 10 item)
        val dataListWithDesc = listOf(
            mapOf("title" to "Pesanan #A001", "desc" to "Tiket konser - Menunggu konfirmasi"),
            mapOf("title" to "Promo Terbaru", "desc" to "Diskon 30% untuk pemesanan hari ini"),
            mapOf("title" to "Pesanan #A002", "desc" to "Pembayaran diterima"),
            mapOf("title" to "Notifikasi Sistem", "desc" to "Perubahan syarat dan ketentuan"),
            mapOf("title" to "Pesanan #A003", "desc" to "Jadwal perjalanan dikonfirmasi"),
            mapOf("title" to "Update Profil", "desc" to "Email berhasil diperbarui"),
            mapOf("title" to "Penawaran Eksklusif", "desc" to "Kode: VIP20 berlaku 3 hari"),
            mapOf("title" to "Pesanan #A004", "desc" to "Pembatalan berhasil"),
            mapOf("title" to "Survei Kepuasan", "desc" to "Beri ulasan dan dapatkan poin"),
            mapOf("title" to "Pengingat", "desc" to "Acara dimulai dalam 24 jam")
        )

        val adapter = SimpleAdapter(
            requireContext(),
            dataListWithDesc,
            android.R.layout.simple_list_item_2,
            arrayOf("title", "desc"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        val listView = view.findViewById<ListView>(R.id.listViewItems)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = dataListWithDesc[position]
            val title = selected["title"]
            val desc = selected["desc"]
            Toast.makeText(requireContext(), "$title — $desc", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Inbox.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Inbox().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}