package com.stark.superisland

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // This is a simple button to take you to the Notification Settings
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startContext(intent)
        
        finish() // Closes the screen after opening settings
    }

    private fun startContext(intent: Intent) {
        startActivity(intent)
    }
}
