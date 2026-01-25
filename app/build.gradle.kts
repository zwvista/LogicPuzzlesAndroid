plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("realm-android")
}

android {
    defaultConfig {
        applicationId = "com.zwstudio.logicpuzzlesandroid"
        minSdk = 26
        compileSdk = 35
        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    namespace = "com.zwstudio.logicpuzzlesandroid"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    androidTestImplementation(libs.espresso.core, {
        exclude(group = "com.android.support", module = "support-annotations")
    })
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    implementation(libs.persistence.api)
    implementation(libs.cloning)
    implementation(libs.koin.android)
    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary.android)
}
