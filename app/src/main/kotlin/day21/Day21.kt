package day21

import com.google.common.base.Stopwatch
import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import common.*
import java.lang.StringBuilder
import kotlin.math.abs

class InputReader(fileReader: FileReader, filename: String) {

    private val codes = fileReader.readFileLines(filename)
    val numericKeypad: ImmutableBiMap<Char, Point>
    val directionalKeypad: ImmutableBiMap<Char, Point> = ImmutableBiMap.of(
        '^', Point(1, 0),
        'A', Point(2, 0),
        '<', Point(0, 1),
        'v', Point(1, 1),
        '>', Point(2, 1),
    )

    init {
        val numericKeypadBuilder = ImmutableBiMap.builderWithExpectedSize<Char, Point>(11)
        numericKeypadBuilder.put('7', Point(0, 0))
        numericKeypadBuilder.put('8', Point(1, 0))
        numericKeypadBuilder.put('9', Point(2, 0))
        numericKeypadBuilder.put('4', Point(0, 1))
        numericKeypadBuilder.put('5', Point(1, 1))
        numericKeypadBuilder.put('6', Point(2, 1))
        numericKeypadBuilder.put('1', Point(0, 2))
        numericKeypadBuilder.put('2', Point(1, 2))
        numericKeypadBuilder.put('3', Point(2, 2))
        numericKeypadBuilder.put('0', Point(1, 3))
        numericKeypadBuilder.put('A', Point(2, 3))
        numericKeypad = numericKeypadBuilder.build()
    }

    private fun pathBetween(start: Point, end: Point): String {
        val sb = StringBuilder()
        val diff = end - start
        // As long as you always go right before going left, you won't hit the empty spot on either keypad
        if (diff.x > 0) {
            sb.append(">".repeat(diff.x))
        }
        if (diff.y > 0) {
            sb.append("v".repeat(diff.y))
        }
        if (diff.y < 0) {
            sb.append("^".repeat(-diff.y))
        }
        if (diff.x < 0) {
            sb.append("<".repeat(-diff.x))
        }
        sb.append('A')
        return sb.toString()
    }

    fun instructionsForCode(code: String, keypad: Map<Char, Point>): String {
        val sb = StringBuilder()
        var start = keypad['A']!!
        for (c in code) {
            val end = keypad[c]!!
            // println("$c: $start -> $end (${end - start})")
            sb.append(pathBetween(start, end))
            start = end
        }
        return sb.toString()
    }

    private fun fullInstructionsForCode(code: String): String {
        val iter1 = instructionsForCode(code, numericKeypad)
        val iter2 = instructionsForCode(iter1, directionalKeypad)
        val iter3 = instructionsForCode(iter2, directionalKeypad)
        return iter3
    }

    fun simulate(code: String, keypad: BiMap<Char, Point>): String {
        var p: Point = keypad['A']!!
        val sb = StringBuilder()
        for (c in code) {
            when (c) {
                '>' -> p += Point.RIGHT
                '<' -> p += Point.LEFT
                '^' -> p += Point.UP
                'v' -> p += Point.DOWN
                'A' -> sb.append(keypad.inverse()[p]!!)
            }
        }
        return sb.toString()
    }

    fun codesComplexity(): Int {
        var sum = 0
        for (code in codes) {
            val instr = fullInstructionsForCode(code)
            val numeric = code.substring(0, code.length-1).toInt()
            println("$code: $instr (${instr.length}), $numeric")
            sum += instr.length * numeric
        }
        return sum
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day21.txt")
    // println(inputReader.codesComplexity())
    println(inputReader.instructionsForCode("379A", inputReader.numericKeypad)) // ^A^^<<A>>AvvvA
    println(inputReader.simulate("<A>Av<<AA>^AA>AvAA^A<vAAA>^A", inputReader.directionalKeypad))
    println(inputReader.instructionsForCode("^A^^<<A>>AvvvA", inputReader.directionalKeypad))
    println(inputReader.simulate("<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A", inputReader.directionalKeypad))
    // println(inputReader.instructionsForCode("<A>A<AAv<AA>>^AvAA^Av<AAA>^A", inputReader.directionalKeypad))
    println(sw.stop().elapsed())
}

// v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A
// <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A
