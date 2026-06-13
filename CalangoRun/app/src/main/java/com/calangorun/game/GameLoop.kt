package com.calangorun.game

import android.graphics.Canvas

class GameLoop(private val gameView: GameView) : Thread() {
    @Volatile var running = false
    
    private val targetFPS = 60
    private val frameDuration = 1000 / targetFPS // 16ms per frame
    
    override fun run() {
        var frameStartTime: Long
        var frameTime: Long
        
        running = true
        
        while (running) {
            frameStartTime = System.currentTimeMillis()
            
            val canvas: Canvas? = gameView.holder.lockCanvas()
            
            if (canvas != null) {
                try {
                    // Update game logic
                    gameView.update()
                    
                    // Render
                    gameView.draw(canvas)
                } finally {
                    gameView.holder.unlockCanvasAndPost(canvas)
                }
            }
            
            // Control FPS
            frameTime = System.currentTimeMillis() - frameStartTime
            val sleepTime = frameDuration - frameTime
            
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime)
                } catch (e: InterruptedException) {
                    // Ignore
                }
            }
        }
    }
}
