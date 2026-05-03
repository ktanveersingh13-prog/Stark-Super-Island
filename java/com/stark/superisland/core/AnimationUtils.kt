package com.stark.superisland.core

import android.view.View
import android.view.animation.OvershootInterpolator

object AnimationUtils {
    fun popIn(view: View) {
        view.alpha = 0f
        view.scaleX = 0.3f
        view.scaleY = 0.3f
        
        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(StarkConstants.ANIM_DURATION_SHORT)
            .setInterpolator(OvershootInterpolator(StarkConstants.BOUNCE_INTENSITY))
            .start()
    }

    fun slideOut(view: View, onEnd: () -> Unit) {
        view.animate()
            .translationY(-200f)
            .alpha(0f)
            .setDuration(StarkConstants.ANIM_DURATION_SHORT)
            .withEndAction { onEnd() }
            .start()
    }
}
