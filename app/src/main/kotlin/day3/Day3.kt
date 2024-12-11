package day3

import common.FileReader

class ProgramReader(fileReader: FileReader, filename: String) {

    companion object {
        val DO_REGEX = Regex("""do\(\)(.*)""", RegexOption.DOT_MATCHES_ALL)
        val DONT_REGEX = Regex("""(.*?)don't\(\)(.*)""", RegexOption.DOT_MATCHES_ALL)
        val MUL_INSTRUCTION_REGEX = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
    }
    private val text: String = fileReader.readFileText(filename)

    fun sum(): Int {
        return sum(text)
    }

    private fun sum(section: String): Int {
        return MUL_INSTRUCTION_REGEX.findAll(section)
            .map { it.groupValues }
            .map { it[1].toInt() * it[2].toInt() }
            .sum()
    }

    fun conditionalSum(): Int {
        val sections = mutableListOf<String>()
        var remainder = text
        while (remainder.isNotEmpty()) {
            // in DO mode
            val untilDont = DONT_REGEX.find(remainder)
            if (untilDont == null) {
                sections.addLast(remainder)
                break
            }

            val section = untilDont.groupValues[1]
            remainder = untilDont.groupValues[2]
            sections.addLast(section)

            // in DONT mode
            val untilDo = DO_REGEX.find(remainder) ?: break
            remainder = untilDo.groupValues[1]
        }

        return sections.sumOf { sum(it) }
    }
}

fun main() {
    val programReader = ProgramReader(FileReader(), "day3.txt")
    println(programReader.sum())
    println(programReader.conditionalSum())
}
