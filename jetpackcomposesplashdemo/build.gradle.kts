plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.splashdemo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.splashdemo"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        val compose_version = rootProject.extra["compose_version"]
        kotlinCompilerExtensionVersion = "$compose_version"
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    val compose_version_splash = "1.2.1"

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.compose.ui:ui:$compose_version_splash")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version_splash")
    implementation("androidx.compose.material:material:1.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version_splash")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version_splash")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version_splash")

    // -------  Compose dependencies --------

    // splash screen dependency
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.5.1")
    // Compose Material Design
    implementation("androidx.compose.material:material:1.2.1")
    // Animations
    implementation("androidx.compose.animation:animation:1.2.1")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    // When using a MDC theme
    implementation("com.google.android.material:compose-theme-adapter:1.1.16")
    // When using a AppCompat theme
    implementation("com.google.accompanist:accompanist-appcompat-theme:0.25.1")
    // For compose tab layout
    implementation("com.google.accompanist:accompanist-pager:0.25.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.25.1")
    // Bottom navigation compose dependency
    implementation("androidx.navigation:navigation-compose:2.5.1")
    // Constraint layout compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // Compose runtime
    implementation("androidx.compose.runtime:runtime:1.2.1")
    // Compose runtime live data
    implementation("androidx.compose.runtime:runtime-livedata:1.2.1")

    implementation("androidx.paging:paging-compose:1.0.0-alpha16")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.26.3-beta")
}