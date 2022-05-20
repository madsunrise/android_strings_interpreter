package main.kotlin

import java.util.regex.Pattern

object Regex {
    val BEGIN_REGEX = Pattern.compile("<string name=\"[a-z|_]+\">")
    const val LINE_END = "</string>"
}