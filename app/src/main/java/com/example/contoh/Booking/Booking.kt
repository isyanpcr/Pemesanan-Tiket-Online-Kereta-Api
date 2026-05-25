package com.example.contoh.Booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.contoh.Booking.BookingModel
import com.example.contoh.databinding.FragmentBookingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [Booking.newInstance] factory method to
 * create an instance of this fragment.
 */
class Booking : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookingList = listOf(
            BookingModel(
                "BKG001",
                "Alya Santoso",
                "Argo Parahyangan",
                "2025-11-01",
                "08:30",
                "5A",
                "IDR 120.000",
                "https://picsum.photos/id/1011/200/200"
            ),
            BookingModel(
                "BKG002",
                "Rudi Hartono",
                "Tegal Ekspres",
                "2025-11-02",
                "10:15",
                "12B",
                "IDR 85.000",
                "https://picsum.photos/id/1012/200/200"
            ),
            BookingModel(
                "BKG003", "Siti Nur", "Lokal Cirebon", "2025-11-03", "06:45", "3C", "IDR 40.000",
                "https://picsum.photos/id/1013/200/200"
            ),
            BookingModel(
                "BKG004", "Budi Santoso", "Argo Wilis", "2025-11-05", "14:00", "7D", "IDR 200.000",
                "https://picsum.photos/id/1015/200/200"
            ),
            BookingModel(
                "BKG005", "Maya Putri", "Senja Utama", "2025-11-07", "19:20", "2A", "IDR 150.000",
                "https://picsum.photos/id/1025/200/200"
            )
        )

        val adapter = BookingAdapter(requireContext(), bookingList)
        binding.listBookings.adapter = adapter

        // set Toolbar title dan daftarkan ke activity, sama seperti Inbox
        binding.toolbarBooking.title = "Booking"
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarBooking)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Booking.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Booking().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}