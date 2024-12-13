package day6

import com.google.common.base.Stopwatch
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import common.FileReader
import kotlin.concurrent.timer

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

class LoopException: Exception("Guard path contains a loop")
class OutOfBoundsException: Exception("Guard left the board")

class Board(
    private val width: Int,
    private val height: Int,
    private val board: Array<CharArray>,
    private val visitedDirs: Array<Array<MutableList<Point>>>,
    private var guard: Point,
    private var dir: Point,
    var count: Int,
    private var spawnNewBoards: Boolean,
    ) {

    constructor(lines: List<String>, spawnNewBoards: Boolean) : this(
        lines[0].length,
        lines.size,
        Array(lines[0].length) { CharArray(lines.size) },
        Array(lines[0].length) { Array(lines.size) { mutableListOf() } },
        Point(-1,-1),
        Point(0, -1), // Start pointing up
        1,
        spawnNewBoards)
    {
        for ((i, line) in lines.withIndex()) {
            for ((j, char) in line.withIndex()) {
                if (char == '^') {
                    guard = Point(j, i)
                    board[j][i] = 'X'
                    visitedDirs[guard.x][guard.y].addLast(dir)
                } else {
                    board[j][i] = char
                }
            }
        }
    }

    private fun clone(): Board {
        return Board(
            width,
            height,
            board.map { it.copyOf() }.toTypedArray(),
            visitedDirs.map { it.map { it.toMutableList() }.toTypedArray() }.toTypedArray(),
            guard,
            dir,
            count,
            spawnNewBoards,
        )
    }

    override fun toString(): String {
        val repr = board.map { it.copyOf() }
        repr[guard.x][guard.y] = when (dir) {
            Point(0, -1) -> '^'
            Point(1, 0) -> '>'
            Point(0, 1) -> 'v'
            Point(-1, 0) -> '<'
            else -> '!'
        }
        val str = StringBuilder()
        // Have to implement myself instead of using joinToString, because it's
        // flipped 90 degrees
        for (y in 0..<height) {
            for (x in 0..<width) {
                str.append(repr[x][y] + " ")
            }
            str.append("\n")
        }
        return str.toString()
    }

    fun moveOne(): Board? {
        val nextPos = guard + dir
        if (nextPos.x >= width || nextPos.x < 0 || nextPos.y >= height || nextPos.y < 0) {
            // Gone off the edge of the board
            throw OutOfBoundsException()
        }

        var newBoard: Board? = null
        when (val nextSquare = board[nextPos.x][nextPos.y]) {
            '.' -> {
                if (spawnNewBoards) {
                    newBoard = clone()
                    newBoard.board[nextPos.x][nextPos.y] = '#'
                    newBoard.spawnNewBoards = false
                }
                guard = nextPos
                board[nextPos.x][nextPos.y] = 'X'
                count++
            }
            'X' -> {
                guard = nextPos
            }
            '#' -> {
                // 90-degree rotation
                dir = Point(-1 * dir.y, dir.x)
            }
            else -> {
                println("Unexpected value $nextSquare")
                throw IllegalStateException()
            }
        }

        if (visitedDirs[guard.x][guard.y].contains(dir)) {
            // We've been here before
            throw LoopException()
        } else {
            visitedDirs[guard.x][guard.y].addLast(dir)
        }

        return newBoard
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    private val board: Board = Board(fileReader.readFileLines(filename), true)
    private val newBoards: MutableList<Board> = mutableListOf()
    private var possibleLoops = 0

    init {
        while (true) {
            try {
                // Simulate the primary board, and create possible loop-creating new boards
                val newBoard = board.moveOne()
                if (newBoard != null) {
                    newBoards.addLast(newBoard)
                }
            } catch (outOfBounds: OutOfBoundsException) {
                break
            }
        }
        for (newBoard in newBoards) {
            while (true) {
                try {
                    newBoard.moveOne()
                } catch (outOfBounds: OutOfBoundsException) {
                    break
                } catch (loops: LoopException) {
                    possibleLoops++
                    break
                }
            }
        }
    }

    fun countPositions(): Int {
        return board.count
    }

    fun countLoops(): Int {
        return possibleLoops
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day6.txt")
    println(inputReader.countPositions())
    println(inputReader.countLoops())
    println(sw.stop().elapsed())
}
