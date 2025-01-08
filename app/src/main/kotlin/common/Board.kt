package common

import java.lang.IllegalArgumentException
import java.util.PriorityQueue


class Board(private val tiles: Array<CharArray>, val start: Node, val end: Node) {
    companion object {
        fun fromText(lines: List<String>): Board {
            val tiles = lines.map { it.toCharArray() }.toTypedArray()
            var start: Node? = null
            var end: Node? = null
            outer@ for (y in tiles.indices) {
                for (x in tiles[0].indices) {
                    if (start == null && tiles[y][x] == 'S') {
                        start = Node(Point(x, y))
                    } else if (end == null && tiles[y][x] == 'E') {
                        end = Node(Point(x, y))
                    }
                    if (start != null && end != null) {
                        return Board(tiles, start, end)
                    }
                }
            }
            throw IllegalArgumentException()
        }
    }

    val height = tiles.size
    val width = tiles[0].size

    fun get(p: Point): Char {
        return tiles[p.y][p.x]
    }

    fun set(p: Point, c: Char) {
        tiles[p.y][p.x] = c
    }

    override fun toString(): String {
        return tiles.joinToString("\n") { it.joinToString("") }
    }

    fun copy(): Board {
        return Board(tiles.map { it.copyOf() }.toTypedArray(), start, end)
    }
}

class Dijkstra(private val board: Board) {
    val distances = mutableMapOf<Node, Int>()

    private fun validReachable(n: Node): Map<Node, Int> {
        return n.reachable().filter {
            it.key.loc.inBounds(board.width, board.height) && board.get(it.key.loc) != '#'
        }
    }

    private fun stepDistances(n: Node, d: Int): List<Step> {
        return validReachable(n).map { Step(n, it.key, d+it.value) }
    }

    fun run(): Int {
        val visited = mutableSetOf<Node>()
        val steps = PriorityQueue<Step>()
        distances.clear()

        distances[board.start] = 0
        steps.addAll(stepDistances(board.start, 0))

        while (steps.isNotEmpty()) {
            val s = steps.poll()
            if (visited.contains(s.target)) {
                continue
            }
            if (s.target == board.end) {
                return s.distance
            }
            visited.add(s.target)
            distances[s.target] = s.distance
            steps.addAll(stepDistances(s.target, s.distance))
        }
        return -1
    }
}

data class Node(val loc: Point) {

    // all the adjacent nodes -- ignoring maze walls -- and the cost associated with them
    fun reachable(): Map<Node, Int> {
        val nodes = mapOf(
            Pair(Node(loc+Point.UP), 1),
            Pair(Node(loc+Point.LEFT), 1),
            Pair(Node(loc+Point.DOWN), 1),
            Pair(Node(loc+Point.RIGHT), 1))
        return nodes
    }
}

class Step(val source: Node, val target: Node, val distance: Int): Comparable<Step> {
    override fun compareTo(other: Step): Int {
        return distance - other.distance
    }
}
