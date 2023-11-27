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
}