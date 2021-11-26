package com.ddona.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MySecondBroadcastManager : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("doanpt", "Receive action2:" + intent?.action)
    }
}