package com.stark.superisland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.stark.superisland.ui.theme.StarkIslandTheme // Ensure this matches your color file

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarkIslandTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Text(text = "STARK SYSTEM: ONLINE")
                }
            }
        }
    }
}
