package main.kotlin.to_human

import java.io.File
import java.util.regex.Pattern

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sourceFile = File("./src/main/kotlin/to_human/input.xml")
            if (!sourceFile.exists()) {
                throw IllegalStateException("Input file not found")
            }
            val outputFile = File("./src/main/kotlin/to_human/output.txt")
            if (outputFile.exists()) {
                outputFile.delete()
            }
            convert(sourceFile.readText(), outputFile)
        }

        private fun convert(sourceText: String, outputFile: File) {
            var currentLine = 1
            val lineBeginnings = BEGIN_REGEX.toRegex().findAll(sourceText).map { it.value }
            val writer = outputFile.bufferedWriter()
            for (lineBeginning in lineBeginnings) {
                val lineBeginIndex = sourceText.indexOf(lineBeginning)
                if (lineBeginIndex < 0) {
                    throw IllegalStateException("Could not find index for $lineBeginning")
                }
                val textStartsIndex = lineBeginIndex + lineBeginning.length
                val lineEndIndex = sourceText.indexOf(LINE_END, startIndex = textStartsIndex)
                if (lineEndIndex < 0) {
                    throw IllegalStateException("Could not find index for $LINE_END, beginning: $lineBeginning")
                }
                val humanText = sourceText
                    .substring(textStartsIndex, lineEndIndex)
                    .replace('\n', ' ')
                    .replace("\\s+".toRegex(), " ")
                with(writer) {
                    write("${currentLine++}")
                    write(". ")
                    write(humanText)
                    newLine()
                }
            }
            writer.close()
        }

        private val BEGIN_REGEX = Pattern.compile("<string name=\"[a-z|_]+\">")
        private const val LINE_END = "</string>"
    }
}