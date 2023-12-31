@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.firebaseCrashlytics)
}

android {
    namespace = "com.bekmnsrw.feature.home.impl"
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

    implementation(project(":feature:home:api"))

    implementation(project(":core:widget"))
    implementation(project(":core:db"))
    implementation(project(":core:utils"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:auth:impl"))
    implementation(project(":feature:auth:api"))
    implementation(project(":feature:favorites:api"))

    implementation(libs.koin)
    implementation(libs.koin.compose)
    implementation(libs.voyager.tab)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.koin)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.lifecycle.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.juint.kotlin)
    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
