package com.calangorun.calangorunwebview

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Enable fullscreen immersive mode
        enableImmersiveMode()

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings: WebSettings = webView.settings

        // Essential settings for HTML5 games
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true

        // Cache settings for offline play
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        // Viewport and scaling
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = false

        // Hardware acceleration is enabled at the application level in AndroidManifest.xml

        // Media settings
        webSettings.mediaPlaybackRequiresUserGesture = false

        // Allow mixed content (for CDN resources)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // JavaScript interfaces for native-web communication
        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        // Custom WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }

        // Custom ChromeClient for fullscreen video support
        webView.webChromeClient = object : WebChromeClient() {
            private var customView: View? = null
            private var customViewCallback: CustomViewCallback? = null
            private val fullscreenContainer = object : FrameLayout(this@MainActivity) {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    super.onConfigurationChanged(newConfig)
                }
            }

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                if (customView != null) {
                    onHideCustomView()
                    return
                }
                customView = view
                customViewCallback = callback
                fullscreenContainer.addView(view)
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                (window.decorView as android.view.ViewGroup).addView(fullscreenContainer)
                fullscreenContainer.visibility = View.VISIBLE
            }

            override fun onHideCustomView() {
                fullscreenContainer.visibility = View.GONE
                fullscreenContainer.removeAllViews()
                if (customView != null) {
                    customView = null
                    customViewCallback?.onCustomViewHidden()
                }
            }

            override fun getVideoLoadingProgressView(): View {
                return progressBar
            }
        }

        // Show loading indicator
        progressBar.visibility = View.VISIBLE

        // Load the game from assets
        webView.loadUrl("file:///android_asset/index.html")
    }

    private fun enableImmersiveMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller?.let {
            it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle orientation changes without restarting activity
    }

    override fun onResume() {
        super.onResume()
        enableImmersiveMode()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Let WebView handle back button (for game back functionality)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}

/**
 * JavaScript interface for native-web communication
 */
class WebAppInterface(private val activity: MainActivity) {

    @android.webkit.JavascriptInterface
    fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    @android.webkit.JavascriptInterface
    fun getDeviceInfo(): String {
        return "${Build.MODEL}|${Build.VERSION.SDK_INT}"
    }

    @android.webkit.JavascriptInterface
    fun isNativeApp(): Boolean {
        return true
    }
}
