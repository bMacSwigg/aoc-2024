package day17

import com.google.common.base.Stopwatch
import common.FileReader
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.RuntimeException


class OutputMismatchException: RuntimeException()

class Computer(private var regA: Int,
               private var regB: Int,
               private var regC: Int,
               val instructions: List<Int>,
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

    private fun comboOperand(operand: Int): Int {
        return when (operand) {
            0, 1, 2, 3 -> operand
            4 -> regA
            5 -> regB
            6 -> regC
            else -> throw IllegalArgumentException()
        }
    }

    private fun adv(operand: Int) {
        val den = 1L shl comboOperand(operand)
        regA = (regA / den).toInt()
        ptr += 2
    }

    private fun bxl(operand: Int) {
        regB = regB xor operand
        ptr += 2
    }

    private fun bst(operand: Int) {
        regB = comboOperand(operand).mod(8)
        ptr += 2
    }

    private fun jnz(operand: Int) {
        if (regA == 0) {
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
        val den = 1L shl comboOperand(operand)
        regB = (regA / den).toInt()
        ptr += 2
    }

    private fun cdv(operand: Int) {
        val den = 1L shl comboOperand(operand)
        regC = (regA / den).toInt()
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
    private val regA = REG_A.find(text)!!.groupValues[1].toInt()
    private val regB = REG_B.find(text)!!.groupValues[1].toInt()
    private val regC = REG_C.find(text)!!.groupValues[1].toInt()
    private val program = PROGRAM.find(text)!!.groupValues[1].split(',').map { it.toInt() }

    val computer = Computer(regA, regB, regC, program)

    fun troubleshootRegA(start: Int = 1, end: Int = Int.MAX_VALUE): Int {
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
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day17.txt")
    inputReader.computer.run()
    println(inputReader.computer.getOutput())
    println(inputReader.troubleshootRegA(2_000_000_000))
    println(sw.stop().elapsed())
}
