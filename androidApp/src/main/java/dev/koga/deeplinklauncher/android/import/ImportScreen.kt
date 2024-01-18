package dev.koga.deeplinklauncher.android.import

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTextField
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.android.core.designsystem.defaultTextFieldColors
import dev.koga.deeplinklauncher.usecase.FileType
import dev.koga.deeplinklauncher.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.usecase.ImportDeepLinksOutput
import dev.koga.deeplinklauncher.util.getRealPathFromUri
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class ImportScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val importDeepLinks = koinInject<ImportDeepLinks>()

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current
        val options = listOf("JSON (.json)", "Plain text (.txt)")
        var selectedIndex by remember { mutableIntStateOf(0) }

        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = object : ActivityResultContracts.GetContent() {
                override fun createIntent(context: Context, input: String): Intent {
                    return super.createIntent(context, input)
                        .putExtra(
                            Intent.EXTRA_MIME_TYPES,
                            arrayOf("text/plain", "application/json")
                        )
                }
            },
            onResult = { uri ->
                // Handle the URI, convert it to a file path or process it directly
                val filePath = uri?.getRealPathFromUri(context).orEmpty()
                scope.launch {
                    val response = importDeepLinks.invoke(
                        filePath = filePath,
                        fileType = when (filePath.substringAfterLast(".")) {
                            "txt" -> FileType.TXT
                            "json" -> FileType.JSON
                            else -> throw IllegalStateException("Invalid file type")
                        }
                    )

                    when (response) {
                        is ImportDeepLinksOutput.Success -> {
                            snackbarHostState.showSnackbar("DeepLinks imported successfully")
                            navigator.pop()
                        }

                        is ImportDeepLinksOutput.Error -> {
                            snackbarHostState.showSnackbar("Something went wrong. " +
                                    "Check the content structure and try again.")
                        }
                    }
                }
            }
        )

        Scaffold(
            topBar = {
                DLLTopBar(title = "Import DeepLinks", onBack = navigator::pop)
            },
            snackbarHost =
            { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        "How to Import Data", style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val headerText = buildAnnotatedString {
                        append("The are two types of files that can be imported: ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("plain text (.txt)")
                        }

                        append(" and ")

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("JSON (.json)")
                        }
                    }

                    Text(
                        text = headerText,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ) {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = options.size
                                ),
                                onClick = { selectedIndex = index },
                                selected = index == selectedIndex
                            ) {
                                Text(label)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    AnimatedContent(
                        targetState = selectedIndex,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { width -> width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> -width } + fadeOut()
                            } else {
                                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> width } + fadeOut()
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, label = ""
                    ) { index ->
                        when (index) {
                            0 -> {
                                Column {

                                    Text(
                                        text = "The most basic JSON format is an object that only contains a link property.",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Normal
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    JSONBoxViewer(
                                        text = """
                                        {
                                            "link": string
                                        }
                                    """.trimIndent()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "It`s possible to add more properties to the object, such as id, name, description, createdAt, isFavorite and folder.",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Normal
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    JsonDisplayScreen()
                                }
                            }

                            1 -> {

                                Column {
                                    Text(
                                        text = "The plain text format is a simple list of deeplinks, one per line.",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Normal
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    JSONBoxViewer(
                                        text = "test://myapplink\nhttps://myapplink2\ntest://myapplink3"
                                    )
                                }

                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(82.dp))
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    HorizontalDivider()

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        onClick = {
                            filePickerLauncher.launch("*/*") // Launch the picker
                        }) {
                        Text(text = "Browse")
                    }
                }
            }

        }
    }
}

@Composable
fun JsonDisplayScreen() {
    val jsonText = """
        [
          {
            "id": string,               // optional
            "link": string,             // required
            "name": string,             // optional
            "description": null,        // optional
            "createdAt": string,        // optional
            "isFavorite": boolean       // optional,
            "folder": {                 // optional
                "id": string,           // optional
                "name": string,         // required if folder is present
                "description": string   // optional
            }
          },
          ...
        ]
    """.trimIndent()

    JSONBoxViewer(text = jsonText)
}

@Composable
fun JSONBoxViewer(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .horizontalScroll(
                rememberScrollState()
            )
    ) {
        Text(
            text = text, modifier = Modifier.padding(24.dp), style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}