package com.calangorun.managers

import android.graphics.Canvas
import android.graphics.Paint

object GameManager {
    enum class GameState {
        MENU, PLAYING, PAUSED, GAME_OVER, SHOP
    }

    var gameState: GameState = GameState.MENU
    var score: Int = 0
    var highScore: Int = 0
    var coins: Int = 0
    var level: Int = 1
    var gameSpeed: Float = 5f

    fun startGame() {
        gameState = GameState.PLAYING
        score = 0
        gameSpeed = 5f
        // Reset entities
    }

    fun gameOver() {
        gameState = GameState.GAME_OVER
        if (score > highScore) {
            highScore = score
        }
        // Save to SharedPreferences
    }

    fun pause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED
        }
    }

    fun resume() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING
        }
    }

    fun openShop() {
        gameState = GameState.SHOP
    }

    fun closeShop() {
        gameState = GameState.MENU
    }

    fun update() {
        when (gameState) {
            GameState.PLAYING -> {
                // Increment score
                score += 1
                
                // Increase difficulty
                if (score % 500 == 0) {
                    gameSpeed += 0.5f
                    level++
                }
            }
            else -> { }
        }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // Draw game entities
        // This will delegate to entity managers
    }
}
