package com.stark.superisland.models

enum class IslandState {
    IDLE,      // Hidden
    COMPACT,   // Small pill
    EXPANDED,  // Large view (Music/Notification details)
    GAMING     // Smallest view for BGMI/COD
}

data class StarkState(
    var currentState: IslandState = IslandState.IDLE,
    var activeApp: String? = null
)
