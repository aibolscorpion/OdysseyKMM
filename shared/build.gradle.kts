plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.parcelize)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "shared"
//            isStatic = true
//        }
//    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.slf4j.simple)
            implementation(libs.paging.common)
            implementation(libs.datastore.preferences.core)
            implementation(libs.sqldelight.runtime)
            implementation(libs.coroutines.extensions)
            implementation(libs.sqldelight.paging3)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android.driver)
        }
//        iosMain.dependencies {
//            implementation(libs.ktor.client.darwin)
//        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

sqldelight{
    databases{
        create("OdysseyDatabase"){
            packageName.set("kz.divtech.odssey.database")
        }
    }
}
android {
    namespace = "kz.divtech.odyssey.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
dependencies {
    implementation(libs.core.ktx)
    implementation(libs.transport.runtime)
}
