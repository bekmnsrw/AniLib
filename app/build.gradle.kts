import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.detekt)
    alias(libs.plugins.firebaseService)
    alias(libs.plugins.firebaseAppDistribution)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.firebasePerf)
}

android {
    namespace = "com.bekmnsrw.anilib"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.bekmnsrw.anilib"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.bekmnsrw.oauth"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val keystoreFile = project.rootProject.file("secret.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiBaseUrl = properties.getProperty("apiBaseUrl") ?: ""
        val authBaseUrl = properties.getProperty("authBaseUrl") ?: ""
        val clientId = properties.getProperty("clientId") ?: ""
        val clientSecret = properties.getProperty("clientSecret") ?: ""
        val redirectUri = properties.getProperty("redirectUri") ?: ""

        buildConfigField(
            type = "String",
            name = "API_BASE_URL",
            value = apiBaseUrl
        )

        buildConfigField(
            type = "String",
            name = "AUTH_BASE_URL",
            value = authBaseUrl
        )

        buildConfigField(
            type = "String",
            name = "CLIENT_ID",
            value = clientId
        )

        buildConfigField(
            type = "String",
            name = "CLIENT_SECRET",
            value = clientSecret
        )

        buildConfigField(
            type = "String",
            name = "REDIRECT_URI",
            value = redirectUri
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.kotlin.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:db"))

    implementation(project(":feature:auth:api"))

    implementation(project(":feature:auth:impl"))
    implementation(project(":feature:home:impl"))
    implementation(project(":feature:favorites:impl"))
    implementation(project(":feature:profile:impl"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.messaging)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material)
    implementation(libs.koin)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.test.junit4)
}
