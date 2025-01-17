package day23

import com.google.common.base.Stopwatch
import common.*

class CycleFinder(val start: Computer, val length: Int) {

    fun findAll(): Set<Set<String>> {
        return explore(start, setOf(), length)
    }

    private fun explore(node: Computer, others: Set<Computer>, depth: Int): Set<Set<String>> {
        if (depth == 0) {
            if (node == start) {
                return setOf(others.map { it.id }.toSet())
            } else {
                return setOf()
            }
        }

        return node.connections.flatMap { explore(it, others.plus(node), depth-1) }.toSet()
    }
}

class Computer(val id: String) {

    val connections = mutableListOf<Computer>()

    fun addConnection(other: Computer) {
        connections.add(other)
    }
}

data class Group(val comps: Set<String>) {

    constructor(vararg comps: String): this(comps.toSet())

    fun add(comp: String): Group {
        return Group(comps.plus(comp))
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    val computers = mutableMapOf<String, Computer>()
    val connections = fileReader.readFileLines(filename).map { it.split('-', limit=2) }
    val twoGroups = connections.map { Group(it.toSet()) }

    init {
        for (connection in connections) {
            val comp1 = computers.computeIfAbsent(connection[0]) { k -> Computer(k) }
            val comp2 = computers.computeIfAbsent(connection[1]) { k -> Computer(k) }
            comp1.addConnection(comp2)
            comp2.addConnection(comp1)
        }
    }

    fun findAll3Cycles(): Set<Set<String>> {
        return computers.values.map { CycleFinder(it, 3) }.flatMap { it.findAll() }.toSet()
    }

    fun startingWithT(cycles: Set<Set<String>>): List<Set<String>> {
        return cycles.filter { it.any { it.startsWith('t') } }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day23.txt")
    println(inputReader.startingWithT(inputReader.findAll3Cycles()).size)
    println(sw.stop().elapsed())
}
