interface AndroidWearDependencies {

    val wearComposeMaterial
        get() = "androidx.wear.compose:compose-material:${Versions.wearOSComposeVersion}"
    val wearFoundation
        get() = "androidx.wear.compose:compose-foundation:${Versions.wearOSComposeVersion}"
    val googleServiceWearable
        get() = "com.google.android.gms:play-services-wearable:18.0.0"
    val androidWearableComposeUI
        get() = "androidx.compose.ui:ui:${Versions.composeUIVersion}"
}