plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {

    namespace = "com.futurecode.ghostfinderradardetector"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.futurecode.ghostfinderradardetector"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        // ADD THIS BLOCK START
        externalNativeBuild {
            cmake {
                // Forces the linker to use 16KB page alignment for native libraries
                arguments("-DANDROID_EXT_MEM_ALIGNMENT=16384")
            }
        }
        // ADD THIS BLOCK END
    }

    buildTypes {

        debug {
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // FIX: Added for 16 KB page size support (November 1st, 2025 requirement)
    packaging {
        jniLibs {
            // This is required so the OS reads the .so file directly from the APK
            useLegacyPackaging = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //facebook ads
    implementation("com.facebook.android:audience-network-sdk:6.21.0")

    //google ads
//    implementation("com.google.android.gms:play-services-ads:25.2.0")
    implementation("com.google.android.gms:play-services-ads:23.6.0") // Check for the latest 2026 version
    implementation("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    
    // Lifecycle - Aligned to stable 2.8.7 to prevent compiler crashes
    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    //google billing
    implementation("com.android.billingclient:billing-ktx:8.3.0")

    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Material components
    implementation("com.google.android.material:material:1.12.0")

    // Lottie dependency
    implementation("com.airbnb.android:lottie:6.4.1")

    // glide - Downgraded to stable 4.x
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // CameraX
//    val camera_version = "1.3.4"
    val camera_version = "1.4.0" // Updated from 1.3.4
    implementation("androidx.camera:camera-core:${camera_version}")
    implementation("androidx.camera:camera-camera2:${camera_version}")
    implementation("androidx.camera:camera-lifecycle:${camera_version}")
    implementation("androidx.camera:camera-view:${camera_version}")
    
    // Essential for CameraX and K2 compiler stability
   // implementation("com.google.guava:listenablefuture:1.0")

    implementation("com.google.guava:guava:31.1-android")

    //lottie
    implementation("com.airbnb.android:lottie:6.7.1")



}
