package day19

import com.google.common.base.Stopwatch
import common.FileReader

class InputReader(fileReader: FileReader, filename: String) {

    private val towels: List<String>
    private val patterns: List<String>
    private val cache = mutableMapOf(Pair("", 1L))

    init {
        val lines = fileReader.readFileLines(filename)
        towels = lines[0].split(", ")
        patterns = lines.subList(2, lines.size)
    }

    private fun possible(pattern: String): Long {
        if (cache.containsKey(pattern)) {
            return cache[pattern]!!
        }
        val starts = towels.filter { pattern.startsWith(it) }
        val remainders = starts.map { pattern.removePrefix(it) }
        val result = remainders.sumOf { possible(it) }
        cache[pattern] = result
        return result
    }

    fun countPossible(): Int {
        return patterns.count { possible(it) != 0L }
    }

    fun countAllCombos(): Long {
        return patterns.sumOf { possible(it) }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day19.txt")
    println(inputReader.countPossible())
    println(inputReader.countAllCombos())
    println(sw.stop().elapsed())
}
