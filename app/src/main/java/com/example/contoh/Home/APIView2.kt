package com.example.contoh.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contoh.R
import com.example.contoh.data.api.TrainApiClient
import com.example.contoh.data.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class APIView2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apiview2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerGrid = findViewById<RecyclerView>(R.id.recyclerGrid)
        val btnBack2 = findViewById<ImageButton>(R.id.btnBack2)

        btnBack2.setOnClickListener {
            finish()
        }

        recyclerGrid.layoutManager = GridLayoutManager(this, 2)
        recyclerGrid.adapter = GridTicketAdapter(emptyList())

        lifecycleScope.launch {
            try {
                val list: List<Ticket> = withContext(Dispatchers.IO) {
                    TrainApiClient.apiService.getTickets()
                }
                recyclerGrid.adapter = GridTicketAdapter(list)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}

class GridTicketAdapter(private val data: List<Ticket>) : RecyclerView.Adapter<GridTicketViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridTicketViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_grid, parent, false)
        return GridTicketViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: GridTicketViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class GridTicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvName = itemView.findViewById<TextView>(R.id.tvGridName)
    private val tvId = itemView.findViewById<TextView>(R.id.tvGridId)
    private val tvDate = itemView.findViewById<TextView>(R.id.tvGridDate)
    private val tvPrice = itemView.findViewById<TextView>(R.id.tvGridPrice)
    private val tvQuota = itemView.findViewById<TextView>(R.id.tvGridQuota)
    private val tvDesc = itemView.findViewById<TextView>(R.id.tvGridDesc)

    fun bind(item: Ticket) {
        tvName.text = item.nama_kereta
        tvId.text = "ID: ${item.id ?: "-"}"
        tvDate.text = item.tanggal_berangkat
        tvPrice.text = "Rp ${item.harga}"
        tvQuota.text = "Kuota: ${item.kuota}"
        tvDesc.text = item.deskripsi
    }
}