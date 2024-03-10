// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("io.realm.kotlin") version "1.11.0" apply false
}


buildscript {

    extra.apply {
        set("compiler_version","1.4.3")
        set("lifecycle_version","2.4.1")
        set("room_version","2.5.0")
        set("compose_version","1.5.7")
        set("compose_ui_version","1.4.0-rc01")
        set("wear_compose_version","1.1.2")
        set("work_version","2.8.0")
        set("nav_version","2.4.1")
        set("exo_player_version","2.18.0")
        set("paging_version","3.1.0")
        set("multidex_version","2.0.1")
    }

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
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
