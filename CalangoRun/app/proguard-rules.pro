# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt

# Keep game entities
-keep class com.calangorun.entities.** { *; }

# Keep managers
-keep class com.calangorun.managers.** { *; }

# Keep rendering classes
-keep class com.calangorun.rendering.** { *; }

# Keep game classes
-keep class com.calangorun.game.** { *; }

# Standard Android optimizations
-dontoptimize
-dontobfuscate
