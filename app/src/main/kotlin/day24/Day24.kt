package day24

import com.google.common.base.Stopwatch
import common.*
import kotlin.IllegalArgumentException

enum class Role {
    INPUT_X,
    INPUT_Y,
    ADD,
    REM,
    HALF_CARRY,
    VAL_Z,
    CARRY,
    UNKNOWN
}

data class Purpose(val role: Role, val rank: Int)

abstract class Wire(val id: String) {
    abstract fun eval(wires: Map<String, Wire>): Long
    abstract fun purpose(wires: Map<String, Wire>): Purpose
}

class InputWire(id: String, private val input: Long): Wire(id) {
    override fun eval(wires: Map<String, Wire>): Long {
        return input
    }

    override fun purpose(wires: Map<String, Wire>): Purpose {
        val role = when (id[0]) {
            'x' -> Role.INPUT_X
            'y' -> Role.INPUT_Y
            else -> throw IllegalArgumentException()
        }
        val rank = id.substring(1).toInt()
        return Purpose(role, rank)
    }
}

class GateWire(id: String, private val w1: String, private val w2: String, val op: String): Wire(id) {
    var output: Long = -1L
    var purpose: Purpose? = null

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

    override fun purpose(wires: Map<String, Wire>): Purpose {
        if (purpose != null) {
            return purpose!!
        } else {
            purpose = calculatePurpose(wires)
            return purpose!!
        }
    }

    fun calculatePurpose(wires: Map<String, Wire>): Purpose {
        val parents = listOf(wires[w1]!!.purpose(wires), wires[w2]!!.purpose(wires)).sortedBy { it.role }
        val rank = parents.maxOf { it.rank }

        if (parents[0].role == Role.INPUT_X) {
            if (parents[1].role != Role.INPUT_Y) {
                println("WARNING: Role mismatch at $id [${parents[0]}, ${parents[1]}]")
                return Purpose(Role.UNKNOWN, rank)
            }
            if (parents[0].rank != parents[1].rank) {
                println("WARNING: Rank mismatch at $id [${parents[0]}, ${parents[1]}]")
            }
            val role = when (op) {
                "XOR" -> Role.ADD
                "AND" -> Role.REM
                else -> throw IllegalArgumentException() // pretty sure this never happens
            }
            return Purpose(role, rank)
        }

        if (parents[0].role == Role.INPUT_Y) {
            // we know it isn't INPUT_X because we sorted the roles
            println("WARNING: Role mismatch at $id [${parents[0]}, ${parents[1]}]")
            return Purpose(Role.UNKNOWN, rank)
        }

        if (parents[0].role == Role.ADD) {
            if (parents[1].role != Role.CARRY && parents[1].role != Role.UNKNOWN) {
                println("WARNING: Role mismatch at $id [${parents[0]}, ${parents[1]}]")
                return Purpose(Role.UNKNOWN, rank)
            }
            if (parents[0].rank != parents[1].rank + 1) {
                println("WARNING: Rank mismatch at $id [${parents[0]}, ${parents[1]} + 1]")
            }
            val role = when (op) {
                "XOR" -> Role.VAL_Z
                "AND" -> Role.HALF_CARRY
                else -> {
                    println("WARNING: Operation mismatch at $id [$op; ${parents[0]}, ${parents[1]}]")
                    Role.UNKNOWN
                }
            }
            return Purpose(role, rank)
        }

        if (parents[0].role == Role.REM) {
            if (parents[1].role != Role.HALF_CARRY && parents[1].role != Role.UNKNOWN) {
                println("WARNING: Role mismatch at $id [${parents[0]}, ${parents[1]}]")
                return Purpose(Role.UNKNOWN, rank)
            }
            if (parents[0].rank != parents[1].rank) {
                println("WARNING: Rank mismatch at $id [${parents[0]}, ${parents[1]}]")
            }
            val role = when (op) {
                "OR" -> Role.CARRY
                else -> {
                    println("WARNING: Operation mismatch at $id [$op; ${parents[0]}, ${parents[1]}]")
                    Role.UNKNOWN
                }
            }
            return Purpose(role, rank)
        }

        if (parents[0].role == Role.HALF_CARRY) {
            if (parents[1].role != Role.UNKNOWN) {
                println("WARNING: Role mismatch at $id [${parents[0]}, ${parents[1]}]")
                return Purpose(Role.UNKNOWN, rank)
            }
            if (parents[0].rank != parents[1].rank) {
                println("WARNING: Rank mismatch at $id [${parents[0]}, ${parents[1]}]")
            }
            val role = when (op) {
                "OR" -> Role.CARRY
                else -> {
                    println("WARNING: Operation mismatch at $id [$op; ${parents[0]}, ${parents[1]}]")
                    Role.UNKNOWN
                }
            }
            return Purpose(role, rank)
        }

        if (parents[0].role == Role.VAL_Z) {
            throw IllegalArgumentException() // pretty sure this never happens
        }

        if (parents[0].role == Role.CARRY) {
            // we know it isn't ADD because we sorted the roles
            if (parents[1].role != Role.UNKNOWN) {
                println("WARNING: Role mismatch at $id [${parents[0]}, ${parents[1]}]")
                return Purpose(Role.UNKNOWN, rank)
            }
            if (parents[0].rank != parents[1].rank - 1) {
                println("WARNING: Rank mismatch at $id [${parents[0]}, ${parents[1]} - 1]")
            }
            val role = when (op) {
                "XOR" -> Role.VAL_Z
                "AND" -> Role.HALF_CARRY
                else -> {
                    println("WARNING: Operation mismatch at $id [$op; ${parents[0]}, ${parents[1]}]")
                    Role.UNKNOWN
                }
            }
            return Purpose(role, rank)
        }

        throw IllegalArgumentException()
    }

    fun graphviz(): String {
        val sb = StringBuilder()
        sb.append("  ${op}_$id [label=$op]\n")
        sb.append("  { $w1 $w2 } -> ${op}_$id;\n")
        sb.append("  ${op}_$id -> $id;\n")
        return sb.toString()
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

    fun printGraphviz() {
        println("digraph {")

        // lock all x to the same rank
        print("  { rank=same; ")
        print(wires.keys.filter { it.startsWith('x') }.joinToString())
        println(" }")
        // lock all y to the same rank
        print("  { rank=same; ")
        print(wires.keys.filter { it.startsWith('y') }.joinToString())
        println(" }")
        // lock all z to the same rank
        print("  { rank=same; ")
        print(wires.keys.filter { it.startsWith('z') }.joinToString())
        println(" }")
        println("")

        // create nodes for each wire
        for (wire in wires.values.filterIsInstance<GateWire>()) {
            print(wire.graphviz())
        }

        println("}")
    }

    fun findAnomalies() {
        for (wire in wires.values.filter { it.id.startsWith('z') }) {
            println("Testing ${wire.id}")
            val purpose = wire.purpose(wires)
            if (purpose.role != Role.VAL_Z) {
                println("WARNING: expected VAL_Z, got ${purpose.role}")
            }
        }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day24.txt")
    println(inputReader.zValues())
    // inputReader.printGraphviz()
    inputReader.findAnomalies()
    /*
     * Swaps:
     * cmv with z17
     * rmj with z23
     * rdg with z30
     * mwp with btb
     * btb,cmv,mwp,rdg,rmj,z17,z23,z30
     *
     * Testing z27
     * WARNING: Role mismatch at jjv [Purpose(role=ADD, rank=1), Purpose(role=REM, rank=0)]
     * OK -- REM(0) is the same as CARRY(0)
     * WARNING: Role mismatch at wbw [Purpose(role=ADD, rank=18), Purpose(role=VAL_Z, rank=17)]
     * SWAP -- cmv with z17
     * WARNING: Role mismatch at pkh [Purpose(role=REM, rank=23), Purpose(role=VAL_Z, rank=23)]
     * SWAP -- rmj with z23
     * Testing z36
     * WARNING: Role mismatch at nmr [Purpose(role=HALF_CARRY, rank=30), Purpose(role=VAL_Z, rank=30)]
     * SWAP -- rdg with z30
     * Testing z44
     * WARNING: Role mismatch at jhw [Purpose(role=REM, rank=38), Purpose(role=CARRY, rank=37)]
     * SWAP -- mwp with btb (jhw should be HALF_CARRY(38))
     * WARNING: Rank mismatch at dqg [Purpose(role=ADD, rank=38), Purpose(role=UNKNOWN, rank=38) + 1]
     * SWAP -- mwp with btb (dqg should be CARRY(38))
     * WARNING: Operation mismatch at dqg [OR; Purpose(role=ADD, rank=38), Purpose(role=UNKNOWN, rank=38)]
     * Ditto
     * Testing z38
     * WARNING: Role mismatch at z38 [Purpose(role=REM, rank=38), Purpose(role=CARRY, rank=37)]
     * WARNING: expected VAL_Z, got UNKNOWN
     * Testing z01
     * WARNING: Role mismatch at z01 [Purpose(role=ADD, rank=1), Purpose(role=REM, rank=0)]
     * WARNING: expected VAL_Z, got UNKNOWN
     * Testing z45
     * WARNING: expected VAL_Z, got CARRY
     * Testing z23
     * WARNING: expected VAL_Z, got HALF_CARRY
     * SWAP -- rmj with z23
     * Testing z00
     * WARNING: expected VAL_Z, got ADD
     * Testing z17
     * WARNING: expected VAL_Z, got CARRY
     * SWAP -- cmv with z17
     * Testing z30
     * WARNING: expected VAL_Z, got REM
     * SWAP -- rdg with z30
     * Testing z18
     * WARNING: Role mismatch at z18 [Purpose(role=ADD, rank=18), Purpose(role=VAL_Z, rank=17)]
     * WARNING: expected VAL_Z, got UNKNOWN
     */
    println(sw.stop().elapsed())
}
