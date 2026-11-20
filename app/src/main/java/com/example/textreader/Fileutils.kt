package com.example.textreader

import android.content.Context
import android.net.Uri

fun readFile(context: Context, uri: Uri): String {
    try {
        val stream = context.contentResolver.openInputStream(uri)
        val content = stream?.bufferedReader()?.readText() ?: ""
        stream?.close()

        if (content.isBlank()) {
            return "Ошибка: файл пустой"
        }

        val text = if (content.contains("<FictionBook")) {
            var body = content.substringAfter("<body").substringAfter(">")
            body = body.substringBefore("</body>")
            body = body.replace("</p>", "\n\n")
            body = body.replace("</title>", "\n\n")
            body = body.replace(Regex("<[^>]*>"), "")
            body.trim()
        } else {
            content
        }

        return text.lowercase()

    } catch (e: Exception) {
        return "Ошибка чтения файла"
    }
}