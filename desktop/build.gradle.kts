import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.multiplatformcomposeapplication"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "18"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                val osName = System.getProperty("os.name")
                val targetOs = when {
                    osName == "Mac OS X" -> "macos"
                    osName.startsWith("Win") -> "windows"
                    osName.startsWith("Linux") -> "linux"
                    else -> error("Unsupported OS: $osName")
                }

                val targetArch = when (val osArch = System.getProperty("os.arch")) {
                    "x86_64", "amd64" -> "x64"
                    "aarch64" -> "arm64"
                    else -> error("Unsupported arch: $osArch")
                }

                val version = "0.7.70" // or any more recent version
                val target = "${targetOs}-${targetArch}"
                implementation("org.jetbrains.skiko:skiko-awt-runtime-$target:$version")
                implementation(libs.ui.util)
                implementation(libs.richeditor.compose)
//                implementation(libs.markdown)
//                implementation(libs.ksoup.html)


            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MD Editor"
            packageVersion = "1.0.0"
            windows {
                this.packageVersion = "1.0.0"
            }
        }
    }
}

//jpackage {
//    imageName = "my-app"
//
//    destinationDir = file("build/jpackage")
//
//    appVersion = "1.0.0"
//
//    inputDir = findProperty("kotlin.root") + "/build/libs"
//
//    installerType = "exe"
//
//    winDirChooser = true
//    winShortcut = true
//    winMenuGroup = true
//}
