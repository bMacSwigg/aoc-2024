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

class InputReader(fileReader: FileReader, filename: String) {

    companion object {
        val ROBOT_REGEX = Regex("""p=(?<px>\d+),(?<py>\d+) v=(?<vx>-?\d+),(?<vy>-?\d+)""")
    }

    private val lines = fileReader.readFileLines(filename)
    val warehouse = Warehouse(lines.subList(0, lines.indexOf("")).map { it.toCharArray() }.toTypedArray())
    val commands = lines.subList(lines.indexOf("")+1, lines.size).flatMap { it.toCharArray().toList() }

    fun runCommands() {
        commands.forEach { warehouse.move(it) }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day15.txt")
    inputReader.runCommands()
    println(inputReader.warehouse.sumGps())
    println(sw.stop().elapsed())
}
