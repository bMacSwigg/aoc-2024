package common

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    fun inBounds(width: Int, height: Int): Boolean {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height)
    }
}
