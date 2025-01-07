package day17

import com.google.common.base.Stopwatch
import common.FileReader
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.RuntimeException


class OutputMismatchException: RuntimeException()

class Computer(private var regA: Long,
               private var regB: Long,
               private var regC: Long,
               private val instructions: List<Int>,
               targetOutput: List<Int>? = null
    ) {
    private var ptr = 0
    private val output = mutableListOf<Int>()
    private val targetOutput = targetOutput?.toMutableList()

    fun run() {
        while (ptr < instructions.size-1) {
            val opcode = instructions[ptr]
            val operand = instructions[ptr+1]
            when (opcode) {
                0 -> adv(operand)
                1 -> bxl(operand)
                2 -> bst(operand)
                3 -> jnz(operand)
                4 -> bxc()
                5 -> out(operand)
                6 -> bdv(operand)
                7 -> cdv(operand)
                else -> throw IllegalArgumentException()
            }
        }
        if (!targetOutput.isNullOrEmpty()) {
            throw OutputMismatchException()
        }
    }

    fun getOutput(): String {
        return output.joinToString(",")
    }

    private fun comboOperand(operand: Int): Long {
        return when (operand) {
            0, 1, 2, 3 -> operand.toLong()
            4 -> regA
            5 -> regB
            6 -> regC
            else -> throw IllegalArgumentException()
        }
    }

    private fun adv(operand: Int) {
        regA = regA shr comboOperand(operand).toInt()
        ptr += 2
    }

    private fun bxl(operand: Int) {
        regB = regB xor operand.toLong()
        ptr += 2
    }

    private fun bst(operand: Int) {
        regB = comboOperand(operand).mod(8).toLong()
        ptr += 2
    }

    private fun jnz(operand: Int) {
        if (regA == 0L) {
            ptr += 2
        } else {
            ptr = operand
        }
    }

    private fun bxc() {
        regB = regB xor regC
        ptr += 2
    }

    private fun out(operand: Int) {
        val o = comboOperand(operand).mod(8)
        if (targetOutput != null && targetOutput.removeFirst() != o) {
            throw OutputMismatchException()
        }
        output.add(o)
        ptr += 2
    }

    private fun bdv(operand: Int) {
        regB = regA shr comboOperand(operand).toInt()
        ptr += 2
    }

    private fun cdv(operand: Int) {
        regC = regA shr comboOperand(operand).toInt()
        ptr += 2
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    companion object {
        val REG_A = Regex("""Register A: (\d*)""")
        val REG_B = Regex("""Register B: (\d*)""")
        val REG_C = Regex("""Register C: (\d*)""")
        val PROGRAM = Regex("""Program: ((?:\d,)*\d)""")
    }
    private val text = fileReader.readFileText(filename)
    private val regA = REG_A.find(text)!!.groupValues[1].toLong()
    private val regB = REG_B.find(text)!!.groupValues[1].toLong()
    private val regC = REG_C.find(text)!!.groupValues[1].toLong()
    private val program = PROGRAM.find(text)!!.groupValues[1].split(',').map { it.toInt() }

    val computer = Computer(regA, regB, regC, program)

    fun troubleshootRegA(start: Long = 1, end: Long = Long.MAX_VALUE): Long {
        for (a in start..end) {
            val attempt = Computer(a, regB, regC, program, program)
            try {
                attempt.run()
                return a
            } catch (e: OutputMismatchException) {
                continue
            }
        }
        return -1
        // throw IllegalStateException("Impossible: no matching value for A within range")
    }

    fun smarter(target: List<Int>, start: Long = 1, end: Long = 7): List<Long> {
        val possibles = mutableListOf<Long>()
        for (a in start..end) {
            val attempt = Computer(a, regB, regC, program, target)
            try {
                attempt.run()
                possibles.add(a)
            } catch (e: OutputMismatchException) {
                continue
            }
        }
        if (possibles.isNotEmpty()) {
            return possibles
        } else {
            throw IllegalStateException("Impossible: no matching value for A within range")
        }
    }

    fun smarter(): Long {
        var possibles = listOf(0L)
        for (i in 1..program.size) {
            println(possibles.size)
            println(program.subList(program.size-i, program.size))
            possibles = possibles.map { it shl 3 }
            possibles = possibles.flatMap {
                try {
                    smarter(program.subList(program.size-i, program.size), it, it+7)
                } catch (e: IllegalStateException) {
                    listOf()
                }
            }
        }
        return possibles.min()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day17.txt")
    inputReader.computer.run()
    println(inputReader.computer.getOutput())
    println(inputReader.smarter())

    val comp = Computer(105706277661082, 0, 0, listOf(2,4,1,5,7,5,1,6,0,3,4,3,5,5,3,0))
    comp.run()
    println(comp.getOutput())
    println(sw.stop().elapsed())
}

// Program: 2,4,1,5,7,5,1,6,0,3,4,3,5,5,3,0
// B = A%8
// B = B xor 5 -> lowest 3 bits of A xor 101: largest possible value is 7
// C = A >> B : drop as much as last 7 bits of A
// B = B xor 6 (110) -> lowest 3 bits of A xor 011
// A = A >> 3 -> drop the lowest 3 bits
// B = B xor C
// out B%8 -> lowest 3 bits of B
// jnz 0
// SO most importantly: B and C are set from A on every iter, independent of their previous values