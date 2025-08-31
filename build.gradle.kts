import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

    plugins {
        id("dev.slne.surf.surfapi.gradle.paper-plugin")
    }

    dependencies {
        implementation("com.github.stefvanschie.inventoryframework:IF:0.11.3")
        implementation("org.xerial:sqlite-jdbc:3.45.2.0")
    }

group = "de.schnitzel"

surfPaperPluginApi {
    mainClass("de.schnitzel.marketplace.Marketplace")
    generateLibraryLoader(false)
    authors.add("Schnitzel")

    runServer {
        withSurfApiBukkit()
    }
}
