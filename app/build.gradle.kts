import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bottonnavapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bottonnavapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Leer la clave de API desde apikeys.properties
        val apiKeysProperties = Properties()
        val apiKeysPropertiesFile = rootProject.file("apikeys.properties")
        if (apiKeysPropertiesFile.exists()) {
            apiKeysPropertiesFile.inputStream().use { stream ->
                apiKeysProperties.load(stream)
            }
        }

        val openaiApiKey = apiKeysProperties.getProperty("openai.api.key") ?: ""
        buildConfigField("String", "OPENAI_API_KEY", "\"$openaiApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //nuevas
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))

    //implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-firestore")

    //implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-auth")

    //implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-database")

    //navigationbar
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //dependencias para chatGPT
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //CardView
    implementation ("androidx.cardview:cardview:1.0.0")

}