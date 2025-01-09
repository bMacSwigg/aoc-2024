package day18

import com.google.common.base.Stopwatch
import common.*
import java.lang.AssertionError

class InputReader(fileReader: FileReader, filename: String, width: Int, height: Int) {

    private val coords = fileReader.readFileLines(filename).map {
        val nums = it.split(',', limit=2)
        Pair(nums[0].toInt(), nums[1].toInt())
    }
    val board: Board

    init {
        val tiles = Array(height) { CharArray(width) { '.' } }
        board = Board(tiles, BasicNode(Point(0, 0)), Point(width-1, height-1))
    }

    fun dropBytes(n: Int) {
        for (loc in coords.subList(0, n)) {
            board.set(Point(loc), '#')
        }
    }

    fun testReachability(): Pair<Int, Int> {
        for (loc in coords) {
            board.set(Point(loc), '#')
            if (Dijkstra(board).run() == -1) {
                return loc
            }
        }
        throw AssertionError() // shouldn't be possible, if the input is valid
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day18.txt", 71, 71)
    inputReader.dropBytes(1024)
    println(inputReader.board)
    println(Dijkstra(inputReader.board).run())
    println(inputReader.testReachability())
    println(sw.stop().elapsed())
}
