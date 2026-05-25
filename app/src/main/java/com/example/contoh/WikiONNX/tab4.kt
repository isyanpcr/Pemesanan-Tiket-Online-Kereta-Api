package com.example.contoh.WikiONNX

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import android.widget.TextView
import com.example.contoh.R

class tab4 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab4, container, false)

        val desc = view.findViewById<TextView>(R.id.content_dataset)
        val features = view.findViewById<TextView>(R.id.content_features)

        val descHtml = """
            <p>Dataset yang digunakan adalah <b>Train Ticket Price</b> dari Kaggle. Dataset ini menyediakan informasi tentang rute perjalanan kereta, jadwal keberangkatan, jenis layanan, kelas tiket, tarif, dan harga akhir. Kombinasi informasi tersebut membuat dataset ini ideal untuk membangun model prediksi harga tiket.</p>

            <p>Data ini dipilih karena mencerminkan kondisi nyata di mana harga tiket sangat dipengaruhi oleh rute, waktu perjalanan, jenis kereta, serta kelas layanan. Dengan variasi fitur yang cukup luas, model dapat mempelajari hubungan antar faktor tersebut dengan lebih baik.</p>

            <p>Dataset seperti ini cocok untuk workflow ONNX karena model regresi atau rekomendasi yang dilatih di framework ML dapat dikonversi menjadi model ONNX sehingga lebih mudah dipindahkan ke berbagai platform dan dijalankan dengan performa yang stabil.</p>
        """.trimIndent()

        val featuresText = """
            Fitur yang digunakan dan alasannya:
            - insert_date: membantu mempelajari perubahan harga dari waktu ke waktu.
            - origin, destination: rute perjalanan adalah faktor utama yang menentukan harga.
            - start_date, end_date: waktu keberangkatan memberi informasi pola hari, jam, atau musim.
            - train_type: jenis kereta biasanya memiliki rentang harga yang berbeda.
            - train_class: kelas tiket adalah pembeda harga yang paling langsung.
            - fare: memberikan konteks tambahan terkait tipe tarif yang digunakan.
            - Unnamed:0: bersifat seperti indeks dan dapat diabaikan bila tidak dibutuhkan.

            Output:
            - price sebagai target numerik untuk tugas regresi.

            Cara ONNX digunakan dalam pemrosesan:
            1) Model dilatih menggunakan framework seperti scikit-learn, XGBoost, LightGBM, atau neural network.
            2) Tahapan preprocessing seperti encoding kategori dan pemrosesan tanggal digabungkan ke pipeline model.
            3) Pipeline lengkap dikonversi ke format ONNX agar model lebih mudah dipindahkan dan digunakan lintas platform.
            4) Model ONNX dijalankan menggunakan ONNX Runtime sehingga inference berjalan lebih cepat dan efisien.
        """.trimIndent()

        desc.text = HtmlCompat.fromHtml(descHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        features.text = featuresText

        return view
    }
}
