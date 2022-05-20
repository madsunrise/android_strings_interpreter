package main.kotlin.to_android

import main.kotlin.Regex
import java.io.File

class ToAndroidStringsConverter {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sourceFile = File("./src/main/kotlin/to_android/input.txt")
            if (!sourceFile.exists()) {
                throw IllegalStateException("Input file not found")
            }
            val sampleFile = File("./src/main/kotlin/to_human/input.xml")
            val outputFile = File("./src/main/kotlin/to_android/output.xml")
            if (outputFile.exists()) {
                outputFile.delete()
            }
            convert(sourceFile, outputFile, sampleFile.readText())
        }

        private fun convert(humanText: File, outputFile: File, sampleXml: String) {
            val lineBeginnings = Regex.BEGIN_REGEX.toRegex().findAll(sampleXml).map { it.value }.toList()
            val writer = outputFile.bufferedWriter().apply {
                write("<resources>")
                newLine()
            }

            for ((i, newHumanValue) in humanText.readLines().withIndex()) {
                val part = lineBeginnings[i]
                val lineBeginIndex = sampleXml.indexOf(part)
                if (lineBeginIndex < 0) {
                    throw IllegalStateException("Could not find index for $part")
                }
                val textStartsIndex = lineBeginIndex + part.length
                val lineEndIndex = sampleXml.indexOf(Regex.LINE_END, startIndex = textStartsIndex)
                if (lineEndIndex < 0) {
                    throw IllegalStateException("Could not find index for ${Regex.LINE_END}, beginning: $part")
                }
                val wholeOldLine = sampleXml.substring(lineBeginIndex, lineEndIndex + Regex.LINE_END.length)
                val oldHumanValue = sampleXml.substring(textStartsIndex, lineEndIndex)
                val wholeNewLine = wholeOldLine.replace(">$oldHumanValue<", ">$newHumanValue<")
                with(writer) {
                    write(wholeNewLine)
                    newLine()
                }
            }

            writer.write("</resources>")
            writer.close()
        }
    }

    private class LineReader(file: File) {
        private val iterator = file.readLines().iterator()

        fun readNextLine(): String? {
            if (iterator.hasNext()) {
                return iterator.next()
            }
            return null
        }
    }
}