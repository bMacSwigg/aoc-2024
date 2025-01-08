package day20

import com.google.common.base.Stopwatch
import common.*

class InputReader(fileReader: FileReader, filename: String) {

    private val board = Board.fromText(fileReader.readFileLines(filename))
    private val dijkstra = Dijkstra(board)

    init {
        dijkstra.run()
        // need to set these so the adjacency function can work easily
        board.set(board.start.loc, '.')
        board.set(board.end.loc, '.')
    }

    private fun adjacent(p: Point, c: Char): List<Point> {
        return listOf(
            p + Point.UP,
            p + Point.LEFT,
            p + Point.DOWN,
            p + Point.RIGHT
        ).filter { it.inBounds(board.width, board.height) && board.get(it) == c }
    }

    fun allCheatsAbove(threshold: Int): Int {
        var sum = 0
        for (y in 0..<board.height) {
            for (x in 0..<board.width) {
                val startCheat = Point(x, y)
                if (board.get(startCheat) != '.') {
                    continue
                }
                val walls = adjacent(startCheat, '#')
                val endCheats = walls.flatMap { adjacent(it, '.') }.toSet()

                val startTime = dijkstra.distances[Node(startCheat)]!!
                val savings = endCheats.map { dijkstra.distances[Node(it)]!! - startTime - 2 }

                sum += savings.count { it > threshold }
            }
        }
        return sum
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day20.txt")
    println(inputReader.allCheatsAbove(99))
    println(sw.stop().elapsed())
}
