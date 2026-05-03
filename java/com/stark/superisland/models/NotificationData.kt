package com.stark.superisland.models

data class NotificationData(
    val id: Int,
    val packageName: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isMusic: Boolean = false
)
