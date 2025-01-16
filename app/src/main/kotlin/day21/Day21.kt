package day21

import com.google.common.base.Stopwatch
import com.google.common.collect.ImmutableBiMap
import common.*


data class KeypadNode(override val loc: Point, val prev: Char, val cost: (s: Char, t: Char) -> Long): Node(loc) {
    override fun reachable(): Map<Node, Long> {
        val nodes: Map<Node, Long> = mapOf(
            Pair(KeypadNode(loc+Point.UP, '^', cost), cost(prev, '^')),
            Pair(KeypadNode(loc+Point.LEFT, '<', cost), cost(prev, '<')),
            Pair(KeypadNode(loc+Point.DOWN, 'v', cost), cost(prev, 'v')),
            Pair(KeypadNode(loc+Point.RIGHT, '>', cost), cost(prev, '>')),
            Pair(KeypadNode(loc, 'A', cost), cost(prev, 'A')))
        return nodes
    }
}

data class CacheKey(val layers: Int, val p1: Point, val c1: Char, val p2: Point, val c2: Char) {
    constructor(layers: Int, n1: KeypadNode, n2: KeypadNode): this(layers, n1.loc, n1.prev, n2.loc, n2.prev)
}

class InputReader(fileReader: FileReader, filename: String) {

    private val codes = fileReader.readFileLines(filename)
    private val numericKeypad: ImmutableBiMap<Char, Point>
    private val directionalKeypad: ImmutableBiMap<Char, Point> = ImmutableBiMap.of(
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

    private val directionalTiles = arrayOf(
        charArrayOf('#', '.', '.'),
        charArrayOf('.', '.', '.')
    )
    private var numericTiles = arrayOf(
        charArrayOf('.', '.', '.'),
        charArrayOf('.', '.', '.'),
        charArrayOf('.', '.', '.'),
        charArrayOf('#', '.', '.')
    )

    private val costCache: MutableMap<CacheKey, Long> = mutableMapOf()

    private fun costOfNumeric(layers: Int, code: String): Long {
        var sum = 0L
        var start = 'A'
        for (c in code) {
            sum += costOnNumericKeypad(layers, start, c)
            start = c
        }
        return sum
    }

    private fun costOnNumericKeypad(layers: Int, s: Char, t: Char): Long {
        val costFn = this.costFnOnDirectionalKeypad(layers)
        val start = KeypadNode(numericKeypad[s]!!, 'A', costFn)
        val end = KeypadNode(numericKeypad[t]!!, 'A', costFn)
        val dijkstra = Dijkstra(Board(numericTiles, start, end))
        val cost = dijkstra.run(1)
        return cost
    }

    private fun costFnOnDirectionalKeypad(layers: Int): (Char, Char) -> Long {
        val costFn = if (layers <= 2) {
            this::costOnColdKeypad
        } else {
            this.costFnOnDirectionalKeypad(layers-1)
        }
        return fun(s: Char, t: Char): Long {
            val start = KeypadNode(directionalKeypad[s]!!, 'A', costFn)
            val end = KeypadNode(directionalKeypad[t]!!, 'A', costFn)

            val cacheKey = CacheKey(layers, start, end)
            if (costCache.containsKey(cacheKey)) {
                return costCache[cacheKey]!!
            }

            val dijkstra = Dijkstra(Board(directionalTiles, start, end))
            val cost = dijkstra.run(1)
            costCache[cacheKey] = cost
            return cost
        }
    }

    fun costOnColdKeypad(s: Char, t: Char): Long {
        return (directionalKeypad[s]!!.gridDistance(directionalKeypad[t]!!)) + 1L
    }

    fun codesComplexity(layers: Int): Long {
        var sum = 0L
        for (code in codes) {
            val cost = costOfNumeric(layers, code)
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
    println(inputReader.codesComplexity(2))
    println(inputReader.codesComplexity(25))
    println(sw.stop().elapsed())
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
