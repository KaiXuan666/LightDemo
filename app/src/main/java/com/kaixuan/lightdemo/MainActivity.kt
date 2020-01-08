package com.kaixuan.lightdemo

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val media = MediaPlayer()
        media.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
            media.setScreenOnWhilePlaying(true)
        startActivityForResult(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ), 1
        )
        startService(Intent(this, LightService::class.java))
    }
}
