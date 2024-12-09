package day2

import common.FileReader
import kotlin.math.abs

class Report(line: String) {
    private val list: List<Int> = line.split(' ').map { it.toInt() }

    fun isSafe(): Boolean {
        return isSafe(list)
    }

    private fun isSafe(levels: List<Int>): Boolean {
        val sign = levels[1] - levels[0]
        for (i in 1..<levels.size) {
            val diff = levels[i] - levels[i-1]
            if (diff * sign <= 0 || abs(diff) > 3) {
                return false
            }
        }
        return true
    }

    fun isSafeWithDampener(): Boolean {
        if (isSafe()) {
            return true
        }

        for (i in list.indices) {
            val newList = list.toMutableList()
            newList.removeAt(i)
            if (isSafe(newList)) {
                return true
            }
        }
        return false
    }
}
class ReportReader(fileReader: FileReader, filename: String) {

    private val reports: List<Report>

    init {
        val lines = fileReader.readFile(filename)

        reports = lines.map { Report(it) }
    }

    fun countSafe(): Int {
        return reports.count { it.isSafe() }
    }

    fun countSafeWithDampener(): Int {
        return reports.count { it.isSafeWithDampener() }
    }
}

fun main() {
    val reportReader = ReportReader(FileReader(), "day2.txt")
    println(reportReader.countSafe())
    println(reportReader.countSafeWithDampener())
}
