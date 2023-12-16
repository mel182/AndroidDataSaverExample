plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.example.wearosexample"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.example.wearosexample"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        val compose_version = rootProject.extra["compose_version"]
        kotlinCompilerExtensionVersion = "$compose_version"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    androidMaterial()
    compose()
    composeViewModel()
    androidXLifecycleRuntimeImplementation()
    androidWearableComposeUI()
    wearOSComposeMaterial()
    wearOSFoundation()
    googleServiceWearable()
    googleMaterialExtendedIcons()
    androidToolsDesugarJdkLibs()

}