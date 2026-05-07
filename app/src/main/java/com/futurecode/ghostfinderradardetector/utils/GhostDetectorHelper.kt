package com.futurecode.ghostfinderradardetector.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.futurecode.ghostfinderradardetector.model.GhostModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GhostDetectorHelper(
    private val context: Context,
    private val detectedGhostView: ImageView,
    private val lottieAnimation: LottieAnimationView,
    private val ghostIndicatorView: ImageView,
    private val scope: CoroutineScope
) {
    private var isSearching = false

    /**
     * Starts the ghost detection process.
     * @param isNoHarm Filters ghosts by danger level (Low/Mid for No Harm, High/Extreme for Horror).
     * @param onResult Callback invoked when detection finishes.
     */
    fun startDetection(isNoHarm: Boolean, onResult: (GhostModel?) -> Unit) {
        if (isSearching) return
        isSearching = true

        // Reset center ghost visibility
        detectedGhostView.visibility = View.GONE
        detectedGhostView.clearAnimation()

        scope.launch(Dispatchers.Main) {
            // 1. Start UI Animations
            // Blink the top toolbar indicator
            val blink = AlphaAnimation(1f, 0.2f).apply {
                duration = 400
                repeatCount = Animation.INFINITE
                repeatMode = Animation.REVERSE
            }
            ghostIndicatorView.startAnimation(blink)
            
            // Speed up the radar rotation during scan
            lottieAnimation.speed = 2.0f
            lottieAnimation.playAnimation()

            // 2. Simulate scanning delay (3 to 6 seconds)
            delay(Random.nextLong(3000, 6000))

            // 3. Stop scanning animations
            ghostIndicatorView.clearAnimation()
            lottieAnimation.speed = 1.0f

            // 4. Result Logic (70% success rate)
            val isFound = Random.nextFloat() < 0.7f

            if (isFound) {
                // Filter ghosts based on the selected category
                val filteredGhosts = if (isNoHarm) {
                    GhostData.ghosts.filter { 
                        it.dangerLevel.contains("Low", ignoreCase = true) || 
                        it.dangerLevel.contains("Mid", ignoreCase = true) 
                    }
                } else {
                    GhostData.ghosts.filter { 
                        it.dangerLevel.contains("High", ignoreCase = true) || 
                        it.dangerLevel.contains("Extreme", ignoreCase = true) 
                    }
                }

                if (filteredGhosts.isNotEmpty()) {
                    val ghost = filteredGhosts.random()
                    
                    // Reveal ghost silhouette in the radar center
                    detectedGhostView.setImageResource(ghost.image)
                    detectedGhostView.visibility = View.VISIBLE
                    val fadeIn = AlphaAnimation(0f, 0.6f).apply {
                        duration = 1000
                        fillAfter = true
                    }
                    detectedGhostView.startAnimation(fadeIn)

                    vibrate()
                    onResult(ghost)
                } else {
                    // Fallback to any ghost if filtered list is empty
                    val ghost = GhostData.ghosts.random()
                    onResult(ghost)
                }
            } else {
                onResult(null)
            }

            isSearching = false
        }
    }

    private fun vibrate() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
