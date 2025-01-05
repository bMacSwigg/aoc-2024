package day13

import com.google.common.base.Stopwatch
import common.FileReader


class Equation(private val matrix: Matrix, private val prize: Pair<Int, Int>) {

    fun solution(): Pair<Int, Int> {
        // println(this)

        if (matrix.determinant() == Rational.ZERO) {
            // no inverse, therefore vectors are colinear
            // either multiple solutions, or no solution
            println("WARNING: Gotta deal with this")
            return Pair(0, 0)
        } else {
            val result = matrix.inverse().timesIntVector(prize)
            if (result.first.isInt() && result.second.isInt()) {
                // solution is unique
                // println("SOLUTION: $result")
                return Pair(result.first.num.toInt(), result.second.num.toInt())
            }
            // not an integer solution, so not possible
            // println("NO SOLUTION")
            return Pair(0, 0)
        }
    }

    fun bruteForce(): Pair<Int, Int> {
        for (a in 1..100) {
            for (b in 1..100) {
                val res = matrix.timesIntVector(Pair(a, b))
                if (res.first.num.toInt() == prize.first && res.second.num.toInt() == prize.second) {
                    return Pair(a, b)
                }
            }
        }
        return Pair(0, 0)
    }

    override fun toString(): String {
        val line1 = "Button A: X+${matrix.a}, Y+${matrix.c}"
        val line2 = "Button B: X+${matrix.b}, Y+${matrix.d}"
        val line3 = "Prize: X=${prize.first}, Y=${prize.second}"
        return "$line1\n$line2\n$line3"
    }
}

class EquationsReader(fileReader: FileReader, filename: String) {

    companion object {
        private const val BUTTON_A_PATTERN = """Button A: X\+(?<ax>\d*), Y\+(?<ay>\d*)"""
        private const val BUTTON_B_PATTERN = """Button B: X\+(?<bx>\d*), Y\+(?<by>\d*)"""
        private const val PRIZE_PATTERN = """Prize: X=(?<px>\d*), Y=(?<py>\d*)"""
        val MACHINE_REGEX = Regex("$BUTTON_A_PATTERN\\n$BUTTON_B_PATTERN\\n$PRIZE_PATTERN")

        fun parseMatchResult(match: MatchResult): Equation {
            val ax = match.groups["ax"]!!.value.toInt()
            val ay = match.groups["ay"]!!.value.toInt()
            val bx = match.groups["bx"]!!.value.toInt()
            val by = match.groups["by"]!!.value.toInt()
            val px = match.groups["px"]!!.value.toInt()
            val py = match.groups["py"]!!.value.toInt()
            val mat = Matrix(Rational.ofInt(ax), Rational.ofInt(bx), Rational.ofInt(ay), Rational.ofInt(by))
            val prize = Pair(px, py)
            return Equation(mat, prize)
        }
    }

    private val text = fileReader.readFileText(filename)
    private val equations = MACHINE_REGEX.findAll(text).map { parseMatchResult(it) }.toList()

    fun sumSolutions(): Long {
        return equations.map { it.solution() }.sumOf { it.first * 3L + it.second }
    }

    fun sumSolutionsBruteForce(): Long {
        return equations.map { it.bruteForce() }.sumOf { it.first * 3L + it.second }
    }

    fun checkMismatch() {
        equations.forEach {
            val solution = it.solution()
            val bruteForce = it.bruteForce()
            if (solution != bruteForce) {
                println(it)
                println("MISMATCH: $solution; $bruteForce")
            }
        }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val equationsReader = EquationsReader(FileReader(), "day13.txt")
    println("\nTOTAL: ${equationsReader.sumSolutions()}")
    println("\nTOTAL: ${equationsReader.sumSolutionsBruteForce()}")
    equationsReader.checkMismatch()
    println(sw.stop().elapsed())
}
