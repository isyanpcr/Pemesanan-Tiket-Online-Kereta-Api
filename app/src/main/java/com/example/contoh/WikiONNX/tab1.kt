package com.example.contoh.WikiONNX

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contoh.R
import androidx.core.text.HtmlCompat
import android.widget.TextView

class tab1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab1, container, false)

        val overview = view.findViewById<TextView>(R.id.content_overview)
        val example = view.findViewById<TextView>(R.id.content_example)

        val overviewHtml = """
    <p>ONNX (Open Neural Network Exchange) adalah format terbuka yang dirancang agar model machine learning bisa berpindah dari satu framework ke framework lain tanpa harus diulang dari awal. Ide utamanya adalah menyediakan standar bersama sehingga model yang dibuat di PyTorch, TensorFlow, atau scikit-learn dapat dieksekusi di berbagai platform.</p>

    <p><b>Tujuan:</b> menyatukan representasi model agar proses pengembangan dan deployment tidak tergantung pada satu ekosistem saja. Dengan ONNX, sebuah model dapat dilatih di satu tempat dan dijalankan di tempat lain.</p>

    <p><b>Manfaat:</b> memudahkan integrasi antar tim dan alat, mempercepat proses deploy ke server atau perangkat edge, meminimalkan konversi manual, dan membuka peluang optimisasi seperti kompresi model atau peningkatan kecepatan inference melalui ONNX Runtime.</p>

    <p><b>Format Model:</b> ONNX menyimpan struktur model sebagai graph komputasi yang terdiri dari node (operator), input-output, serta tensor penyimpan parameter. Semua ini dikemas dalam file biner berbasis Protocol Buffers sehingga mudah dibaca oleh berbagai runtime.</p>
""".trimIndent()


        val exampleHtml = """
    <p>Dalam penggunaan nyata, ONNX sering menjadi jembatan ketika model selesai dilatih namun perlu dijalankan di lingkungan dengan batasan tertentu. Misalnya, sebuah model yang dilatih di desktop menggunakan PyTorch bisa diekspor ke ONNX agar bisa dijalankan lebih cepat di server inference, disematkan ke aplikasi mobile, atau dioptimasi untuk perangkat edge yang memiliki sumber daya terbatas.</p>
""".trimIndent()


        overview.text = HtmlCompat.fromHtml(overviewHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        example.text = HtmlCompat.fromHtml(exampleHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)

        return view
    }
}