package com.example.contoh.Home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contoh.R
import com.example.contoh.data.AppDatabase
import com.example.contoh.data.entity.NoteEntity
import com.example.contoh.databinding.ActivityPemesananBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PemesananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPemesananBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPemesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Pemesanan Tiket"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getInstance(this)

        // setup spinner options (bebas)
        val jenisOptions = listOf("Ekonomi", "Bisnis", "Eksekutif")
        val stasiunOptions = listOf("Gambir", "Pasar Senen", "Bandung", "Surabaya", "Yogyakarta")

        binding.spinnerJenis.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, jenisOptions)
        binding.spinnerAsal.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stasiunOptions)
        binding.spinnerTujuan.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stasiunOptions)

        // date picker
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.etDate.setOnClickListener {
            val dp = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                binding.etDate.setText(sdf.format(calendar.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dp.show()
        }

        // setup recycler
        val rv = binding.rvNotes
        rv.layoutManager = LinearLayoutManager(this)
        // tambah divider untuk tampilan lebih baik
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = NoteAdapter(emptyList())
        rv.adapter = adapter

        // toggle form visibility (form awal tersembunyi berdasarkan layout)
        binding.btnToggleForm.setOnClickListener {
            val willShow = binding.cardForm.visibility != View.VISIBLE
            if (willShow) {
                binding.cardForm.alpha = 0f
                binding.cardForm.visibility = View.VISIBLE
                binding.cardForm.animate().alpha(1f).setDuration(200).start()
            } else {
                binding.cardForm.animate().alpha(0f).setDuration(180).withEndAction {
                    binding.cardForm.visibility = View.GONE
                    binding.cardForm.alpha = 1f
                }.start()
            }
            binding.btnToggleForm.text = if (willShow) "Tutup Form" else "Tambah Pemesanan"
        }

        binding.btnSaveNote.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val jenis = binding.spinnerJenis.selectedItem?.toString() ?: ""
            val asal = binding.spinnerAsal.selectedItem?.toString() ?: ""
            val tujuan = binding.spinnerTujuan.selectedItem?.toString() ?: ""
            val tanggal = binding.etDate.text.toString().trim()

            if (name.isBlank() || jenis.isBlank() || asal.isBlank() || tujuan.isBlank() || tanggal.isBlank()) {
                Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val note = NoteEntity(
                    name = name,
                    jenis = jenis,
                    stasiun_asal = asal,
                    stasiun_tujuan = tujuan,
                    tanggal_berangkat = tanggal,
                    createdAt = System.currentTimeMillis()
                )
                withContext(Dispatchers.IO) { db.noteDao().insert(note) }
                // refresh list
                loadNotes()
                runOnUiThread {
                    Toast.makeText(this@PemesananActivity, "Tersimpan!", Toast.LENGTH_SHORT).show()
                    binding.etName.setText("")
                    binding.spinnerJenis.setSelection(0)
                    binding.spinnerAsal.setSelection(0)
                    binding.spinnerTujuan.setSelection(0)
                    binding.etDate.setText("")
                    // tutup form setelah simpan agar fokus ke list
                    binding.cardForm.animate().alpha(0f).setDuration(180).withEndAction {
                        binding.cardForm.visibility = View.GONE
                        binding.cardForm.alpha = 1f
                        binding.btnToggleForm.text = "Tambah Pemesanan"
                    }.start()
                }
            }
        }

        // initial load
        loadNotes()
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                db.noteDao().getAll()
            }
            adapter.update(list)
            // toggle empty state
            runOnUiThread {
                binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    inner class NoteAdapter(private var items: List<NoteEntity>) : RecyclerView.Adapter<NoteAdapter.VH>() {
        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvItemName)
            val tvJenis: TextView = view.findViewById(R.id.tvItemJenis)
            val tvTanggal: TextView = view.findViewById(R.id.tvItemTanggal)
            val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
            val tvAsal: TextView = view.findViewById(R.id.tvItemAsal)
            val tvTujuan: TextView = view.findViewById(R.id.tvItemTujuan)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name
            holder.tvJenis.text = "Jenis: ${item.jenis}"
            holder.tvTanggal.text = item.tanggal_berangkat
            holder.tvAsal.text = "Asal: ${item.stasiun_asal}"
            holder.tvTujuan.text = "Tujuan: ${item.stasiun_tujuan}"
            holder.btnDelete.setOnClickListener {
                AlertDialog.Builder(this@PemesananActivity)
                    .setTitle("Konfirmasi")
                    .setMessage("Hapus pemesanan ini?")
                    .setPositiveButton("Hapus") { dialog, _ ->
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) { db.noteDao().deleteById(item.id) }
                            loadNotes()
                            runOnUiThread {
                                Toast.makeText(this@PemesananActivity, "Dihapus", Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        override fun getItemCount(): Int = items.size

        fun update(newList: List<NoteEntity>) {
            items = newList
            notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pemesanan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tugas -> {
                startActivity(Intent(this, TugasActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}
