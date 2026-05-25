package com.example.contoh.data.model

data class Ticket(
    val id: Long? = null,
    val created_at: String? = null,
    val kode_kereta: String,
    val nama_kereta: String,
    val stasiun_asal: String,
    val stasiun_tujuan: String,
    val jam_berangkat: String,
    val jam_tiba: String,
    val tanggal_berangkat: String,
    val harga: Double,
    val kelas: String,
    val kuota: Int,
    val deskripsi: String
)