package com.example.textreader

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle

@Composable
fun FileReaderScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val text = rememberSaveable { mutableStateOf("") }
    val loading = rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                loading.value = true
                text.value = readFile(context, uri)
                loading.value = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Button(
            onClick = { launcher.launch(arrayOf("*/*")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть файл")
        }

        if (loading.value) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else {
            Text(
                text = buildAnnotatedString {
                    append(text.value)
                    Regex("""\[[^]]+]""").findAll(text.value).forEach {
                        addStyle(
                            SpanStyle(fontStyle = FontStyle.Italic),
                            it.range.first + 1,
                            it.range.last
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}