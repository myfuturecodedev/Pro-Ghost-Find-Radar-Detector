plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.futurecode.ghostfinderradardetector"
    compileSdk = 35

    defaultConfig {
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
        applicationId = "com.futurecode.ghostfinderradardetector"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            // 16 KB page size support for debug APKs
            packaging {
                jniLibs {
                    useLegacyPackaging = false
                }
            }
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // 16 KB page size support for release APKs/Bundles
            packaging {
                jniLibs {
                    useLegacyPackaging = false
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kotlin { jvmToolchain(17) }

dependencies {
    constraints {
        implementation("com.google.android.gms:play-services-ads:24.0.0") {
            because("Prevents mediation adapters from pulling in broken transitive bytecode packages")
        }
    }

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics)

    // Core AdMob dependency library - Using latest stable 24.0.0
    implementation("com.google.android.gms:play-services-ads:24.0.0")

    // Ads SDKs - Updated to latest versions for Android 15 (16KB support)
    implementation("com.facebook.android:audience-network-sdk:6.21.0")
    implementation("com.google.ads.mediation:facebook:6.21.0.3")

    // 2. Mintegral Bidding Adapter - Updated for Android 15 compatibility
    implementation("com.google.ads.mediation:mintegral:17.1.61.1")

    // 3. Pangle Bidding Adapter - Updated to latest stable for 16KB alignment fixes
    implementation("com.google.ads.mediation:pangle:8.1.0.4.0")

    // 4. Liftoff/Vungle Bidding Adapter - Updated
    implementation("com.google.ads.mediation:vungle:7.7.4.2")

    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation("com.android.billingclient:billing-ktx:8.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    val camera_version = "1.4.0"
    implementation("androidx.camera:camera-core:${camera_version}")
    implementation("androidx.camera:camera-camera2:${camera_version}")
    implementation("androidx.camera:camera-lifecycle:${camera_version}")
    implementation("androidx.camera:camera-view:${camera_version}")

    implementation("com.google.guava:guava:31.1-android")
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // firebase analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))
}
