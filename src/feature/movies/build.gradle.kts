@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("kmp-library-plugin")
    alias(libs.plugins.jetbrains.compose)
    id("koin-plugin-setup")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.animation)
            implementation(compose.uiUtil)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.moko)
            implementation(libs.kamel)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.kotlin.stdlib)

            implementation(projects.domain)
            implementation(projects.designsystem)
            implementation(projects.platform)
            implementation(projects.resources)
            implementation(libs.kotlinx.collections.immutable)
        }
    }
}
