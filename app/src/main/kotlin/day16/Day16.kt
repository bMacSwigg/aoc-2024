package day16

import com.google.common.base.Stopwatch
import common.FileReader
import common.Point
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.PriorityQueue


data class Node(val loc: Point, val dir: Point) {

    // all the adjacent nodes -- ignoring maze walls -- and the cost associated with them
    fun reachable(): Map<Node, Int> {
        val forward = Node(loc+dir, dir)
        val nodes = mutableMapOf(Pair(forward, 1))
        when (dir) {
            Point.UP, Point.DOWN -> {
                nodes[Node(loc, Point.LEFT)] = 1000
                nodes[Node(loc, Point.RIGHT)] = 1000
            }
            Point.LEFT, Point.RIGHT -> {
                nodes[Node(loc, Point.UP)] = 1000
                nodes[Node(loc, Point.DOWN)] = 1000
            }
        }
        return nodes
    }
}

class Step(val target: Node, val distance: Int): Comparable<Step> {
    override fun compareTo(other: Step): Int {
        return distance - other.distance
    }
}

class Maze(private val board: Array<CharArray>) {

    private val height = board.size
    private val width = board[0].size
    private var start: Point = Point(-1, -1)
    private var end: Point = Point(-1, -1)



    init {
        var foundStart = false
        var foundEnd = false
        outer@ for (y in 0..<height) {
            for (x in 0..<width) {
                if (!foundStart && board[y][x] == 'S') {
                    start = Point(x, y)
                    foundStart = true
                } else if (!foundEnd && board[y][x] == 'E') {
                    end = Point(x, y)
                    foundEnd = true
                }
                if (foundStart && foundEnd) {
                    break@outer
                }
            }
        }
    }

    private fun validReachable(n: Node): Map<Node, Int> {
        return n.reachable().filter { get(it.key.loc) != '#' }
    }

    private fun stepDistances(n: Node, d: Int): List<Step> {
        return validReachable(n).map { Step(it.key, d+it.value) }
    }

    fun dijkstra(): Int {
        val visited = mutableSetOf<Node>()
        val distances = mutableMapOf<Node, Int>()
        val steps = PriorityQueue<Step>()

        distances[Node(start, Point.RIGHT)] = 0
        steps.addAll(stepDistances(Node(start, Point.RIGHT), 0))

        while (steps.isNotEmpty()) {
            val s = steps.poll()
            if (visited.contains(s.target)) {
                continue
            }
            if (s.target.loc == end) {
                return s.distance
            }
            visited.add(s.target)
            distances[s.target] = s.distance
            steps.addAll(stepDistances(s.target, s.distance))
        }
        return -1
    }

    private fun get(p: Point): Char {
        return board[p.y][p.x]
    }

    private fun set(p: Point, c: Char) {
        board[p.y][p.x] = c
    }

    override fun toString(): String {
        return board.joinToString("\n") { it.joinToString("") }
    }
}

class InputReader(fileReader: FileReader, filename: String) {
    private val board = fileReader.readFileLines(filename).map { it.toCharArray() }.toTypedArray()
    val maze = Maze(board)

}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day16.txt")
    println(inputReader.maze.dijkstra())
    println(sw.stop().elapsed())
}
