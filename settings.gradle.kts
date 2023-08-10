pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlinx/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlinx/")
    }
}

rootProject.name = "MyKMApplication"
include(":androidApp")
include(":shared")