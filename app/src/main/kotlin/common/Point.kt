package common

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {

    constructor(p: Pair<Int, Int>): this(p.first, p.second)

    companion object {
        val UP: Point = Point(0, -1)
        val RIGHT: Point = Point(1, 0)
        val DOWN: Point = Point(0, 1)
        val LEFT: Point = Point(-1, 0)
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    fun inBounds(width: Int, height: Int): Boolean {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height)
    }

    fun gridDistance(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}
