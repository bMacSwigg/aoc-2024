package day22

import com.google.common.base.Stopwatch
import common.*

fun generateNext(secret: Long): Long {
    // step 1
    var new = prune(mix(secret shl 6, secret))

    // step 2
    new = prune(mix(new shr 5, new))

    // step 3
    new = prune(mix(new shl 11, new))

    return new
}

fun mix(a: Long, b: Long): Long {
    return a.xor(b)
}

fun prune(a: Long): Long {
    // this is 2^24, so could also do:
    // a - ((a >> 24) << 24)
    return a.mod(16777216L)
}

class InputReader(fileReader: FileReader, filename: String) {
    private val initialValues = fileReader.readFileLines(filename).map { it.toLong() }

    fun sum2000steps(): Long {
        var sum = 0L
        for (value in initialValues) {
            var secret = value
            for (i in 1..2000) {
                secret = generateNext(secret)
            }
            sum += secret
        }
        return sum
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day22.txt")
    println(inputReader.sum2000steps())
    println(sw.stop().elapsed())
}
