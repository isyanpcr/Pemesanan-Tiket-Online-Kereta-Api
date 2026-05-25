package com.example.contoh.WikiONNX

import ai.onnxruntime.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.contoh.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.nio.FloatBuffer

class tab3 : Fragment() {

    private lateinit var env: OrtEnvironment
    private lateinit var session: OrtSession

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_tab3, container, false)

        val etFertilizer = view.findViewById<TextInputEditText>(R.id.et_fertilizer)
        val etTemp = view.findViewById<TextInputEditText>(R.id.et_temp)
        val etN = view.findViewById<TextInputEditText>(R.id.et_n)
        val etP = view.findViewById<TextInputEditText>(R.id.et_p)
        val etK = view.findViewById<TextInputEditText>(R.id.et_k)

        val tvResult = view.findViewById<TextView>(R.id.result)
        val btnPrediksi = view.findViewById<MaterialButton>(R.id.btnPrediksi)

        try {
            initONNX()
        } catch (e: Exception) {
            tvResult.text = "Gagal load model: ${e.message}"
        }

        btnPrediksi.setOnClickListener {
            val f1 = etFertilizer.text.toString().trim()
            val f2 = etTemp.text.toString().trim()
            val f3 = etN.text.toString().trim()
            val f4 = etP.text.toString().trim()
            val f5 = etK.text.toString().trim()

            if (f1.isEmpty() || f2.isEmpty() || f3.isEmpty() || f4.isEmpty() || f5.isEmpty()) {
                tvResult.text = "Semua input harus diisi!"
                return@setOnClickListener
            }

            try {
                val inputArray = floatArrayOf(
                    f1.toFloat(),
                    f2.toFloat(),
                    f3.toFloat(),
                    f4.toFloat(),
                    f5.toFloat()
                )

                val result = runInference(inputArray)
                tvResult.text = "Hasil: $result"

            } catch (e: Exception) {
                tvResult.text = "Input harus berupa angka!"
            }
        }

        return view
    }

    private fun initONNX() {
        env = OrtEnvironment.getEnvironment()
        val modelBytes = requireContext().assets
            .open("crop_yield_modelV5.onnx")
            .readBytes()
        session = env.createSession(modelBytes)
    }

    private fun runInference(input: FloatArray): Float {
        val shape = longArrayOf(1, input.size.toLong())
        val tensor = OnnxTensor.createTensor(
            env,
            FloatBuffer.wrap(input),
            shape
        )

        val inputName = session.inputNames.iterator().next()
        val result = session.run(mapOf(inputName to tensor))
        val output = result[0].value as Array<FloatArray>

        return output[0][0]
    }
}
