package day20

import com.google.common.base.Stopwatch
import common.*
import kotlin.math.abs

class InputReader(fileReader: FileReader, filename: String) {

    private val board = Board.fromText(fileReader.readFileLines(filename))
    private val dijkstra = Dijkstra(board)

    init {
        dijkstra.run()
        // need to set these so the adjacency function can work easily
        board.set(board.start.loc, '.')
        board.set(board.end.loc, '.')
    }

    private fun freePointsWithinN(p: Point, n: Int): List<Point> {
        val points = mutableListOf<Point>()
        for (yOffset in -n..n) {
            val xFreedom = n - abs(yOffset)
            for (xOffset in -xFreedom..xFreedom) {
                val newPoint = Point(p.x + xOffset, p.y + yOffset)
                if (newPoint.inBounds(board.width, board.height) && board.get(newPoint) != '#') {
                    points.add(newPoint)
                }
            }
        }
        return points
    }

    fun allCheatsAbove(timeLimit: Int, threshold: Int): Int {
        val cheats = mutableMapOf<Pair<Point, Point>, Int>()
        for (y in 0..<board.height) {
            for (x in 0..<board.width) {
                val startCheat = Point(x, y)
                if (board.get(startCheat) != '.') {
                    continue
                }
                val endCheats = freePointsWithinN(startCheat, timeLimit)

                val startTime = dijkstra.distances[BasicNode(startCheat)]!!
                endCheats.forEach {
                    val saved = dijkstra.distances[BasicNode(it)]!! - startTime - startCheat.gridDistance(it)
                    cheats[Pair(startCheat, it)] = saved
                }
            }
        }
        return cheats.values.count { it > threshold }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day20.txt")
    println(inputReader.allCheatsAbove(2, 99))
    println(inputReader.allCheatsAbove(20, 99))
    println(sw.stop().elapsed())
}
