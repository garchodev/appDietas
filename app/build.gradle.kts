plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.appdietas"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appdietas"
        minSdk = 34
        targetSdk = 36
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


    // Retrofit - Para hacer las llamadas a la API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson Converter - Para convertir automáticamente JSON a Objetos Kotlin/Java
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.viewpager2)
    implementation(libs.cardview)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation(libs.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}
