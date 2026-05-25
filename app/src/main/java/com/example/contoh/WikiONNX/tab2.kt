package com.example.contoh.WikiONNX

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contoh.R
import androidx.core.text.HtmlCompat
import android.widget.TextView

class tab2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab2, container, false)

        val storage = view.findViewById<TextView>(R.id.content_storage)
        val codeExample = view.findViewById<TextView>(R.id.code_example)

        val storageHtml = """
    <p>File <b>.onnx</b> disimpan sebagai file biner berbasis Protocol Buffers. Di dalamnya terdapat struktur lengkap yang merepresentasikan model, sehingga model dapat digunakan kembali tanpa bergantung pada framework asalnya.</p>

    <ul>
        <li><b>Graph</b>: berisi rangkaian node dan operator yang menentukan alur perhitungan, termasuk input, output, serta hubungan antar node.</li>
        <li><b>Initializer</b>: menyimpan nilai parameter seperti bobot dan bias sehingga model dapat langsung digunakan tanpa proses pelatihan ulang.</li>
        <li><b>Metadata</b>: informasi tambahan seperti nama model, versi, dan penjelasan singkat.</li>
        <li><b>Opset Version</b>: menentukan versi operator ONNX yang dipakai untuk menjamin kompatibilitas di berbagai runtime.</li>
    </ul>

    <p>Format ini dirancang agar portable, sehingga model dapat dijalankan di berbagai platform seperti server, mobile, hingga perangkat edge menggunakan ONNX Runtime atau mesin eksekusi lain yang mendukung ONNX.</p>

    <p>Selain itu, file ONNX mudah dioptimalkan menggunakan alat seperti <b>onnxoptimizer</b> atau <b>quantization tools</b>, yang dapat membuat model lebih ringan dan cepat tanpa mengubah arsitektur inti.</p>
""".trimIndent()




        storage.text = HtmlCompat.fromHtml(storageHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)

        return view
    }

}