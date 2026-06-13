package com.calangorun.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Player {
    var x = 0f
    var y = 0f
    var width = 64f
    var height = 64f
    
    var velocityY = 0f
    var isJumping = false
    var isSliding = false
    var isInvincible = false
    
    private val gravity = 2500f // pixels/s²
    private val jumpVelocity = -900f
    private val groundY: Float
    private var slideTimer = 0L
    private val slideDuration = 800L // ms
    
    // Animation
    private var currentFrame = 0
    private var frameTimer = 0L
    private val frameDuration = 100L // ms per frame
    private var sprite: Bitmap? = null
    
    constructor(screenWidth: Int, screenHeight: Int) {
        groundY = screenHeight * 0.75f
        x = screenWidth * 0.2f
        y = groundY - height
    }
    
    fun update(deltaTime: Float) {
        // Update slide timer
        if (isSliding) {
            slideTimer += (deltaTime * 1000).toLong()
            if (slideTimer >= slideDuration) {
                isSliding = false
                height = 64f
                slideTimer = 0
            }
        }
        
        // Apply gravity
        if (isJumping || y < groundY - height) {
            velocityY += gravity * deltaTime
            y += velocityY * deltaTime
            
            // Check ground collision
            if (y >= groundY - height) {
                y = groundY - height
                velocityY = 0f
                isJumping = false
            }
        }
        
        // Update animation
        updateAnimation(deltaTime)
    }
    
    private fun updateAnimation(deltaTime: Float) {
        frameTimer += (deltaTime * 1000).toLong()
        if (frameTimer >= frameDuration) {
            currentFrame++
            frameTimer = 0
        }
    }
    
    fun jump() {
        if (!isJumping && !isSliding) {
            velocityY = jumpVelocity
            isJumping = true
        }
    }
    
    fun slide() {
        if (!isSliding && !isJumping) {
            isSliding = true
            height = 32f // Reduce height while sliding
            slideTimer = 0
        }
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
        // Draw player sprite
        // If we have a sprite bitmap, draw it
        // Otherwise draw a placeholder rectangle
        
        if (sprite != null) {
            val srcRect = Rect(0, 0, sprite!!.width, sprite!!.height)
            val dstRect = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
            canvas.drawBitmap(sprite!!, srcRect, dstRect, paint)
        } else {
            // Placeholder - draw colored rectangle
            paint.color = if (isInvincible) 0xFFFF00.toInt() else 0xFF66AA00.toInt()
            canvas.drawRect(x, y, x + width, y + height, paint)
        }
    }
    
    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }
    
    fun reset(screenWidth: Int, screenHeight: Int) {
        groundY = screenHeight * 0.75f
        x = screenWidth * 0.2f
        y = groundY - height
        velocityY = 0f
        isJumping = false
        isSliding = false
        isInvincible = false
    }
}
