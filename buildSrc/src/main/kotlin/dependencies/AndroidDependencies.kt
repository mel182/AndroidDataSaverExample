package dependencies

import Versions

interface AndroidDependencies {
    //const val androidKotlinCore = "androidx.core:core-ktx:1.9.0"
    val androidxAppCompat
        get() = "androidx.appcompat:appcompat:1.6.1"

    val androidxActivity
        get() = "androidx.activity:activity-ktx:1.6.1"

    val androidxFragment
        get() = "androidx.fragment:fragment-ktx:1.5.5"

    val androidxCardView
        get() = "androidx.cardview:cardview:1.0.0"

    val androidxDatastore
        get() = "androidx.datastore:datastore-core:1.0.0"

    val androidxRecyclerView
        get() = "androidx.recyclerview:recyclerview:1.2.1"

    val androidxLegacySupport
        get() = "androidx.legacy:legacy-support-v4:1.0.0"

    val androidxConstraintLayout // For motion layout use version 2.1.4 or newer
        get() = "androidx.constraintlayout:constraintlayout:2.1.4"

    val androidxCoordinatorLayout
        get() = "androidx.coordinatorlayout:coordinatorlayout:1.2.0"

    val androidArchLifeCycleExtensions
        get() = "android.arch.lifecycle:extensions:1.1.1"

    val androidDatabinding
        get() = "com.android.databinding:compiler:${Versions.dataBindingCompilerVersion}"

    val androidDatabindingMultiDex
        get() = "androidx.multidex:multidex:${Versions.multiDexVersion}"

    val androidLifecycleRuntimeKtx
        get() = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"

    val androidCoreSplashScreen
        get() = "androidx.core:core-splashscreen:1.0.0"

    // Work manager
    // Kotlin + coroutines
    val androidxWorkManagerRuntimeKtx
        get() = "androidx.work:work-runtime-ktx:${Versions.workVersion}"
    // optional - GCMNetworkManager support
    val androidxWorkManagerGCMSupport
        get() = "androidx.work:work-gcm:${Versions.workVersion}"
    // optional - Test helpers (test implementation)
    val androidxWorkTesting
        get() = "androidx.work:work-testing:${Versions.workVersion}"

    // Nav graph
    val androidxNavigationFragmentKtx
        get() = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    val androidxNavigationUIKtx
        get() = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"

    // Splash screen
    val splashScreen
        get() = "androidx.core:core-splashscreen:1.0.0"

    // Preference datastore
    val preferenceDatastore
        get() = "androidx.datastore:datastore-preferences:1.0.0"

    // Activity compose
    val activityCompose
        get() = "androidx.activity:activity-compose:1.6.1"

    // Compose Material Design
    val composeMaterialDesign
        get() = "androidx.compose.material:material:1.3.1"
    // Compose Animations
    val composeAnimation
        get() = "androidx.compose.animation:animation:1.3.3"
    val composeUI
        get() = "androidx.compose.ui:ui:1.7.0-alpha03"
    val composeFoundation
        get() = "androidx.compose.foundation:foundation:1.7.0-alpha03"
    val composeUIGraphics
        get() = "androidx.compose.ui:ui-graphics"
    // Tooling support (Previews, etc.)
    val composeUITooling
        get() = "androidx.compose.ui:ui-tooling:1.3.3"
    val composeToolingPreview
        get() = "androidx.compose.ui:ui-tooling-preview:1.3.3"
    // Integration with ViewModels
    val composeLifecycleViewmodel
        get() = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"
    val composeLifecycleRuntime
        get() = "androidx.lifecycle:lifecycle-runtime-compose:2.6.1"

    // When using a MDC theme
    val composeThemeAdapter
        get() = "com.google.android.material:compose-theme-adapter:1.2.1"
    // When using a AppCompat theme
    val composeAppCompatTheme
        get() = "com.google.accompanist:accompanist-appcompat-theme:0.25.1"
    // Compose tab layout
    val accompanistPager
        get() = "com.google.accompanist:accompanist-pager:0.25.1"
    val accompanistPagerIndicator
        get() = "com.google.accompanist:accompanist-pager-indicators:0.25.1"
    // Bottom navigation compose dependency
    val androidComposeNavigation
        get() = "androidx.navigation:navigation-compose:2.7.5"
    // Constraint layout compose
    val accompanistPermissions
        get() = "com.google.accompanist:accompanist-permissions:0.32.0"
    val constraintLayoutCompose
        get() = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
    val composeBOM
        get() = "androidx.compose:compose-bom:2024.02.01"
    val biometric
        get() = "androidx.biometric:biometric:1.1.0"

    // Compose runtime
    val composeRuntime
        get() = "androidx.compose.runtime:runtime:1.6.2"
    // Compose runtime live data
    val composeRuntimeLiveData
        get() = "androidx.compose.runtime:runtime-livedata:1.2.1"
    // Compose paging
    val composePaging
        get() = "androidx.paging:paging-compose:1.0.0-alpha16"
    val glanceAppWidget
        get() = "androidx.glance:glance-appwidget:1.0.0-beta01"
    val glance
        get() = "androidx.glance:glance:1.0.0-beta01"
    val localSearchAppSearch
        get() = "androidx.appsearch:appsearch:1.1.0-alpha03"
    val localSearchCompiler
        get() = "androidx.appsearch:appsearch-compiler:1.1.0-alpha03"
    val localSearchAppSearchLocalStorage
        get() = "androidx.appsearch:appsearch-local-storage:1.1.0-alpha03"
}

