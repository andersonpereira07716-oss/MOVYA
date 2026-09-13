plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

subprojects {
    tasks.withType(com.android.build.gradle.tasks.ProcessAndroidResources::class.java).configureEach {
        doFirst {
            val termuxAapt2 = file("/data/data/com.termux/files/usr/bin/aapt2")
            if (termuxAapt2.exists()) {
                fileTree("${gradle.gradleUserHomeDir}/caches").matching {
                    include("**/aapt2-*-linux/aapt2")
                    include("**/aapt2")
                }.forEach { targetFile ->
                    if (targetFile.isFile && targetFile.name == "aapt2") {
                        termuxAapt2.copyTo(targetFile, overwrite = true)
                        targetFile.setExecutable(true)
                    }
                }
            }
        }
    }
}
