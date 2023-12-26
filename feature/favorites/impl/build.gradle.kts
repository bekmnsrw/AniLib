@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.bekmnsrw.feature.favorites.impl"
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

    implementation(project(":feature:favorites:api"))
    implementation(project(":feature:home:api"))
    implementation(project(":feature:auth:api"))

    implementation(project(":core:widget"))
    implementation(project(":core:db"))
    implementation(project(":core:utils"))
    implementation(project(":core:navigation"))
    implementation(project(":core:presentation"))
    implementation(project(":core:network"))

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.pager)
    implementation(libs.pager.indicators)

    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.voyager.tab)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.koin)

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.compose)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
