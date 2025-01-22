package day1

import com.google.common.base.Stopwatch
import common.FileReader
import kotlin.math.abs

class ListReader(fileReader: FileReader, filename: String) {

    private val list1: MutableList<Int>
    private val list2: MutableList<Int>

    init {
        val lines = fileReader.readFile(filename)

        list1 = mutableListOf<Int>()
        list2 = mutableListOf<Int>()
        lines.forEach {
            val nums = it.split("\\s+".toRegex(), limit=2)
            list1.addLast(nums[0].toInt())
            list2.addLast(nums[1].toInt())
        }

        list1.sort()
        list2.sort()
    }

    fun calculateDiff(): Int {
        return list1.zip(list2).fold(0) { acc, pair ->
            acc + abs(pair.first - pair.second)
        }
    }

    fun calculateSimilarity(): Int {
        return list1.fold(0) { acc, num ->
            // This could certainly be more efficient, eg make a frequency map of list2
            // But -- it doesn't need to be. This runs fast enough as-is.
            acc + (num * list2.count { it == num})
        }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val listReader = ListReader(FileReader(), "day1.txt")
    println(listReader.calculateDiff())
    println(listReader.calculateSimilarity())
    println(sw.stop().elapsed())
}
