package main.kotlin

import java.util.regex.Pattern

object Regex {
    val BEGIN_REGEX = Pattern.compile("<string name=\"[a-zA-Z0-9_]+\">")
    const val LINE_END = "</string>"
}