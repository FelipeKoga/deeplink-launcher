package dev.koga.deeplinklauncher.cli.suite

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal data class SuiteCase(
    val name: String?,
    val url: String,
    val folder: String?,
    val expect: Expectation,
)

@Serializable
internal data class Expectation(
    val opens: Boolean = true,
    val android: String? = null,
    val ios: String? = null,
)

internal object Suite {

    private val json = Json {
        ignoreUnknownKeys = true
        allowTrailingComma = true
    }

    fun parse(text: String): List<SuiteCase> {
        val content = text.trim()
        return if (content.startsWith("{")) fromExport(content) else fromLines(content)
    }

    private fun fromExport(content: String): List<SuiteCase> {
        val export = try {
            json.decodeFromString<ExportFile>(content)
        } catch (e: SerializationException) {
            throw CliFailure(
                exitCode = ExitCode.USAGE,
                message = "The suite is not a valid DeepLink Launcher export: ${e.message?.lineSequence()?.first()}",
                hint = "Export a collection from the app, or pass one link per line.",
                cause = e,
            )
        }
        val folders = export.folders.orEmpty().associate { it.id to it.name }
        return export.deepLinks.map { deepLink ->
            SuiteCase(
                name = deepLink.name?.ifBlank { null },
                url = deepLink.link,
                folder = deepLink.folderId?.let(folders::get),
                expect = (deepLink.expect ?: Expectation()).let { expect ->
                    expect.copy(android = expect.android ?: deepLink.targetPackage?.ifBlank { null })
                },
            )
        }
    }

    private fun fromLines(content: String): List<SuiteCase> =
        content.lineSequence()
            .map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .map { SuiteCase(name = null, url = it, folder = null, expect = Expectation()) }
            .toList()

    @Serializable
    private data class ExportFile(
        val folders: List<Folder>? = null,
        val deepLinks: List<DeepLink>,
    )

    @Serializable
    private data class Folder(val id: String, val name: String)

    @Serializable
    private data class DeepLink(
        val link: String,
        val name: String? = null,
        val folderId: String? = null,
        val targetPackage: String? = null,
        val expect: Expectation? = null,
    )
}
