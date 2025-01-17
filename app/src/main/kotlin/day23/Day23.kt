package day23

import com.google.common.base.Stopwatch
import common.*
import java.lang.IllegalArgumentException


data class Group(val comps: Set<String>) {

    constructor(vararg comps: String): this(comps.toSet())

    fun add(comp: String): Group {
        return Group(comps.plus(comp))
    }
}

class InputReader(fileReader: FileReader, filename: String) {

    val computers: Set<String>

    val twoGroups: Set<Group>

    init {
        val connections = fileReader.readFileLines(filename).map { it.split('-', limit=2) }
        computers = connections.flatten().toSet()
        twoGroups = connections.map { Group(it.toSet()) }.toSet()
    }

    fun findAllGroups(size: Int, prev: Set<Group>? = null): Set<Group> {
        if (size < 2) {
            throw IllegalArgumentException()
        }
        if (size == 2) {
            return twoGroups.toSet()
        }

        val smallerGroups = prev ?: findAllGroups(size-1)
        if (smallerGroups.isEmpty()) {
            return emptySet()
        }

        val groups = mutableSetOf<Group>()
        for (comp in computers) {
            for (g in smallerGroups) {
                if (g.comps.all { twoGroups.contains(Group(it, comp)) }) {
                    groups.add(g.add(comp))
                }
            }
        }
        return groups
    }

    fun startingWithT(cycles: Set<Group>): List<Group> {
        return cycles.filter { it.comps.any { it.startsWith('t') } }
    }

    fun password(): String {
        var groups = findAllGroups(3)
        for (size in 4..14) {
            val next = findAllGroups(size, groups)
            if (next.isEmpty()) {
                return groups.first().comps.sorted().joinToString(",")
            } else {
                groups = next
            }
        }
        // 14 is the largest possible group size, because every node has 13 edges
        throw AssertionError()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day23.txt")
    println(inputReader.startingWithT(inputReader.findAllGroups(3)).size)
    println(inputReader.password())
    println(sw.stop().elapsed())
}
