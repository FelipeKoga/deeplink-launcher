plugins {
    id("kotlin")
    application
    alias(libs.plugins.deeplinkLauncher.codeAnalysis)
    alias(libs.plugins.kotlinSerialization)
}

version = "0.1.0"

kotlin {
    jvmToolchain(17)
}

application {
    applicationName = "deeplink"
    mainClass.set("dev.koga.deeplinklauncher.cli.MainKt")
}

tasks.distTar {
    compression = Compression.GZIP
    archiveExtension.set("tar.gz")
}

tasks.jar {
    manifest {
        attributes("Implementation-Version" to project.version)
    }
}

val nativeImage by tasks.registering(Exec::class) {
    group = "distribution"
    description = "Builds the deeplink native binary with GraalVM native-image. Needs GRAALVM_HOME."
    val jvmOnlyTerminalBackends = setOf("mordant-jvm-jna-jvm", "mordant-jvm-ffm-jvm", "jna")
    val libraries = configurations.runtimeClasspath.get().incoming.artifactView {
        componentFilter { id -> (id as? ModuleComponentIdentifier)?.module !in jvmOnlyTerminalBackends }
    }.files
    val classpath = files(tasks.jar, libraries)
    val graalHome = providers.environmentVariable("GRAALVM_HOME")
    val binary = layout.buildDirectory.file("native/deeplink")
    val mainClass = application.mainClass
    inputs.files(classpath)
    outputs.file(binary)
    doFirst {
        val home = graalHome.orNull ?: throw GradleException("Set GRAALVM_HOME to a GraalVM JDK 21.")
        binary.get().asFile.parentFile.mkdirs()
        commandLine("$home/bin/native-image", "-cp", classpath.asPath, "-o", binary.get().asFile.path, mainClass.get())
    }
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}
