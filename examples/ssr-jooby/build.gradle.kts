import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    alias(libs.plugins.jooby)
    alias(libs.plugins.kilua.rpc)
    alias(libs.plugins.kilua)
}

val mainClassNameVal = "example.MainKt"

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    jvmToolchain(17)
    jvm {
        withJava()
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set(mainClassNameVal)
        }
    }
    js(IR) {
        useEsModules()
        browser {
            commonWebpackConfig {
                outputFileName = "main.bundle.js"
            }
            runTask {
                sourceMaps = false
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }
    wasmJs {
        useEsModules()
        browser {
            commonWebpackConfig {
                outputFileName = "main.bundle.js"
            }
            runTask {
                sourceMaps = false
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
        if (project.gradle.startParameter.taskNames.find {
                it.contains("wasmJsBrowserProductionWebpack") || it.contains("wasmJsArchive") || it.contains("jarWithWasmJs")
            } != null) {
            applyBinaryen {
                binaryenArgs = mutableListOf(
                    "--enable-nontrapping-float-to-int",
                    "--enable-gc",
                    "--enable-reference-types",
                    "--enable-exception-handling",
                    "--enable-bulk-memory",
                    "--inline-functions-with-loops",
                    "--traps-never-happen",
                    "--fast-math",
                    "--closed-world",
                    "--metrics",
                    "-O3", "--gufa", "--metrics",
                    "-O3", "--gufa", "--metrics",
                    "-O3", "--gufa", "--metrics",
                )
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val webMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(project(":kilua"))
                implementation(project(":modules:kilua-core-css"))
                implementation(project(":modules:kilua-bootstrap"))
                implementation(project(":modules:kilua-ssr"))
            }
        }
        val jsMain by getting {
            dependsOn(webMain)
        }
        val wasmJsMain by getting {
            dependsOn(webMain)
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":modules:kilua-ssr-server-jooby"))
                implementation(libs.jooby.netty)
                implementation(libs.logback.classic)
            }
        }
    }
}

compose {
    platformTypes.set(platformTypes.get() - KotlinPlatformType.jvm)
    kotlinCompilerPlugin.set(libs.versions.compose.plugin)
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=${libs.versions.kotlin.get()}")
}

tasks {
    joobyRun {
        mainClass = mainClassNameVal
        restartExtensions = listOf("conf", "properties", "class")
        compileExtensions = listOf("java", "kt")
        port = 8080
    }
}
