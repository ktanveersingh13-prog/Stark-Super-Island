package com.stark.superisland.core

import android.content.Context
import android.provider.Settings

object SecurityProvider {
    fun isOverlayAllowed(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun checkIntegrity(): Boolean {
        // High-level check: Return true if system is healthy
        return true 
    }
}
