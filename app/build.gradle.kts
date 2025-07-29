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
        compileSdk = 34
        targetSdk = 34
        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.zwstudio.logicpuzzlesandroid"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1", {
        exclude(group = "com.android.support", module = "support-annotations")
    })
    implementation("androidx.appcompat:appcompat:1.7.1")
    testImplementation("junit:junit:4.13.2")
    implementation("javax.persistence:persistence-api:1.0.2")
    implementation("uk.com.robust-it:cloning:1.9.12")
    implementation("io.insert-koin:koin-android:4.1.0")
    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
}
