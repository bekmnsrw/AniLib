// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.firebaseService) apply false
    alias(libs.plugins.firebaseAppDistribution) apply false
    alias(libs.plugins.firebaseCrashlytics) apply false
    alias(libs.plugins.firebasePerf) apply false
}
true // Needed to make the Suppress annotation work for the plugins block
