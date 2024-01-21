import org.gradle.api.JavaVersion

object ProjectConfig {
    const val appID = "com.example.datasaverexampleapp"
    const val minSdk = 21
    const val compileSdk = 34
    const val targetSdk = 34
    const val versionCode = 1
    const val versionName = "1.0"
    const val composeVersion = "1.5.7"
    const val jvmTarget = "17"
    val javaVersion = JavaVersion.VERSION_17
}