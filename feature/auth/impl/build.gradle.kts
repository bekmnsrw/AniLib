@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.bekmnsrw.feature.auth.impl"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.kotlin.compiler.get()
    }
}

dependencies {

    implementation(project(":feature:auth:api"))
    implementation(project(":core:network"))
    implementation(project(":core:navigation"))
    implementation(project(":core:utils"))
    implementation(project(":core:designsystem"))

    implementation(libs.compose)
    implementation(libs.material3)

    implementation(libs.androidx.lifecycle.compose)

    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.androidx.browser)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
