package day7

import com.google.common.base.Stopwatch
import common.FileReader

class Equation(
    val result: Long,
    private val operands: List<Long>,
    ) {

    private fun isPossible(target: Long, operands: List<Long>): Boolean {
        if (operands.size == 1) {
            return operands[0] == target
        }

        val operand = operands.last()
        val rest = operands.subList(0, operands.size-1)

        val possibleWithAdd = (operand < target) && isPossible(target-operand, rest)
        val possibleWithMul = (target % operand == 0L) && isPossible(target/operand, rest)
        return possibleWithAdd || possibleWithMul
    }

    fun isPossible(): Boolean {
        val possible = isPossible(result, operands)
        return possible
    }

    private fun isPossibleWithConcatenation(target: Long, operands: List<Long>): Boolean {
        if (operands.size == 1) {
//            if (operands[0] == target) {
//                print("$result = ${operands[0]}")
//            }
            return operands[0] == target
        }

        val operand = operands.last()
        val rest = operands.subList(0, operands.size-1)

        if ((operand < target) && isPossibleWithConcatenation(target-operand, rest)) {
            // + operator works
            // print(" + $operand")
            return true
        } else if ((target % operand == 0L) && isPossibleWithConcatenation(target/operand, rest)) {
            // * operator works
            // print(" * $operand")
            return true
        } else if (target.toString().endsWith(operand.toString())) {
            val newTarget = target.toString().removeSuffix(operand.toString())
            if (newTarget.isEmpty()) {
                return false
            }
            if (isPossibleWithConcatenation(newTarget.toLong(), rest)) {
                // || operator works
                // print(" || $operand")
                return true
            }
        }
        return false
    }

    fun isPossibleWithConcatenation(): Boolean {
        return isPossibleWithConcatenation(result, operands)
    }
}

class InputReader(fileReader: FileReader, filename: String) {
    private val lines: List<String> = fileReader.readFileLines(filename)
    private val equations: List<Equation> = lines.map { line ->
        val result = line.split(':')[0].toLong()
        val operands = line.split(':')[1].trim().split(' ').map { it.toLong() }
        Equation(result, operands)
    }

    fun sumOfPossibleResults(): Long {
        return equations.filter { it.isPossible() }.sumOf { it.result }
    }

    fun sumOfPossibleResultsWithConcatenation(): Long {
        return equations.filter { it.isPossibleWithConcatenation() }.sumOf { it.result }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day7.txt")
    println(inputReader.sumOfPossibleResults())
    println(inputReader.sumOfPossibleResultsWithConcatenation())
    println(sw.stop().elapsed())
}
