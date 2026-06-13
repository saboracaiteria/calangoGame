package com.calangorun.game

import android.content.Context
import android.view.MotionEvent
import com.calangorun.managers.GameManager

class InputHandler(private val context: Context) {

    private var touchStartX = 0f
    private var touchStartY = 0f
    private var touchStartTime = 0L

    fun handleTouchEvent(event: MotionEvent, gameView: GameView): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = event.x
                touchStartY = event.y
                touchStartTime = System.currentTimeMillis()
                return handleTouchDown(gameView)
            }
            MotionEvent.ACTION_UP -> {
                return handleTouchUp(event, gameView)
            }
            MotionEvent.ACTION_MOVE -> {
                // Could be used for drag controls
            }
        }
        return true
    }

    private fun handleTouchDown(gameView: GameView): Boolean {
        when (GameManager.gameState) {
            GameManager.GameState.MENU -> {
                GameManager.startGame()
            }
            GameManager.GameState.GAME_OVER -> {
                GameManager.startGame()
            }
            GameManager.GameState.PLAYING -> {
                // Jump on tap (will be refined with swipe detection)
            }
            else -> { }
        }
        return true
    }

    private fun handleTouchUp(event: MotionEvent, gameView: GameView): Boolean {
        val touchEndX = event.x
        val touchEndY = event.y
        val touchDuration = System.currentTimeMillis() - touchStartTime

        val dx = touchEndX - touchStartX
        val dy = touchEndY - touchStartY

        // Detect swipe
        when (GameManager.gameState) {
            GameManager.GameState.PLAYING -> {
                when {
                    // Swipe down - slide
                    dy > 100 && kotlin.math.abs(dy) > kotlin.math.abs(dx) -> {
                        // Player.slide()
                    }
                    // Swipe up - jump
                    dy < -100 && kotlin.math.abs(dy) > kotlin.math.abs(dx) -> {
                        // Player.jump()
                    }
                    // Tap - jump (if no significant swipe)
                    touchDuration < 200 && kotlin.math.abs(dx) < 50 && kotlin.math.abs(dy) < 50 -> {
                        // Player.jump()
                    }
                }
            }
            else -> { }
        }
        return true
    }
}
