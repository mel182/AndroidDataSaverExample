import dependencies.AndroidDebugDependencies
import dependencies.AndroidDependencies
import dependencies.AndroidTestDependencies
import dependencies.AndroidWearDependencies
import extensions.addPlatform
import extensions.androidTestImplementation
import extensions.androidTestPlatform
import extensions.coreLibraryDesugaring
import extensions.debugImplementation
import extensions.kapt
import extensions.testImplementation
import extensions.implementation
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies: AndroidDependencies, GoogleMaterialDependencies, KotlinXDependencies, GooglePlayDependencies, FirebaseDependencies,
    AndroidTestDependencies, AndroidDebugDependencies, DesugaringDependencies, GoogleDependencies,
    AndroidWearDependencies {

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

    // Coil compose io
    const val composeCoilKt = "io.coil-kt:coil-compose:2.2.2"
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

fun DependencyHandler.kotlinVersion() {
    implementation(Dependencies.kotlinVersion)
}

fun DependencyHandler.androidComposeProjectDefaultDependencies() {
    androidXDependenciesDefault()
    androidMaterial()
    androidXLifecycleRuntime()
    compose()
}

fun DependencyHandler.androidComposeMaterial3DefaultDependencies() {
    androidXDependenciesDefault()
    androidMaterial3()
    androidXLifecycleRuntime()
    compose()
}

fun DependencyHandler.androidXDatabinding() {
    kapt(Dependencies.androidDatabinding)
}

fun DependencyHandler.androidXLifecycleRuntime() {
    kapt(Dependencies.androidLifecycleRuntimeKtx)
}

fun DependencyHandler.androidXLifecycleRuntimeImplementation() {
    kapt(Dependencies.androidLifecycleRuntimeKtx)
}

fun DependencyHandler.androidComposeConstraintLayout() {
    implementation(Dependencies.constraintLayoutCompose)
}

fun DependencyHandler.composeBOM() {
    addPlatform(Dependencies.composeBOM)
    androidTestPlatform(Dependencies.composeBOM)
}

fun DependencyHandler.kotlinBOM() {
    addPlatform(Dependencies.kotlinBOM)
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

fun DependencyHandler.compose() {
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.composeUI)
    implementation(Dependencies.composeUIGraphics)
    implementation(Dependencies.composeUITooling)
    implementation(Dependencies.composeToolingPreview)
    implementation(Dependencies.composeLifecycleViewmodel)
    debugImplementation(Dependencies.composeUiToolingDebug)
    debugImplementation(Dependencies.composeUiTestManifestDebug)
}

fun DependencyHandler.composeViewModel() {
    implementation(Dependencies.composeLifecycleViewmodel)
    implementation(Dependencies.composeLifecycleRuntime)
}

fun DependencyHandler.composeNavigation() {
    implementation(Dependencies.androidComposeNavigation)
}

fun DependencyHandler.composeThemeAdapter() {
    implementation(Dependencies.composeThemeAdapter)
}

fun DependencyHandler.composeRuntime() {
    implementation(Dependencies.composeRuntime)
}

fun DependencyHandler.composeRuntimeLivedata() {
    implementation(Dependencies.composeRuntimeLiveData)
}

fun DependencyHandler.composePaging() {
    implementation(Dependencies.composePaging)
}

fun DependencyHandler.androidMaterial() {
    implementation(Dependencies.googleMaterial)
}

fun DependencyHandler.androidMaterial3() {
    implementation(Dependencies.googleMaterial3)
}

fun DependencyHandler.coilKt() {
    implementation(Dependencies.composeCoilKt)
}

fun DependencyHandler.androidPaging() {
    implementation(Dependencies.androidListPagingLibrary)
}

fun DependencyHandler.androidToolsDesugarJdkLibs() {
    coreLibraryDesugaring(Dependencies.androidTools_JDK_DesugaringLibs)
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

fun DependencyHandler.androidCoreSplashScreen() {
    implementation(Dependencies.androidCoreSplashScreen)
}

fun DependencyHandler.kotlinXCorountines() {
    implementation(Dependencies.kotlinXCoroutine)
}

fun DependencyHandler.googlePlay() {
    implementation(Dependencies.googlePlayAwareness)
    implementation(Dependencies.googlePlayMaps)
    implementation(Dependencies.googlePlayLocation)
}

fun DependencyHandler.googlePlayUpdate() {
    implementation(Dependencies.googlePlayAppUpdateKtx)
    implementation(Dependencies.googlePlayAppUpdate)
}

fun DependencyHandler.googlePlayAuth() {
    implementation(Dependencies.googlePlayServicesAuth)
}

fun DependencyHandler.googleAppCompatTheme() {
    implementation(Dependencies.googleAppCompatTheme)
}

fun DependencyHandler.googleAccompanistPager() {
    implementation(Dependencies.accompanistPager)
}

fun DependencyHandler.googleSwipeRefresh() {
    implementation(Dependencies.googleSwipeRefresh)
}

fun DependencyHandler.googleAccompanistPagerIndicator() {
    implementation(Dependencies.accompanistPagerIndicator)
}

fun DependencyHandler.googleMaterialExtendedIcons() {
    implementation(Dependencies.googleMaterialExtendedIcons)
}

fun DependencyHandler.wearOSComposeMaterial() {
    implementation(Dependencies.wearComposeMaterial)
}

fun DependencyHandler.wearOSFoundation() {
    implementation(Dependencies.wearFoundation)
}

fun DependencyHandler.googleServiceWearable() {
    implementation(Dependencies.googleServiceWearable)
}

fun DependencyHandler.androidWearableComposeUI() {
    implementation(Dependencies.androidWearableComposeUI)
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

fun DependencyHandler.firebaseAuthKtx() {
    implementation(Dependencies.firebaseAuthKtx)
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

fun DependencyHandler.glanceAppWidget() {
    implementation(Dependencies.glanceAppWidget)
}

fun DependencyHandler.glance() {
    implementation(Dependencies.glance)
}

fun DependencyHandler.glanceLibrary() {
    glanceAppWidget()
    glance()
}

interface KotlinXDependencies {
    val kotlinXCoroutine
        get() = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    val kotlinBOM
        get() = "org.jetbrains.kotlin:kotlin-bom:1.8.0"
}

interface GooglePlayDependencies {

    val googlePlayAwareness
        get() = "com.google.android.gms:play-services-awareness:19.0.1"
    val googlePlayMaps
        get() = "com.google.android.gms:play-services-maps:18.0.2"
    // The specific version number specified in your dependency node must correspond to the version
    // of the Google Play services SDK you have downloaded and installed. Similarly, when you install
    // newer versions of the SDK, you must update the dependency node accordingly
    val googlePlayLocation
        get() = "com.google.android.gms:play-services-location:19.0.1"
    val googlePlayServicesAuth
        get() = "com.google.android.gms:play-services-auth:20.5.0"
    val googlePlayAppUpdateKtx
        get() = "com.google.android.play:app-update-ktx:2.0.1"
    val googlePlayAppUpdate
        get() = "com.google.android.play:app-update:2.0.1"
}

interface GoogleMaterialDependencies {
    val googleMaterial
        get() = "androidx.compose.material:material:1.3.1"

    val googleMaterial3
        get() = "androidx.compose.material3:material3"

    val googleMaterialExtendedIcons
        get() = "androidx.compose.material:material-icons-extended-android:1.5.0"

}

interface GoogleDependencies {
    val googleAppCompatTheme
        get() = "com.google.accompanist:accompanist-appcompat-theme:0.25.1"
    val googleSwipeRefresh
        get() = "com.google.accompanist:accompanist-swiperefresh:0.26.3-beta"
}

interface DesugaringDependencies {
    val androidTools_JDK_DesugaringLibs
        get() = "com.android.tools:desugar_jdk_libs:2.0.3"
}

interface FirebaseDependencies {

    val firebaseDatabase
        get() = "com.google.firebase:firebase-database:20.1.0"
    val firebaseMessagingKtx
        get() = "com.google.firebase:firebase-messaging-ktx"
    val firebaseAuth
        get() = "com.google.firebase:firebase-auth:21.0.1"
    val firebaseAuthKtx
        get() = "com.google.firebase:firebase-auth-ktx:21.2.0"
    // Import the BoM for the Firebase platform
    val firebaseBOM
        get() = "com.google.firebase:firebase-bom:26.3.0"

    val firebaseBOM_26_1_1
        get() = "com.google.firebase:firebase-bom:26.1.1"
    val firebaseDbKtx
        get() = "com.google.firebase:firebase-database-ktx"

    // Declare the dependencies for the Firebase Cloud Messaging and Analytics libraries
    // When using the BoM, you don"t specify versions in Firebase library dependencies
    // The specific version number specified in your dependency node must correspond to the version
    // of the Google Play services SDK you have downloaded and installed. Similarly, when you install
    // newer versions of the SDK, you must update the dependency node accordingly
    val firebaseMessaging
        get() = "com.google.firebase:firebase-messaging"
    val firebaseAnalytics
        get() = "com.google.firebase:firebase-analytics"

}