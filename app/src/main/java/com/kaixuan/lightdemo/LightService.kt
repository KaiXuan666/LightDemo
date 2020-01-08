package com.kaixuan.lightdemo

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager


class LightService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    val media = MediaPlayer()
    @SuppressLint("InvalidWakeLockTag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        media.start()
        media.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
        media.setScreenOnWhilePlaying(true)
        showFloatingWindow()
        return super.onStartCommand(intent, flags, startId)
    }
    var mStartX =  0
    var mStartY =  0

    private fun showFloatingWindow() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.getDefaultDisplay()
        val point = Point()
        display.getSize(point)
        Log.e("Point()", "Point=" + point)
//        1080, 2210
//        540, 1105
//        左下角：-540, 1105

        // 新建悬浮窗控件
//            val button = Button(applicationContext)
//            button.setText("Floating Window")
//            button.setBackgroundColor(Color.BLUE)
        // 设置LayoutParam
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.width = 1
        layoutParams.height = 1
        layoutParams.x = -10000
        layoutParams.y = 20000
        // 将悬浮窗控件添加到WindowManager
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        val view = View.inflate(applicationContext, R.layout.layout_test, null)
        view.setOnTouchListener(OnTouchListener { v, event ->
            // 当前值以屏幕左上角为原点
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mStartX = event.rawX.toInt()
                    mStartY = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    layoutParams.x += event.rawX.toInt() - mStartX
                    layoutParams.y += event.rawY.toInt() - mStartY
                    Log.e("位置", "layoutParams.x=" + layoutParams.x + ", layoutParams.y" + layoutParams.y)
                    windowManager.updateViewLayout(view, layoutParams)
                    mStartX = event.rawX.toInt()
                    mStartY = event.rawY.toInt()
                }
                MotionEvent.ACTION_UP -> {
                }
            }
            // 消耗触摸事件
            true
        })
        windowManager.addView(view, layoutParams)
    }

}


