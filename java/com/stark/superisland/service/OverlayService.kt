package com.stark.superisland.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import com.stark.superisland.R

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingView = LayoutInflater.from(this).inflate(R.layout.stark_island_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 50
        }

        windowManager.addView(floatingView, params)
        animateIsland()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra("TITLE") ?: "Stark System"
        floatingView.findViewById<TextView>(R.id.island_text).text = title
        animateIsland()
        return START_STICKY
    }

    private fun animateIsland() {
        floatingView.scaleX = 0.5f
        floatingView.animate().scaleX(1.1f).setDuration(300).setInterpolator(OvershootInterpolator()).withEndAction {
            floatingView.animate().scaleX(1.0f).setDuration(100).start()
        }.start()
    }
}
