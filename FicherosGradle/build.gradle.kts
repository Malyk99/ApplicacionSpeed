// build.gradle (Nivel de Proyecto)

buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.4.0")
        classpath ("com.android.tools.build:gradle:8.6.0")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}

plugins {
    id("com.android.application") version "8.6.0" apply false
    id("com.android.library") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
