import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)

    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

mavenPublishing {
    coordinates(
        groupId = "com.dontsaybojio",
        artifactId = "curvedtext",
        version = libs.versions.curvedtext.get()
    )
    pom {
        name.set("CurvedText")
        description.set("KMP Curved TextView \uD83C\uDF08")
        inceptionYear.set("2026")
        url.set("https://github.com/delacrixmorgan/curvedtext-kmp")
        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://github.com/delacrixmorgan/curvedtext-kmp/blob/main/LICENSE.md")
            }
        }
        developers {
            developer {
                id.set("delacrixmorgan")
                name.set("Morgan Koh")
                email.set("delacrixmorgan@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/delacrixmorgan/curvedtext-kmp")
        }
    }
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}

kotlin {
    androidLibrary {
        namespace = "com.dontsaybojio.curvedtext.lib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
        }
    }
}