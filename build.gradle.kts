// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    extra.apply {
        set("compiler_version","1.4.3")
        set("lifecycle_version","2.4.1")
        set("room_version","2.5.0")
        set("compose_version","1.4.5")
        set("compose_ui_version","1.4.0-rc01")
        set("wear_compose_version","1.1.2")
    }

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("com.google.gms:google-services:4.3.15")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.google.com")
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
