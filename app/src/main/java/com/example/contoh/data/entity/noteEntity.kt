package com.example.contoh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val jenis: String,
    val stasiun_asal: String,
    val stasiun_tujuan: String,
    val tanggal_berangkat: String,
    val createdAt: Long
)