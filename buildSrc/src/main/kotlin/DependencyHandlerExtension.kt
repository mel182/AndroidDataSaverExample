import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependency:String) {
    add("implementation", dependency)
}

fun DependencyHandler.test(dependency:String) {
    add("test", dependency)
}

fun DependencyHandler.androidTest(dependency:String) {
    add("androidTest", dependency)
}

fun DependencyHandler.kapt(dependency:String) {
    add("implementation", dependency)
}

fun DependencyHandler.addPlatform(dependency:String) {
    add("implementation", platform(dependency))
}

fun DependencyHandler.testImplementation(dependency:String) {
    add("testImplementation", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency:String) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency:String) {
    add("debugImplementation", dependency)
}