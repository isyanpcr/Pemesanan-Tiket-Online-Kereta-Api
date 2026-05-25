package com.example.contoh.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contoh.R
import com.example.contoh.data.api.TrainApiClient
import com.example.contoh.data.model.Ticket
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class APIView1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apiview1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnFilter = findViewById<Button>(R.id.btnFilter)
        val fab = findViewById<FloatingActionButton>(R.id.fabRefresh)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = HorizontalTicketAdapter(emptyList())

        fetchTickets(recyclerView)

        fab.setOnClickListener {
            fab.isEnabled = false
            fetchTickets(recyclerView) {
                fab.isEnabled = true
                Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show()
            }
        }


        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchTickets(recyclerView: RecyclerView, onDone: (() -> Unit)? = null) {
        lifecycleScope.launch {
            try {
                val list: List<Ticket> = withContext(Dispatchers.IO) {
                    TrainApiClient.apiService.getTickets()
                }
                recyclerView.adapter = HorizontalTicketAdapter(list)
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onDone?.invoke()
            }
        }
    }
}

class HorizontalTicketAdapter(private val data: List<Ticket>) : RecyclerView.Adapter<HorizontalTicketViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalTicketViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_horizontal, parent, false)
        return HorizontalTicketViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HorizontalTicketViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class HorizontalTicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvName = itemView.findViewById<TextView>(R.id.tvName)
    private val tvId = itemView.findViewById<TextView>(R.id.tvId)
    private val tvRoute = itemView.findViewById<TextView>(R.id.tvRoute)
    private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)

    fun bind(item: Ticket) {
        tvName.text = item.nama_kereta
        tvId.text = "ID: ${item.id ?: "-"}"
        tvRoute.text = "${item.stasiun_asal} → ${item.stasiun_tujuan}"
        tvTime.text = "Berangkat: ${item.jam_berangkat}"
    }
}