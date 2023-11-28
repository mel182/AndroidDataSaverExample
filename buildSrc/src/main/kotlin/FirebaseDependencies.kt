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