package com.example.contoh.Booking

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.contoh.Booking.BookingModel
import com.example.contoh.R
import java.net.HttpURLConnection
import java.net.URL

class BookingAdapter(
    private val ctx: Context,
    private val bookings: List<BookingModel>
) : ArrayAdapter<BookingModel>(ctx, 0, bookings) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(ctx).inflate(
            R.layout.item_booking, parent, false
        )

        val data = bookings[position]

        val imgTrain = view.findViewById<ImageView>(R.id.imgTrain)
        val textTrainName = view.findViewById<TextView>(R.id.textTrainName)
        val textPassenger = view.findViewById<TextView>(R.id.textPassenger)
        val textDateTime = view.findViewById<TextView>(R.id.textDateTime)
        val textSeat = view.findViewById<TextView>(R.id.textSeat)
        val textPrice = view.findViewById<TextView>(R.id.textPrice)

        // default icon while loading/fallback
        imgTrain.setImageResource(android.R.drawable.ic_menu_myplaces)

        // try load remote photo (simple loader, no external lib)
        loadImageFromUrl(data.photoUrl, imgTrain)

        textTrainName.text = data.trainName
        textPassenger.text = "Penumpang: ${data.passengerName}"
        textDateTime.text = "${data.date} • ${data.time}"
        textSeat.text = "Seat: ${data.seat}"
        textPrice.text = data.price

        view.setOnClickListener {
            val message = """
                Booking ID: ${data.bookingId}
                Penumpang: ${data.passengerName}
                Kereta: ${data.trainName}
                Tanggal: ${data.date}
                Jam: ${data.time}
                Kursi: ${data.seat}
                Harga: ${data.price}
            """.trimIndent()

            AlertDialog.Builder(ctx)
                .setTitle("Detail Booking")
                .setMessage(message)
                .setPositiveButton("Tutup", null)
                .show()
        }

        return view
    }

    // Simple image loader (background thread). Good for small demo/dummy images.
    private fun loadImageFromUrl(urlStr: String, target: ImageView) {
        if (urlStr.isBlank()) return
        Thread {
            try {
                val url = URL(urlStr)
                val conn = url.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.connect()
                val input = conn.inputStream
                val bmp = BitmapFactory.decodeStream(input)
                input.close()
                target.post {
                    if (bmp != null) {
                        target.setImageBitmap(bmp)
                    } else {
                        target.setImageResource(android.R.drawable.ic_menu_myplaces)
                    }
                }
            } catch (e: Exception) {
                // fallback on error
                target.post { target.setImageResource(android.R.drawable.ic_menu_myplaces) }
            }
        }.start()
    }
}