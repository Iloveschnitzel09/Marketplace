import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api("dev.slne.surf:surf-database:2.2.1-SNAPSHOT")
}

group = "de.schnitzel"
version = "1.21.7-1.0.0-SNAPSHOT"

surfPaperPluginApi {
    mainClass("de.schnitzel.marketplace.Marketplace")
    generateLibraryLoader(false)
    authors.add("Schnitzel")

    runServer {
        withSurfApiBukkit()
    }
}

kotlin {
    jvmToolchain(24)
}
