plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.datasaverexampleapp"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.example.datasaverexampleapp"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.composeVersion
    }

    buildFeatures {
        dataBinding = true
        compose = true
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configurations { implementation.get().exclude(mapOf("group" to "org.jetbrains", "module" to "annotations")) }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    androidXDependenciesExtended()
    androidMaterial()
    kotlinXCorountines()
    googlePlay()
    firebase()
    room()
    retrofit()
    lifeCycleLiveDataKtx()
    exoPlayer()
    routerAndMediaCasting()
    androidSwipeToRefresh()
    facebookShimmer()
    androidPaging()
    pierFrancesCosoffritiYouTubePlayer()
    androidTests()
}
