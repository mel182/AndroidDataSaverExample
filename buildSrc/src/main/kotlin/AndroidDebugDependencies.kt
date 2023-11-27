interface AndroidDebugDependencies {

    val composeUiToolingDebug
        get() = "androidx.compose.ui:ui-tooling:${Versions.composeUIVersion}"

    val composeUiTestManifestDebug
        get() = "androidx.compose.ui:ui-test-manifest:${Versions.composeUIVersion}"
}