package com.example.contoh

//
//
//
//package com.example.contoh
//
//import android.app.AlertDialog
//import android.content.Context
//import android.graphics.BitmapFactory
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.ImageView
//import android.widget.TextView
//import java.net.HttpURLConnection
//import java.net.URL
//
//class BookingAdapter(
//    private val ctx: Context,
//    private val bookings: List<BookingModel>
//) : ArrayAdapter<BookingModel>(ctx, 0, bookings) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = convertView ?: LayoutInflater.from(ctx).inflate(
//            R.layout.item_booking, parent, false
//        )
//
//        val data = bookings[position]
//
//        val imgTrain = view.findViewById<ImageView>(R.id.imgTrain)
//        val textTrainName = view.findViewById<TextView>(R.id.textTrainName)
//        val textPassenger = view.findViewById<TextView>(R.id.textPassenger)
//        val textDateTime = view.findViewById<TextView>(R.id.textDateTime)
//        val textSeat = view.findViewById<TextView>(R.id.textSeat)
//        val textPrice = view.findViewById<TextView>(R.id.textPrice)
//
//        // default icon while loading/fallback
//        imgTrain.setImageResource(android.R.drawable.ic_menu_myplaces)
//
//        // try load remote photo (simple loader, no external lib)
//        loadImageFromUrl(data.photoUrl, imgTrain)
//
//        textTrainName.text = data.trainName
//        textPassenger.text = "Penumpang: ${data.passengerName}"
//        textDateTime.text = "${data.date} • ${data.time}"
//        textSeat.text = "Seat: ${data.seat}"
//        textPrice.text = data.price
//
//        view.setOnClickListener {
//            // inflate layout dialog
//            val dialogView = LayoutInflater.from(ctx).inflate(R.layout.dialog_booking_detail, null)
//
//            val dlgImg = dialogView.findViewById<ImageView>(R.id.dialog_imgTrain)
//            val dlgBookingId = dialogView.findViewById<TextView>(R.id.dialog_textBookingId)
//            val dlgPassenger = dialogView.findViewById<TextView>(R.id.dialog_textPassenger)
//            val dlgTrain = dialogView.findViewById<TextView>(R.id.dialog_textTrain)
//            val dlgDate = dialogView.findViewById<TextView>(R.id.dialog_textDate)
//            val dlgTime = dialogView.findViewById<TextView>(R.id.dialog_textTime)
//            val dlgSeat = dialogView.findViewById<TextView>(R.id.dialog_textSeat)
//            val dlgPrice = dialogView.findViewById<TextView>(R.id.dialog_textPrice)
//
//            // set placeholder then load actual image
//            dlgImg.setImageResource(android.R.drawable.ic_menu_myplaces)
//            loadImageFromUrl(data.photoUrl, dlgImg)
//
//            // set texts
//            dlgBookingId.text = "Booking ID: ${data.bookingId}"
//            dlgPassenger.text = "Penumpang: ${data.passengerName}"
//            dlgTrain.text = "Kereta: ${data.trainName}"
//            dlgDate.text = "Tanggal: ${data.date}"
//            dlgTime.text = "Jam: ${data.time}"
//            dlgSeat.text = "Kursi: ${data.seat}"
//            dlgPrice.text = "Harga: ${data.price}"
//
//            // show dialog
//            AlertDialog.Builder(ctx)
//                .setTitle("Detail Booking")
//                .setView(dialogView)
//                .setPositiveButton("Tutup", null)
//                .show()
//        }
//
//        return view
//    }
//
//    // Simple image loader (background thread). Good for small demo/dummy images.
//    private fun loadImageFromUrl(urlStr: String, target: ImageView) {
//        if (urlStr.isBlank()) return
//        Thread {
//            try {
//                val url = URL(urlStr)
//                val conn = url.openConnection() as HttpURLConnection
//                conn.doInput = true
//                conn.connectTimeout = 5000
//                conn.readTimeout = 5000
//                conn.connect()
//                val input = conn.inputStream
//                val bmp = BitmapFactory.decodeStream(input)
//                input.close()
//                target.post {
//                    if (bmp != null) {
//                        target.setImageBitmap(bmp)
//                    } else {
//                        target.setImageResource(android.R.drawable.ic_menu_myplaces)
//                    }
//                }
//            } catch (e: Exception) {
//                // fallback on error
//                target.post { target.setImageResource(android.R.drawable.ic_menu_myplaces) }
//            }
//        }.start()
//    }
//}
