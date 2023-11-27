import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies: AndroidDependencies, GoogleMaterialDependencies, KotlinXDependencies, GooglePlayDependencies, FirebaseDependencies, AndroidTestDependencies {

    // Kotlin verion
    const val kotlinVersion = "androidx.core:core-ktx:1.9.0"

    // Swipe to refresh
    const val swipeToRefreshAccompanist = "com.google.accompanist:accompanist-swiperefresh:0.26.3-beta"

    // router and media casting
    const val mediaRouter = "androidx.mediarouter:mediarouter:1.2.6"
    const val mediaCasting = "com.google.android.gms:play-services-cast-framework:21.0.1"

    // List paging
    const val androidListPagingLibrary = "androidx.paging:paging-runtime:${Versions.pagingLibraryVersion}"

    //Room database
    const val roomDatabasePersistenceCompiler = "android.arch.persistence.room:compiler:1.1.1"
    const val roomArchPersistenceRoomRuntime = "android.arch.persistence.room:runtime:1.1.1"
    const val roomDatabaseRuntime = "androidx.room:room-runtime:${Versions.roomDBVersion}"
    const val roomDatabaseCompiler = "androidx.room:room-compiler:${Versions.roomDBVersion}"
    const val roomDatabaseTest = "android.arch.persistence.room:testing:1.1.1"

    //Retrofit
    const val retrofit2 = "com.squareup.retrofit2:retrofit:2.9.0"
    const val retrofit2GsonConverter = "com.squareup.retrofit2:converter-gson:2.6.0"
    const val retrofit2LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.5.0"

    //lifecycleVersion
    const val lifeCycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"

    //Exo player
    const val exoPlayerCore = "com.google.android.exoplayer:exoplayer-core:${Versions.exoPlayerVersion}"
    const val exoPlayerUI = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoPlayerVersion}"

    // Facebook shimmer
    const val facebookShimmer = "com.facebook.shimmer:shimmer:0.5.0"

    // Pier frances cosoffritti android youtube player
    const val pierFrancesCosoffrittiYouTubePlayer = "com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0"
}

fun DependencyHandler.room() {
    kapt(Dependencies.roomDatabasePersistenceCompiler)
    implementation(Dependencies.roomArchPersistenceRoomRuntime)
    implementation(Dependencies.roomDatabaseRuntime)
    kapt(Dependencies.roomDatabaseCompiler)
    androidTestImplementation(Dependencies.roomDatabaseTest)
}

fun DependencyHandler.androidXDependenciesDefault() {
    implementation(Dependencies.kotlinVersion)
    implementation(Dependencies.androidxAppCompat)
    implementation(Dependencies.googleMaterial)
    implementation(Dependencies.androidxConstraintLayout)
    implementation(Dependencies.kotlinXCoroutine)
    androidTests()
}

fun DependencyHandler.androidXDatabinding() {
    kapt(Dependencies.androidDatabinding)
}

fun DependencyHandler.androidXDependenciesExtended() {
    implementation(Dependencies.kotlinVersion)
    implementation(Dependencies.androidxAppCompat)
    implementation(Dependencies.androidxActivity)
    implementation(Dependencies.androidxFragment)
    implementation(Dependencies.androidxCardView)
    implementation(Dependencies.androidxDatastore)
    implementation(Dependencies.androidxRecyclerView)
    implementation(Dependencies.androidxLegacySupport)
    implementation(Dependencies.androidxConstraintLayout)
    implementation(Dependencies.androidxCoordinatorLayout)
    implementation(Dependencies.androidArchLifeCycleExtensions)
    androidXDatabinding()
    implementation(Dependencies.androidDatabindingMultiDex)
    implementation(Dependencies.androidxWorkManagerRuntimeKtx)
    implementation(Dependencies.androidxWorkManagerGCMSupport)
    //androidTestImplementation(Dependencies.androidxWorkTesting)
    implementation(Dependencies.androidxNavigationFragmentKtx)
    implementation(Dependencies.androidxNavigationUIKtx)
    implementation(Dependencies.splashScreen)
    implementation(Dependencies.preferenceDatastore)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.composeMaterialDesign)
    implementation(Dependencies.composeAnimation)
    implementation(Dependencies.composeUITooling)
    implementation(Dependencies.composeToolingPreview)
    implementation(Dependencies.composeLifecycleViewmodel)
    implementation(Dependencies.composeThemeAdapter)
    implementation(Dependencies.composeAppCompatTheme)
    implementation(Dependencies.accompanistPager)
    implementation(Dependencies.accompanistPagerIndicator)
    implementation(Dependencies.androidComposeNavigation)
    implementation(Dependencies.constraintLayoutCompose)
    implementation(Dependencies.composeRuntime)
    implementation(Dependencies.composeRuntimeLiveData)
    implementation(Dependencies.composePaging)
}

fun DependencyHandler.androidMaterial() {
    implementation(Dependencies.googleMaterial)
}

fun DependencyHandler.androidPaging() {
    implementation(Dependencies.androidListPagingLibrary)
}

fun DependencyHandler.androidSwipeToRefresh() {
    implementation(Dependencies.swipeToRefreshAccompanist)
}

fun DependencyHandler.androidTests() {
    testImplementation(Dependencies.jUnit)
    androidTestImplementation(Dependencies.jUnitExtension)
    androidTestImplementation(Dependencies.jUnitEspresso)

    // UI test
    androidTestImplementation(Dependencies.uiJUnitTesting)
    androidTestImplementation(Dependencies.debugComposeUiTooling)
}

fun DependencyHandler.kotlinXCorountines() {
    implementation(Dependencies.kotlinXCoroutine)
}

fun DependencyHandler.googlePlay() {
    implementation(Dependencies.googlePlayAwareness)
    implementation(Dependencies.googlePlayMaps)
    implementation(Dependencies.googlePlayLocation)
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.retrofit2)
    implementation(Dependencies.retrofit2GsonConverter)
    implementation(Dependencies.retrofit2LoggingInterceptor)
}

fun DependencyHandler.firebase() {
    implementation(Dependencies.firebaseDatabase)
    implementation(Dependencies.firebaseMessagingKtx)
    implementation(Dependencies.firebaseAuth)
    addPlatform(Dependencies.firebaseBOM)
    //addPlatform(Dependencies.firebaseBOM_26_1_1)
    implementation(Dependencies.firebaseDbKtx)
    implementation(Dependencies.firebaseMessaging)
    implementation(Dependencies.firebaseAnalytics)
}

fun DependencyHandler.lifeCycleLiveDataKtx() {
    implementation(Dependencies.lifeCycleLiveDataKtx)
}

fun DependencyHandler.exoPlayer() {
    implementation(Dependencies.exoPlayerCore)
    implementation(Dependencies.exoPlayerUI)
}

fun DependencyHandler.routerAndMediaCasting() {
    implementation(Dependencies.mediaRouter)
    implementation(Dependencies.mediaCasting)
}

fun DependencyHandler.facebookShimmer() {
    implementation(Dependencies.facebookShimmer)
}

fun DependencyHandler.pierFrancesCosoffritiYouTubePlayer() {
    implementation(Dependencies.pierFrancesCosoffrittiYouTubePlayer)
}