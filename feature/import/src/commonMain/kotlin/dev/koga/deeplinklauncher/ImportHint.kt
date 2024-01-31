package dev.koga.deeplinklauncher

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import kotlinx.datetime.Clock


val generalPropertiesHint: AnnotatedString
    @Composable get() {
        return buildAnnotatedString {
            append(
                "It's possible to add more properties to the object, " +
                        "such as"
            )

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append(" id, name, description, createdAt, isFavorite, ")
            }

            append("and ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("folder.")
            }
        }
    }

val folderPropertiesHint: AnnotatedString
    @Composable get() {
        return buildAnnotatedString {
            append(
                "The folder object has the following properties: "
            )

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("id, name, ")
            }

            append("and ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("description.")
            }
        }
    }


val uuidHint: AnnotatedString
    @Composable get() {
        return buildAnnotatedString {
            append("We recommend using the ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("UUID ")
            }
            append("format for the ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("id ")
            }
            append("property")
        }
    }

val createdAtHint: AnnotatedString
    @Composable get() {
        return buildAnnotatedString {
            append("For the ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("createdAt ")
            }

            append("property, if you want to include it, you must use the ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("ISO 8601 ")
            }

            append("format. Example: ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append(Clock.System.now().toString())
            }
        }
    }

val basicJsonPreview = """
    {
        "link": string
    }
""".trimIndent()

val jsonStructurePreview = """
        [
            "folders": [
                {
                    "id": string,           // optional
                    "name": string,         // required
                    "description": string   // optional
                },
                ...
            ],
            "deepLinks": [
                {
                    "id": string,               // optional
                    "link": string,             // required
                    "name": string,             // optional
                    "description": null,        // optional
                    "createdAt": string,        // optional
                    "isFavorite": boolean       // optional,
                    "folderId": string,         // optional
                },
                ...
            ]
        ]
    """.trimIndent()


val plainTextPreview = """
    test://myapplink
    https://myapplink2
    test://myapplink3
""".trimIndent()
