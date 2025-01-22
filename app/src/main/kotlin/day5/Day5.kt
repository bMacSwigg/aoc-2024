package day5

import com.google.common.base.Stopwatch
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import common.FileReader

class Rules(rulesLines: List<String>) {

    private val rules: Multimap<Int, Int>

    init {
        val rulesBuilder = ImmutableMultimap.builder<Int,Int>()
        for (line in rulesLines) {
            val nums = line.split('|', limit=2)
            rulesBuilder.put(nums[0].toInt(), nums[1].toInt())
        }
        rules = rulesBuilder.build()
    }

    fun isValid(first: Int, second: Int): Boolean {
        if (rules.get(first).contains(second)) {
            return true;
        } else if (rules.get(second).contains(first)) {
            return false;
        } else {
            println("No defined rule, assuming valid")
            return true;
        }
    }
}

class Print(private val pages: List<Int>) {

    constructor(line: String) : this(line.split(',').map { it.toInt() })

    fun isValid(rules: Rules): Boolean {
        for ((i, first) in pages.withIndex()) {
            for (second in pages.subList(i+1, pages.size)) {
                if (!rules.isValid(first, second)) return false
            }
        }
        return true
    }

    fun fixOrder(rules: Rules): Print {
        val newPages = pages.sortedWith { first, second ->
            if (rules.isValid(first, second)) 1 else -1
        }
        return Print(newPages)
    }

    fun middleValue(): Int {
        val midIndex = pages.size / 2
        return pages[midIndex]
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    companion object {
        val RULES_REGEX = Regex("""\d+\|\d+""")
        val PRINT_REGEX = Regex("""^[\d,]+$""")
    }

    private val lines: List<String> = fileReader.readFileLines(filename)
    private val rules: Rules = Rules(lines.filter { RULES_REGEX.matches(it) })
    private val prints: List<Print> = lines.filter { PRINT_REGEX.matches(it) }.map { Print(it) }

    fun sumValidMiddles(): Int {
        return prints.filter { it.isValid(rules) }.sumOf { it.middleValue() }
    }

    fun fixAndSumMiddles(): Int {
        return prints.filter { !it.isValid(rules) }.map { it.fixOrder(rules) }.sumOf { it.middleValue() }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day5.txt")
    println(inputReader.sumValidMiddles())
    println(inputReader.fixAndSumMiddles())
    println(sw.stop().elapsed())
}
