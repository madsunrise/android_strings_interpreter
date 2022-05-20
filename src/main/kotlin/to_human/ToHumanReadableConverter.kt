package main.kotlin.to_human

import main.kotlin.Regex
import java.io.File

class ToHumanReadableConverter {
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
            val lineBeginnings = Regex.BEGIN_REGEX.toRegex().findAll(sourceText).map { it.value }
            val writer = outputFile.bufferedWriter()
            for (lineBeginning in lineBeginnings) {
                val lineBeginIndex = sourceText.indexOf(lineBeginning)
                if (lineBeginIndex < 0) {
                    throw IllegalStateException("Could not find index for $lineBeginning")
                }
                val textStartsIndex = lineBeginIndex + lineBeginning.length
                val lineEndIndex = sourceText.indexOf(Regex.LINE_END, startIndex = textStartsIndex)
                if (lineEndIndex < 0) {
                    throw IllegalStateException("Could not find index for ${Regex.LINE_END}, beginning: $lineBeginning")
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
    }
}