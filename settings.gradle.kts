pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // 1. PANGLE SDK Repository (AdMob Adapter ke liye zaroori hai)
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }

        // 2. MINTEGRAL SDK Repository (MBridge core components ke liye LAZMI hai)
        maven { url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea") }
    }
}

rootProject.name = "Ghost Finder Radar Detector"
include(":app")
 