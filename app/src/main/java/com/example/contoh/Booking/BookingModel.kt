package com.example.contoh.Booking

data class BookingModel(
    val bookingId: String,
    val passengerName: String,
    val trainName: String,
    val date: String,
    val time: String,
    val seat: String,
    val price: String,
    val photoUrl: String
)