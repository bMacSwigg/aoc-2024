package day24

import com.google.common.base.Stopwatch
import common.*
import java.lang.IllegalArgumentException

abstract class Wire(val id: String) {
    abstract fun eval(wires: Map<String, Wire>): Long
}

class InputWire(id: String, private val input: Long): Wire(id) {
    override fun eval(wires: Map<String, Wire>): Long {
        return input
    }
}

class GateWire(id: String, private val w1: String, private val w2: String, private val op: String): Wire(id) {
    var output: Long = -1L
    override fun eval(wires: Map<String, Wire>): Long {
        if (output != -1L) {
            return output
        }

        val left = wires[w1]!!.eval(wires)
        val right = wires[w2]!!.eval(wires)
        output = when (op) {
            "AND" -> left and right
            "OR" -> left or right
            "XOR" -> left xor right
            else -> throw IllegalArgumentException()
        }
        return output
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    companion object {
        val WIRE_PATTERN = Regex("""^(?<id>\w{3}): (?<val>[1|0])$""")
        val GATE_PATTERN = Regex("""^(?<id1>\w{3}) (?<op>AND|XOR|OR) (?<id2>\w{3}) -> (?<id3>\w{3})$""")

        fun parseInput(match: MatchResult): InputWire {
            val id = match.groups["id"]!!.value
            val input = match.groups["val"]!!.value.toLong()
            return InputWire(id, input)
        }

        fun parseGate(match: MatchResult): GateWire {
            val id = match.groups["id3"]!!.value
            val left = match.groups["id1"]!!.value
            val right = match.groups["id2"]!!.value
            val op = match.groups["op"]!!.value
            return GateWire(id, left, right, op)
        }
    }

    private val lines = fileReader.readFileLines(filename)
    private val wires: Map<String, Wire>

    init {
        val inputWires = lines.mapNotNull { WIRE_PATTERN.find(it) }.map { parseInput(it) }
        val gateWires = lines.mapNotNull { GATE_PATTERN.find(it) }.map { parseGate(it) }
        wires = inputWires.plus(gateWires).associateBy { it.id }
    }

    fun zValues(): Long {
        val zWires = wires.keys.filter { it.startsWith('z') }.sortedDescending()
        var sum = 0L
        for (z in zWires) {
            val output = wires[z]!!.eval(wires)
            sum = sum shl 1
            sum += output
        }
        return sum
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day24.txt")
    println(inputReader.zValues())
    println(sw.stop().elapsed())
}
