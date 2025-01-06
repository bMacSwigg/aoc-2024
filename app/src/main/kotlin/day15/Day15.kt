package day15

import com.google.common.base.Stopwatch
import common.FileReader
import common.Point
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


class Warehouse(private val board: Array<CharArray>) {

    private val height = board.size
    private val width = board[0].size
    private var robot: Point = Point(-1, -1)

    init {
        outer@ for (y in 0..<height) {
            for (x in 0..<width) {
                if (board[y][x] == '@') {
                    robot = Point(x, y)
                    break@outer
                }
            }
        }
    }

    private fun get(p: Point): Char {
        return board[p.y][p.x]
    }

    private fun set(p: Point, c: Char) {
        board[p.y][p.x] = c
    }

    fun move(cmd: Char) {
        val delta: Point = when(cmd) {
            '<' -> Point(-1, 0)
            '^' -> Point(0, -1)
            '>' -> Point(1, 0)
            'v' -> Point(0, 1)
            else -> throw IllegalArgumentException()
        }
        var next = robot + delta
        while (get(next) == 'O') { next += delta }
        if (get(next) == '.') {
            set(next, 'O')
            set(robot, '.')
            robot += delta
            set(robot, '@')
        } else if (get(next) == '#') {
            return
        } else {
            throw IllegalStateException()
        }
    }

    fun sumGps(): Long {
        var total = 0L
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (board[y][x] == 'O') {
                    total += 100 * y + x
                }
            }
        }
        return total
    }

    override fun toString(): String {
        return board.joinToString("\n") { it.joinToString("") }
    }
}

class BiggerWarehouse(inputBoard: Array<CharArray>) {

    private val height = inputBoard.size
    private val width = inputBoard[0].size * 2 // everything is double-width
    private var board = Array(height) { CharArray(width) }
    private var robot: Point = Point(-1, -1)

    init {
        outer@ for (y in 0..<height) {
            for (x in 0..<width/2) {
                val input = inputBoard[y][x]
                when (input) {
                    '#' -> {
                        board[y][x*2] = '#'
                        board[y][x*2+1] = '#'
                    }
                    '.' -> {
                        board[y][x*2] = '.'
                        board[y][x*2+1] = '.'
                    }
                    'O' -> {
                        board[y][x*2] = '['
                        board[y][x*2+1] = ']'
                    }
                    '@' -> {
                        board[y][x*2] = '@'
                        board[y][x*2+1] = '.'
                        robot = Point(x*2, y)
                    }
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }

    private fun get(p: Point): Char {
        return board[p.y][p.x]
    }

    private fun set(b: Array<CharArray>, p: Point, c: Char) {
        b[p.y][p.x] = c
    }

    fun move(cmd: Char) {
        val delta: Point = when(cmd) {
            '<' -> Point(-1, 0)
            '^' -> Point(0, -1)
            '>' -> Point(1, 0)
            'v' -> Point(0, 1)
            else -> throw IllegalArgumentException()
        }
        if (delta.y == 0) {
            horizontalMove(delta)
        } else {
            verticalMove(delta)
        }
    }

    // Simpler, because there can't be overlap between boxes
    private fun horizontalMove(delta: Point) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        set(newBoard, robot, '.')
        var next = robot + delta
        while (get(next) == '[' || get(next) == ']') {
            set(newBoard, next, get(next-delta))
            next += delta
        }
        if (get(next) == '.') {
            set(newBoard, next, get(next-delta))
            board = newBoard
            robot += delta
            return
        } else if (get(next) == '#') {
            return
        } else {
            throw IllegalStateException()
        }
    }

    private fun verticalMove(delta: Point) {
        if (push(setOf(robot), delta)) {
            robot += delta
        }
    }

    private fun push(points: Set<Point>, delta: Point): Boolean {
        if (points.isEmpty()) { return true }

        val newPoints = points.map { it+delta }
        val impossible = newPoints.map { get(it) }.any { it == '#' }
        if (impossible) {
            return false
        }

        val pushedPoints = newPoints.filter { get(it) == '[' || get(it) == ']' }.toMutableSet()
        for (p in newPoints) {
            when (get(p)) {
                '[' -> pushedPoints.add(p + Point(1, 0))
                ']' -> pushedPoints.add(p + Point(-1, 0))
            }
        }

        val success = push(pushedPoints, delta)
        if (!success) {
            return false
        }

        points.forEach {
            set(board, it+delta, get(it))
            set(board, it, '.')
        }
        return true
    }

    fun sumGps(): Long {
        var total = 0L
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (board[y][x] == '[') {
                    total += 100 * y + x
                }
            }
        }
        return total
    }

    override fun toString(): String {
        return board.joinToString("\n") { it.joinToString("") }
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    companion object {
        val ROBOT_REGEX = Regex("""p=(?<px>\d+),(?<py>\d+) v=(?<vx>-?\d+),(?<vy>-?\d+)""")
    }

    private val lines = fileReader.readFileLines(filename)
    val board = lines.subList(0, lines.indexOf("")).map { it.toCharArray() }.toTypedArray()
    val warehouse = Warehouse(board)
    val biggerWarehouse = BiggerWarehouse(board)
    val commands = lines.subList(lines.indexOf("")+1, lines.size).flatMap { it.toCharArray().toList() }

    fun runCommands() {
        commands.forEach { warehouse.move(it) }
    }

    fun runBiggerCommands() {
        commands.forEach { biggerWarehouse.move(it) }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day15.txt")
    inputReader.runCommands()
    println(inputReader.warehouse.sumGps())

    inputReader.runBiggerCommands()
    println(inputReader.biggerWarehouse.sumGps())
    println(sw.stop().elapsed())
}
