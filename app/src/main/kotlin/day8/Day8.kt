package day8

import com.google.common.base.Stopwatch
import com.google.common.collect.ImmutableMultimap
import common.FileReader
import common.Point

class InputReader(fileReader: FileReader, filename: String) {
    private val lines: List<String> = fileReader.readFileLines(filename)
    private val width = lines[0].length
    private val height = lines.size
    private val nodes: ImmutableMultimap<Char, Point>

    init {
        val nodesBuilder = ImmutableMultimap.builder<Char, Point>()
        for ((y, line) in lines.withIndex()) {
            for ((x, char) in line.withIndex()) {
                if (char == '.') {
                    continue
                } else {
                    nodesBuilder.put(char, Point(x, y))
                }
            }
        }
        nodes = nodesBuilder.build()
    }

    fun countUniqueAntinodes(): Int {
        val antinodes = mutableSetOf<Point>()
        for (frequency in nodes.keySet()) {
            val nodesAtF = nodes[frequency].toList()
            for ((i, nodeA) in nodesAtF.withIndex()) {
                for (nodeB in nodesAtF.listIterator(i+1)){
                    val diff = nodeB - nodeA
                    antinodes.add(nodeB + diff)
                    antinodes.add(nodeA - diff)
                }
            }
        }
        return antinodes.count { it.inBounds(width, height) }
    }

    fun countUniqueAntinodesWithHarmonics(): Int {
        val antinodes = mutableSetOf<Point>()
        for (frequency in nodes.keySet()) {
            val nodesAtF = nodes[frequency].toList()
            for ((i, nodeA) in nodesAtF.withIndex()) {
                for (nodeB in nodesAtF.listIterator(i+1)){
                    val diff = nodeB - nodeA

                    var addAntinode = nodeB
                    while(addAntinode.inBounds(width, height)) {
                        antinodes.add(addAntinode)
                        addAntinode += diff
                    }

                    var subAntinode = nodeA
                    while(subAntinode.inBounds(width, height)) {
                        antinodes.add(subAntinode)
                        subAntinode -= diff
                    }
                }
            }
        }
        return antinodes.count()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val inputReader = InputReader(FileReader(), "day8.txt")
    println(inputReader.countUniqueAntinodes())
    println(inputReader.countUniqueAntinodesWithHarmonics())
    println(sw.stop().elapsed())
}
