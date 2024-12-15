package day11.kt

import com.google.common.base.Stopwatch
import common.FileReader

class CachingStoneCalculator() {
    private val lookup: MutableMap<Pair<Stone, Int>, Long> = mutableMapOf()

    fun get(stone: Stone, blinks: Int): Long {
        if (Pair(stone, blinks) in lookup) {
            return lookup[Pair(stone, blinks)]!!
        } else {
            val result = calculate(stone, blinks)
            put(stone, blinks, result)
            return result
        }
    }

    private fun calculate(stone: Stone, blinks: Int): Long {
        return if (blinks == 0) {
            1
        } else {
            stone.blink().sumOf { get(it, blinks-1) }
        }
    }

    private fun put(stone: Stone, blinks: Int, stones: Long) {
        lookup[Pair(stone, blinks)] = stones
    }
}

data class Stone(val num: Long) {

    fun blink(): List<Stone> {
        if (num == 0L) {
            return listOf(Stone(1L))
        }

        val asStr = num.toString()
        if (asStr.length % 2 == 0) {
            val mid = asStr.length / 2
            val num1 = asStr.substring(0, mid).toLong()
            val num2 = asStr.substring(mid).toLong()
            return listOf(Stone(num1), Stone(num2))
        }

        return listOf(Stone(num * 2024))
    }
}

class StoneReader(fileReader: FileReader, filename: String) {

    private var stones =
        fileReader.readFileText(filename)
            .split(" ")
            .map { Stone(it.toLong()) }
    private val cached: CachingStoneCalculator = CachingStoneCalculator()

    fun calculate(blinks: Int): Long {
        return stones.sumOf { cached.get(it, blinks) }
    }

    private fun blink() {
        stones = stones.flatMap { it.blink() }
    }

    fun blink(n: Int, verbose: Boolean = false) {
        for (i in 1..n) {
            if (verbose) print("$i, ")
            blink()
        }
        if (verbose) println("\nFinal: ${countStones()} stones")
    }

    fun countStones(): Int {
        return stones.size
    }

    override fun toString(): String {
        return stones.joinToString(" ") { it.num.toString() }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val stoneReader = StoneReader(FileReader(), "day11.txt")
    println(stoneReader.calculate(25))
    println(stoneReader.calculate(75))
    println(sw.stop().elapsed())
}
