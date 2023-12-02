package dependencies

interface AndroidTestDependencies {

    // JUnit test dependencies
    val jUnit
        get() = "junit:junit:4.13.2"
    val jUnitExtension
        get() = "androidx.test.ext:junit:1.1.3"
    val jUnitEspresso
        get() = "androidx.test.espresso:espresso-core:3.4.0"

    // UI Tests
    val uiJUnitTesting
        get() = "androidx.compose.ui:ui-test-junit4:1.2.1"
    val debugComposeUiTooling
        get() = "androidx.compose.ui:ui-tooling:1.2.1"
}