plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.datasaverexampleapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.datasaverexampleapp"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    composeOptions {
        val compose_version = rootProject.extra["compose_version"]
        kotlinCompilerExtensionVersion = "$compose_version"
    }

    buildFeatures {
        dataBinding = true
        compose = true
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))  //fileTree(dir: "libs", include: ["*.jar"])
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.5")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("android.arch.lifecycle:extensions:1.1.1")
    implementation("android.arch.persistence.room:runtime:1.1.1")
    implementation("com.google.firebase:firebase-database:20.1.0")

    implementation(platform("com.google.firebase:firebase-bom:26.3.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // Adding Google Play services as app dependencies
    implementation("com.google.android.gms:play-services-awareness:19.0.1")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-location:19.0.1")
    implementation("androidx.datastore:datastore-core:1.0.0")

    val room_version = rootProject.extra["room_version"]
    kapt("android.arch.persistence.room:compiler:1.1.1")
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    androidTestImplementation("android.arch.persistence.room:testing:1.1.1")

    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")

    val lifecycle_version = rootProject.extra["lifecycle_version"]
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation(platform("com.google.firebase:firebase-bom:26.1.1"))
    implementation("com.google.firebase:firebase-database-ktx")

    val compiler_version = rootProject.extra["compiler_version"]
    kapt("com.android.databinding:compiler:$compiler_version")

    val multidex_version = rootProject.extra["multidex_version"]
    implementation("androidx.multidex:multidex:$multidex_version")

    // ---- work manager dependency ---- \\
    val work_version = rootProject.extra["work_version"]
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:$work_version")
    // optional - GCMNetworkManager support
    implementation("androidx.work:work-gcm:$work_version")
    // optional - Test helpers
//    androidTestImplementation "androidx.work:work-testing:$work_version"
    // ---- work manager dependency ---- \\

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:26.3.0"))

    // Declare the dependencies for the Firebase Cloud Messaging and Analytics libraries
    // When using the BoM, you don"t specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")

    // Nav graph dependencies
    val nav_version = rootProject.extra["nav_version"]
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // The specific version number specified in your dependency node must correspond to the version
    // of the Google Play services SDK you have downloaded and installed. Similarly, when you install
    // newer versions of the SDK, you must update the dependency node accordingly
    implementation("com.google.android.gms:play-services-location:19.0.1")

    // ExoPlayer dependencies
    val exo_player_version = rootProject.extra["exo_player_version"]
    implementation("com.google.android.exoplayer:exoplayer-core:$exo_player_version")
    implementation("com.google.android.exoplayer:exoplayer-ui:$exo_player_version")

    // router and media casting
    implementation("androidx.mediarouter:mediarouter:1.2.6")
    implementation("com.google.android.gms:play-services-cast-framework:21.0.1")

    val paging_version = rootProject.extra["paging_version"]
    implementation("androidx.paging:paging-runtime:$paging_version")

    // facebook shimmer 0.5.0
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // splash screen dependency
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Preference datastore dependency
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Motion layout dependency
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // -------  Compose dependencies --------

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")
    // Compose Material Design
    implementation("androidx.compose.material:material:1.3.1")
    // Animations
    implementation("androidx.compose.animation:animation:1.3.3")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.3.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    // When using a MDC theme
    implementation("com.google.android.material:compose-theme-adapter:1.2.1")
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

    // Youtube player dependency
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.1")

}