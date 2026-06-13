package com.calangorun.rendering

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class Animation(
    private val frames: List<Bitmap>,
    private val frameDuration: Long
) {
    private var currentFrame = 0
    private var frameTimer = 0L
    
    fun update(deltaTime: Long) {
        frameTimer += deltaTime
        if (frameTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.size
            frameTimer = 0
        }
    }
    
    fun getCurrentFrame(): Bitmap {
        return if (frames.isNotEmpty()) frames[currentFrame] else frames[0]
    }
    
    fun reset() {
        currentFrame = 0
        frameTimer = 0
    }
}
