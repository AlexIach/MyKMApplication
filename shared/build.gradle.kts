/**
 * List of plugins to use
 */
plugins {
    kotlin("multiplatform") // Multiplatform plugin
    kotlin("native.cocoapods") // Cocoapods plugin
    kotlin("plugin.serialization") version "1.5.10" // Plugin for serialization
    id("com.android.library") // Android library plugin
    id("com.squareup.sqldelight") // SQLDelight plugin
    id("com.google.devtools.ksp") version "1.8.21-1.0.11" // KSP plugin
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-10" // Native coroutines plugin
}

/**
 * Shared code configuration:
 * - platforms (Android, iOS, e.t.c)
 * - targets (iosX64,iosArm64, e.t.c)
 * - integration with cocoapods (for iOS)
 * - different source-set setup (library, framework dependencies)
 */
@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    // Android setup
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    // iOS setup
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }

        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    // Source-set setup
    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        val ktorVersion = "2.3.3"
        val sqlDelightVersion = "1.5.5"
        val koinVersion = "3.4.3"
        val kotlinxSerializationVersion = "1.5.1"
        val kotlinCoroutinesVersion = "1.7.1"

        // Common dependencies
        val commonMain by getting {
            dependencies {
                // Koin DI
                api("io.insert-koin:koin-core:$koinVersion")
                // Coroutines native
                api("com.rickclephas.kmm:kmm-viewmodel-core:1.0.0-ALPHA-10")
                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
                // Sql Delight
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqlDelightVersion")
            }
        }

        // Android-specific dependencies
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                api("io.insert-koin:koin-android:$koinVersion")
            }
        }

        // iOS-specific dependencies
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
    }
}

/**
 * SQLDelight database setup
 */
sqldelight {
    database("DogifyDatabase") {
        packageName = "com.alex.iachimov.mykmmapplication.db"
    }
}

/**
 * Other Android configurations setup
 */
android {
    namespace = "com.alex.iachimov.mykmapplication"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}