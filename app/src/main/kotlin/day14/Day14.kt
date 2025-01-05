package day14

import com.google.common.base.Stopwatch
import common.FileReader


class Robot(var pos: Pair<Long, Long>, val vel: Pair<Long, Long>, private val dims: Pair<Long, Long>) {

    fun step(n: Int) {
        val x = (pos.first + (vel.first * n)).mod(dims.first)
        val y = (pos.second + (vel.second * n)).mod(dims.second)
        pos = Pair(x, y)
    }
}

class RobotsReader(fileReader: FileReader, filename: String, private val dims: Pair<Long, Long>) {

    companion object {
        val ROBOT_REGEX = Regex("""p=(?<px>\d+),(?<py>\d+) v=(?<vx>-?\d+),(?<vy>-?\d+)""")

        fun parseMatchResult(match: MatchResult, dims: Pair<Long, Long>): Robot {
            val px = match.groups["px"]!!.value.toLong()
            val py = match.groups["py"]!!.value.toLong()
            val vx = match.groups["vx"]!!.value.toLong()
            val vy = match.groups["vy"]!!.value.toLong()
            return Robot(Pair(px, py), Pair(vx, vy), dims)
        }
    }

    private val lines = fileReader.readFileLines(filename)
    private val robots = lines.map{ ROBOT_REGEX.find(it) }.map { parseMatchResult(it!!, dims) }.toList()

    fun step(n: Int) {
        robots.forEach { it.step(n) }
    }

    fun calculateSafetyFactor(): Long {
        var q1 = 0L
        var q2 = 0L
        var q3 = 0L
        var q4 = 0L
        robots.forEach {
            if (it.pos.first < dims.first / 2) {
                if (it.pos.second < dims.second / 2) {
                    q1++
                } else if (it.pos.second > dims.second / 2) {
                    q2++
                }
            } else if (it.pos.first > dims.first / 2) {
                if (it.pos.second < dims.second / 2) {
                    q3++
                } else if (it.pos.second > dims.second / 2) {
                    q4++
                }
            }
        }
        return q1 * q2 * q3 * q4
    }

    fun visualize(n: Int, start: Int = 0) {
        step(start)
        println(this)
        for (i in 1..n) {
            Thread.sleep(1000)
            step(1)
            println("VISUALIZING AT ${start+i} SECONDS")
            println(this)
        }
    }

    private fun generateRoom(): Array<IntArray> {
        val room = Array(dims.second.toInt()) { IntArray(dims.first.toInt()) { 0 }}
        for (r in robots) {
            room[r.pos.second.toInt()][r.pos.first.toInt()]++
        }
        return room
    }

    private fun isAnomalous(): Boolean {
        val room = generateRoom()
        for (y in 1..(dims.second-2).toInt()) {
            for (x in 1..(dims.first-2).toInt()) {
                if (room[y-1][x-1] == 1 &&
                    room[y-1][x] == 1 &&
                    room[y-1][x+1] == 1 &&
                    room[y][x-1] == 1 &&
                    room[y][x] == 1 &&
                    room[y][x+1] == 1 &&
                    room[y+1][x-1] == 1 &&
                    room[y+1][x] == 1 &&
                    room[y+1][x+1] == 1) {
                    return true
                }
            }
        }
        return false
    }

    fun printAnomalies(n: Int, start: Int = 0, offset: Int = 0) {
        step(offset)
        for (i in 1..n) {
            step(1)
            if (isAnomalous()) {
                println("ANOMALY AT TIME ${i+start+offset}")
                println(this)
            }
        }
        println("Checked up through time ${n+start+offset}")
    }

    override fun toString(): String {
        return generateRoom().joinToString("\n") { it.joinToString("") { if (it == 0) "." else it.toString() } }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val robotsReader = RobotsReader(FileReader(), "day14.txt", Pair(101L, 103L))
    robotsReader.step(100)
    println(robotsReader.calculateSafetyFactor())
    robotsReader.printAnomalies(10000, 100, 2500)
    println(sw.stop().elapsed())
}
