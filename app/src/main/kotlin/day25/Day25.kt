package day25

import com.google.common.base.Stopwatch
import common.*

data class Lock(val pins: List<Int>) {
    companion object {
        const val LOCK_HEIGHT = 5

        fun parse(lines: List<String>): Lock {
            val pins = mutableListOf<Int>(-1, -1, -1, -1, -1)
            for ((i, line) in lines.withIndex()) {
                for ((j, c) in line.withIndex()) {
                    if (c == '.' && pins[j] == -1) {
                        pins[j] = i-1
                    }
                }
            }
            if (pins.any { it == -1 }) {
                // should not be possible
                throw IllegalStateException(pins.toString())
            }
            return Lock(pins)
        }
    }

    fun fits(key: Key): Boolean {
        val sums = pins.zip(key.heights).map { it.first + it.second }
        return sums.all { it <= LOCK_HEIGHT }
    }
}

data class Key(val heights: List<Int>) {
    companion object {
        fun parse(lines: List<String>): Key {
            val heights = mutableListOf<Int>(-1, -1, -1, -1, -1)
            for ((i, line) in lines.withIndex()) {
                for ((j, c) in line.withIndex()) {
                    if (c == '#' && heights[j] == -1) {
                        heights[j] = Lock.LOCK_HEIGHT - i + 1
                    }
                }
            }
            if (heights.any { it == -1 }) {
                // should not be possible
                throw IllegalStateException(heights.toString())
            }
            return Key(heights)
        }
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    private val locks: List<Lock>
    private val keys: List<Key>

    init {
        val locksBuilder = mutableListOf<Lock>()
        val keysBuilder = mutableListOf<Key>()

        val inputs = fileReader.readFileText(filename).split("\n\n").map { it.split("\n") }
        for (lines in inputs) {
            if (lines[0] == ".....") {
                keysBuilder.add(Key.parse(lines))
            } else if (lines[0] == "#####") {
                locksBuilder.add(Lock.parse(lines))
            } else {
                throw IllegalArgumentException()
            }
        }

        locks = locksBuilder.toList()
        keys = keysBuilder.toList()
    }

    fun countViablePairs(): Long {
        var sum = 0L
        for (lock in locks) {
            for (key in keys) {
                if (lock.fits(key)) sum++
            }
        }
        return sum
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day25.txt")
    println(inputReader.countViablePairs())
    println(sw.stop().elapsed())
}
