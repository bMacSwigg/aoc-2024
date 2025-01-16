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

data class Sequence(val a: Int, val b: Int, val c: Int, val d: Int) {

    fun shift(next: Int): Sequence {
        return Sequence(b, c, d, next)
    }
}

class Monkey(val initial: Long) {
    val pricesBySeq: Map<Sequence, Int>

    init {
        val mapBuilder = mutableMapOf<Sequence, Int>()

        val sec1 = generateNext(initial)
        val sec2 = generateNext(sec1)
        val sec3 = generateNext(sec2)
        val sec4 = generateNext(sec3)
        val a = sec1.mod(10)
        val b = sec2.mod(10)
        val c = sec3.mod(10)
        val d = sec4.mod(10)
        var sequence = Sequence(a - initial.mod(10), b - a, c - b, d - c)
        mapBuilder[sequence] = d

        var prev = sec4
        var prevPrice = d
        for (i in 5..2000) {
            val next = generateNext(prev)
            val price = next.mod(10)
            val diff = price - prevPrice
            sequence = sequence.shift(diff)
            if (!mapBuilder.containsKey(sequence)) {
                // only insert if it's new: we can only sell at the _first_ occurence of a sequence
                mapBuilder[sequence] = price
            }
            prev = next
            prevPrice = price
        }

        pricesBySeq = mapBuilder
    }
}

class MonkeyMarket() {
    val totalPriceBySequence = mutableMapOf<Sequence, Int>()

    fun add(monkey: Monkey) {
        for (entry in monkey.pricesBySeq) {
            val totalSoFar = totalPriceBySequence.getOrDefault(entry.key, 0)
            totalPriceBySequence[entry.key] = totalSoFar + entry.value
        }
    }

    fun max(): Int {
        return totalPriceBySequence.maxOf { it.value }
    }
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

    fun findMax(): Int {
        val mm = MonkeyMarket()
        initialValues.map { Monkey(it) }.forEach { mm.add(it) }
        return mm.max()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day22.txt")
    println(inputReader.sum2000steps())
    println(inputReader.findMax())
    println(sw.stop().elapsed())
}
