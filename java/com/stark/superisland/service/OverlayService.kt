package com.stark.superisland.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.stark.superisland.R
import com.stark.superisland.core.AnimationUtils

class StarkOverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var starkBaseView: View
    private var isIslandExpanded = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Inflate the professional Base Container
        starkBaseView = LayoutInflater.from(this).inflate(R.layout.stark_island_base, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 25 // Stark style position
        }

        windowManager.addView(starkBaseView, params)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra("STARK_TITLE") ?: "System Active"
        updateStarkContent(title)
        return START_STICKY
    }

    private fun updateStarkContent(title: String) {
        val textView = starkBaseView.findViewById<TextView>(R.id.stark_music_title)
        val progressBar = starkBaseView.findViewById<ProgressBar>(R.id.stark_music_progress)
        
        textView?.text = title
        
        // Trigger professional physics-based animation
        AnimationUtils.popIn(starkBaseView)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::starkBaseView.isInitialized) windowManager.removeView(starkBaseView)
    }
}
