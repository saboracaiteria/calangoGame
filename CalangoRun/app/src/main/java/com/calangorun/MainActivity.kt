package com.calangorun

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.calangorun.game.GameView
import com.calangorun.managers.AudioManager
import com.calangorun.managers.ScoreManager
import com.calangorun.managers.ShopManager

class MainActivity : AppCompatActivity() {
    
    private lateinit var gameView: GameView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize managers
        ScoreManager.initialize(this)
        ShopManager.initialize(this)
        AudioManager.initialize(this)
        
        // Create game view programmatically
        gameView = GameView(this, null)
        setContentView(gameView)
        
        // Enable fullscreen immersive mode
        enableFullscreen()
    }
    
    private fun enableFullscreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            )
        }
    }
    
    override fun onResume() {
        super.onResume()
        enableFullscreen()
    }
    
    override fun onPause() {
        super.onPause()
        // Pause game when activity is paused
        // gameView.pauseGame()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        AudioManager.release()
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableFullscreen()
        }
    }
}
