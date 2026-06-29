plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.uph.m24si1.cashguardkelompok4"
    compileSdk = 35

    defaultConfig {
        applicationId = "edu.uph.m24si1.cashguardkelompok4"
        minSdk = 30
        targetSdk = 35
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
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    
    // Room Database
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // LiveData & ViewModel
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
}
