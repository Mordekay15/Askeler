plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.osmdroid)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.android.volley:volley:1.2.1")
//    implementation("com.mapbox.maps:android:10.15.0")
//    implementation("com.mapbox.navigation:core:2.18.0")
//    implementation("com.mapbox.navigation:android:2.18.0")
    //implementation(libs.android)
    //implementation(libs.mapbox.sdk.services)
    //implementation(libs.navigationcore.android)
//    implementation(libs.locationcomponent)
//    implementation(libs.extension.style)
//    implementation("com.mapbox.plugin:annotation:11.11.0")
//    implementation("com.mapbox:mapbox-sdk-services:6.12.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}