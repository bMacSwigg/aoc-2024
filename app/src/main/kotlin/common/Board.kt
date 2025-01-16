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
                        start = BasicNode(Point(x, y))
                    } else if (end == null && tiles[y][x] == 'E') {
                        end = BasicNode(Point(x, y))
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

    fun copy(start: Node = this.start, end: Node = this.end): Board {
        return Board(tiles.map { it.copyOf() }.toTypedArray(), start, end)
    }
}

class Dijkstra(private val board: Board) {
    val distances = mutableMapOf<Node, Long>()
    var endNode: Node? = null

    private fun validReachable(n: Node): Map<Node, Long> {
        return n.reachable().filter {
            it.key.loc.inBounds(board.width, board.height) && board.get(it.key.loc) != '#'
        }
    }

    private fun stepDistances(n: Node, d: Long): List<Step> {
        return validReachable(n).map { Step(n, it.key, d+it.value) }
    }

    fun run(shortcutCost: Int = 0): Long {
        if (board.start == board.end) {
            endNode = board.start
            return shortcutCost.toLong()
        }

        val visited = mutableSetOf<Node>()
        val steps = PriorityQueue<Step>()
        distances.clear()

        distances[board.start] = 0
        visited.add(board.start)
        steps.addAll(stepDistances(board.start, 0))

        while (steps.isNotEmpty()) {
            val s = steps.poll()
            if (visited.contains(s.target)) {
                continue
            }
            visited.add(s.target)
            distances[s.target] = s.distance
            // TODO: could add a function for '.matches()'
            if (s.target == board.end) {
                endNode = s.target
                return s.distance
            } else {
                steps.addAll(stepDistances(s.target, s.distance))
            }
        }
        return -1
    }
}

abstract class Node(open val loc: Point) {
    abstract fun reachable(): Map<Node, Long>
}

data class BasicNode(override val loc: Point): Node(loc) {

    // all the adjacent nodes -- ignoring maze walls -- and the cost associated with them
    override fun reachable(): Map<Node, Long> {
        val nodes: Map<Node, Long> = mapOf(
            Pair(BasicNode(loc+Point.UP), 1L),
            Pair(BasicNode(loc+Point.LEFT), 1L),
            Pair(BasicNode(loc+Point.DOWN), 1L),
            Pair(BasicNode(loc+Point.RIGHT), 1L))
        return nodes
    }
}

class Step(val source: Node, val target: Node, val distance: Long): Comparable<Step> {
    override fun compareTo(other: Step): Int {
        return distance.compareTo(other.distance)
    }
}
