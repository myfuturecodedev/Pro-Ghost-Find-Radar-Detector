# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Uncomment this to preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Navigation Safe Args
-keep class **Args { *; }
-keep class **Directions { *; }

# AndroidX Fragment / Activity result APIs
-keepclassmembers class * extends androidx.fragment.app.Fragment {
    public <init>(...);
}
-keepclassmembers class * extends androidx.appcompat.app.AppCompatActivity {
    public <init>(...);
}

# Parcelize
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Facebook Audience Network
-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe

# Keep all CameraX implementation classes (where CameraConfig lives)
-keep class androidx.camera.core.impl.** { *; }

# Specifically keep all Config and Option classes to prevent reflection errors
-keep class * implements androidx.camera.core.impl.Config { *; }
-keep class * extends androidx.camera.core.impl.Config$Option { *; }

# Keep the Camera2 implementation specifically
-keep class androidx.camera.camera2.impl.** { *; }
-keepnames class com.futurecode.ghostfinderradardetector.model.GhostModel

# 🛑 CRITICAL SHIELD TO PREVENT ADMOB VERIFYERROR CRASH
-keep class com.google.android.gms.internal.ads.** { *; }
-dontwarn com.google.android.gms.internal.ads.**


# Gson specific rules to safeguard reflection sweeps
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Explicitly keep your notification data structure package safe
-keep class com.futurecode.ghostfinderradardetector.notification.NotificationModel { *; }