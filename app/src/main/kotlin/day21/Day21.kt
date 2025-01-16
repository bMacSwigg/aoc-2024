package day21

import com.google.common.base.Stopwatch
import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import common.*
import java.lang.StringBuilder


class KeypadTraverser(var pos: Point) {

    fun costTo(other: Point): Int {
        return -1
    }

    fun moveTo(other: Point) {
        pos = other
    }
}

data class KeypadNode(override val loc: Point, val prev: Char, val cost: (s: Char, t: Char) -> Int): Node(loc) {
    override fun reachable(): Map<Node, Int> {
        val nodes: Map<Node, Int> = mapOf(
            Pair(KeypadNode(loc+Point.UP, '^', cost), cost(prev, '^')),
            Pair(KeypadNode(loc+Point.LEFT, '<', cost), cost(prev, '<')),
            Pair(KeypadNode(loc+Point.DOWN, 'v', cost), cost(prev, 'v')),
            Pair(KeypadNode(loc+Point.RIGHT, '>', cost), cost(prev, '>')),
            Pair(KeypadNode(loc, 'A', cost), cost(prev, 'A')))
        return nodes
    }

}

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

    val directionalTiles = arrayOf(
        charArrayOf('#', '.', '.'),
        charArrayOf('.', '.', '.')
    )
    var numericTiles = arrayOf(
        charArrayOf('.', '.', '.'),
        charArrayOf('.', '.', '.'),
        charArrayOf('.', '.', '.'),
        charArrayOf('#', '.', '.')
    )

//    fun costOf(code: String, keypad: Map<Char, Point>, cost: (s: Char, t: Char) -> Int, tiles: Array<CharArray>): Int {
//        var sum = 0
//        var start = KeypadNode(keypad['A']!!, 'A', cost)
//        for (c in code) {
//            val end = KeypadNode(keypad[c]!!, cost)
//            sum += Dijkstra(Board(tiles, start, end)).run()
//            sum += costs['A']!!
//            start = end
//        }
//        return sum
//    }

    fun costOfNumeric(code: String): Int {
        var sum = 0
        var start = 'A'
        for (c in code) {
            sum += costOnNumericKeypad(start, c)
            start = c

        }
        return sum
    }

    fun costOnNumericKeypad(s: Char, t: Char): Int {
        val start = KeypadNode(numericKeypad[s]!!, 'A', this::costOnRadioactiveKeypad)
        val end = KeypadNode(numericKeypad[t]!!, 'A', this::costOnRadioactiveKeypad)
        val dijkstra = Dijkstra(Board(numericTiles, start, end))
        val cost = dijkstra.run(1)
//        println("$s -> $t: $cost")
        return cost
    }

    private fun costFnOnDirectionalKeypad(layers: Int): (Char, Char) -> Int {
        val costFn = if (layers <= 2) {
            this::costOnColdKeypad
        } else {
            this.costFnOnDirectionalKeypad(layers-1)
        }
        return { s: Char, t: Char ->
            val start = KeypadNode(directionalKeypad[s]!!, 'A', costFn)
            val end = KeypadNode(directionalKeypad[t]!!, 'A', costFn)
            val dijkstra = Dijkstra(Board(directionalTiles, start, end))
            dijkstra.run(1)
        }

    }

    fun costOnRadioactiveKeypad(s: Char, t: Char): Int {
        val start = KeypadNode(directionalKeypad[s]!!, 'A', this::costOnColdKeypad)
        val end = KeypadNode(directionalKeypad[t]!!, 'A', this::costOnColdKeypad)
        val dijkstra = Dijkstra(Board(directionalTiles, start, end))
        val cost = dijkstra.run(1)
        if (dijkstra.endNode == null) {
            println("$s, $t, $cost")
        }
        return cost
    }

    fun costOnColdKeypad(s: Char, t: Char): Int {
        return (directionalKeypad[s]!!.gridDistance(directionalKeypad[t]!!)) + 1
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

    fun fullInstructionsForCode(code: String): String {
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
//            val instr = fullInstructionsForCode(code)
            val cost = costOfNumeric(code)
            val numeric = code.substring(0, code.length-1).toInt()
            println("$code: $cost, $numeric")
            sum += cost * numeric
        }
        return sum
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day21.txt")
    println(inputReader.codesComplexity())
//    println(inputReader.instructionsForCode("029A", inputReader.numericKeypad))
//    println(inputReader.instructionsForCode("029A", inputReader.numericKeypad).length)
//    println(inputReader.costOnColdKeypad('A', 'v')) // 3
//    println(inputReader.costOnColdKeypad('v', '<')) // 2
//    println(inputReader.costOnColdKeypad('<', '<')) // 1
//    println(inputReader.costOnColdKeypad('<', 'A')) // 4
//    println(inputReader.costOnRadioactiveKeypad('A', '<')) // 10
//    println(inputReader.costOfNumeric("029A"))
//    testCosts(">^^A", inputReader::costOnRadioactiveKeypad)
//    testCosts("vA<^AA>A", inputReader::costOnColdKeypad)
//    println(inputReader.costOfNumeric("980A"))
//    println(inputReader.costOfNumeric("179A"))
//    println(inputReader.costOfNumeric("456A"))
//    println(inputReader.fullInstructionsForCode("379A"))
//    println(inputReader.costOfNumeric("379A"))
//    println(inputReader.simulate("v<<A>>^AvA^A", inputReader.directionalKeypad))
//    println(inputReader.simulate("<A>A", inputReader.directionalKeypad))
//    println(inputReader.simulate("^A", inputReader.numericKeypad))
//    println(inputReader.simulate("v<<A>>^AAv<A<A>>^AAvAA^<A>A", inputReader.directionalKeypad))
//    println(inputReader.simulate("<AAv<AA>>^A", inputReader.directionalKeypad))
//    println(inputReader.simulate("^A^^<<A", inputReader.numericKeypad))
//    println(inputReader.simulate("<vA<AA>>^AAvA<^A>AAvA^A", inputReader.directionalKeypad))
//    println(inputReader.simulate("v<<AA>^AA>A", inputReader.directionalKeypad))
//    println(inputReader.simulate("^A<<^^A", inputReader.numericKeypad))
//    println(inputReader.costOnNumericKeypad('3', '7'))
//    println(inputReader.costOfNumeric("37"))
//    var sum = 0
//    var s = 'A'
//    for (c in "<<^^A") {
//        val cost = inputReader.costOnRadioactiveKeypad(s, c)
//        println("$s -> $c: $cost")
//        s = c
//        sum += cost
//    }
//    println("$sum")
//
//    sum = 0
//    s = 'A'
//    for (c in "^^<<A") {
//        val cost = inputReader.costOnRadioactiveKeypad(s, c)
//        println("$s -> $c: $cost")
//        s = c
//        sum += cost
//    }
//    println("$sum")
}

fun testCosts(str: String, costFn: (s: Char, t: Char) -> Int) {
    var sum = 0
    var s = 'A'
    for (c in str) {
        val cost = costFn(s, c)
        println("$s -> $c: $cost")
        s = c
        sum += cost
    }
    println("$sum")
}

// v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A
// <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A

// <A>A<AAv<AA>>^AvAA^Av<AAA>^A
// 4422441324121323312232411232
//       *   * *

/*
 * <vA<AA>>^AvAA<^A>A <v<A>>^AvA^A <vA>^A<v<A>^A>AAvA^A <v<A>A>^AAAvA<^A>A
 * v<<A>>^A <A>A vA<^AA>A <vAAA>^A
 * <A ^A >^^A vvvA
 * 029A
 */
// <vA<AA>>^AvAA<^A>A = A -> 0 (18)
// <v<A>>^AvA^A = 0 -> 2 (12)
// <vA>^A<v<A>^A>AAvA^A = 2 -> 9 (20!)
// <v<A>A>^AAAvA<^A>A = 9 -> A (18!)