# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



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