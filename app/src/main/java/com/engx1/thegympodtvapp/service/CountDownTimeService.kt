package com.engx1.thegympodtvapp.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder

class CountDownTimeService: Service() {

    lateinit var counter: CountDownTimer
    var endTime: Long = 10000;

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        counter.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        endTime = intent!!.getLongExtra("end_time", 0)

        counter = object : CountDownTimer(endTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val time = String.format("%02d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60)
                val i = Intent("COUNTDOWN_UPDATED")
                i.putExtra("countdown", time)
                sendBroadcast(i)
            }

            override fun onFinish() {
                val i = Intent("COUNTDOWN_UPDATED")
                i.putExtra("countdown", "0")
                sendBroadcast(i)
                //Log.d("COUNTDOWN", "FINISH!");
                stopSelf()
            }
        }
        counter.start()
        return START_STICKY
    }
}