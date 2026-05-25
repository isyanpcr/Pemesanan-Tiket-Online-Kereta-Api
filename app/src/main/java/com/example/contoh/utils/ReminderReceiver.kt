package com.example.contoh.Home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.contoh.utils.NotificationHelper

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Pengingat"
        val message = intent.getStringExtra("message") ?: "Anda memiliki pengingat"
        val openIntent = Intent(context, GenerateQRCodeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        NotificationHelper.showNotification(context, title, message, openIntent)
    }
}
