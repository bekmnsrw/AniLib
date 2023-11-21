@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.detekt)
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
}

dependencies {

    implementation(project(":feature:home:api"))

    implementation(project(":core:widget"))
    implementation(project(":core:db"))
    implementation(project(":core:utils"))
    implementation(project(":core:navigation"))
    implementation(project(":core:presentation"))
    implementation(project(":core:network"))

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}