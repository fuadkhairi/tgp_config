package com.engx1.thegympodtvapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.engx1.thegympodtvapp.ui.MainActivity
import com.engx1.thegympodtvapp.ui.MusicActivity

class LaunchPlayerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifyIntent = Intent(context, MusicActivity::class.java)
        context.startActivity(notifyIntent)
    }
}
