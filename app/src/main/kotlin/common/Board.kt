package common

import java.util.PriorityQueue


class Board(private val tiles: Array<CharArray>, private val start: Node, private val end: Node) {
    private val height = tiles.size
    private val width = tiles[0].size

    fun get(p: Point): Char {
        return tiles[p.y][p.x]
    }

    fun set(p: Point, c: Char) {
        tiles[p.y][p.x] = c
    }

    override fun toString(): String {
        return tiles.joinToString("\n") { it.joinToString("") }
    }

    private fun validReachable(n: Node): Map<Node, Int> {
        return n.reachable().filter { it.key.loc.inBounds(width, height) && get(it.key.loc) != '#' }
    }

    private fun stepDistances(n: Node, d: Int): List<Step> {
        return validReachable(n).map { Step(n, it.key, d+it.value) }
    }

    fun dijkstra(): Int {
        val visited = mutableSetOf<Node>()
        val distances = mutableMapOf<Node, Int>()
        val steps = PriorityQueue<Step>()

        distances[start] = 0
        steps.addAll(stepDistances(start, 0))

        while (steps.isNotEmpty()) {
            val s = steps.poll()
            if (visited.contains(s.target)) {
                continue
            }
            if (s.target == end) {
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
