# Add project specific ProGuard rules here.
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}

-keep class com.calangorun.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}