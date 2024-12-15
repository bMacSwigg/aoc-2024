package day12

import com.google.common.base.Stopwatch
import common.FileReader
import common.Point

data class Cell(val p: Point, val perimeter: Int)

data class Region(val plotType: Char, val cells: MutableList<Cell>) {

    fun add(c: Cell) {
        cells.addLast(c)
    }

    fun merge(other: Region) {
        cells.addAll(other.cells)
    }

    private fun perimeter(): Int {
        return cells.sumOf { it.perimeter }
    }

    private fun area(): Int {
        return cells.size
    }

    fun cost(): Long {
        return perimeter().toLong() * area().toLong()
    }
}

class FarmMap(private val plots: Array<CharArray>) {
    private val width = plots[0].size
    private val height = plots.size
    private var regionsMap: Array<Array<Region?>> = Array(width) { Array(height) { null } }
    private val regions: MutableSet<Region> = mutableSetOf()

    private fun plotType(p: Point): Char {
        return if (p.inBounds(width, height)) {
            plots[p.y][p.x]
        } else {
            '.'
        }
    }

    private fun region(p: Point): Region? {
        return if (p.inBounds(width, height)) {
            regionsMap[p.x][p.y]
        } else {
            null
        }
    }

    private fun cell(p: Point): Cell {
        val plotType = plotType(p)
        var perimeter = 0
        if (plotType != plotType(p + Point.UP)) {
            perimeter++
        }
        if (plotType != plotType(p + Point.RIGHT)) {
            perimeter++
        }
        if (plotType != plotType(p + Point.DOWN)) {
            perimeter++
        }
        if (plotType != plotType(p + Point.LEFT)) {
            perimeter++
        }
        return Cell(p, perimeter)
    }

    fun populateRegions() {
        for ((y, line) in plots.withIndex()) {
            for ((x, plotType) in line.withIndex()) {
                val p = Point(x, y)
                val cell = cell(p)
                val matchesUp = (plotType(p) == plotType(p + Point.UP))
                val matchesLeft = (plotType(p) == plotType(p + Point.LEFT))
                if (matchesLeft) {
                    // must exist, since we've already iterated through from the left
                    val region = region(p + Point.LEFT)!!
                    region.add(cell)
                    regionsMap[p.x][p.y] = region
                    if (matchesUp) {
                        // merge
                        val other = region(p + Point.UP)!!
                        if (other == region) {
                            // little loop
                            continue
                        }
                        region.merge(other)
                        regionsMap = regionsMap.map {
                            it.map {
                                if (other == it) region else it
                            }.toTypedArray()
                        }.toTypedArray()
                        regions.removeIf { other == it }
                    }
                } else if (matchesUp) {
                    // must exist, since we've already iterated through from theabove
                    val region = region(p + Point.UP)!!
                    region.add(cell)
                    regionsMap[p.x][p.y] = region
                } else {
                    val region = Region(plotType(p), mutableListOf(cell))
                    regionsMap[p.x][p.y] = region
                    regions.add(region)
                }
            }
        }
    }

    fun totalCost(): Long {
        return regions.sumOf { it.cost() }
    }
}

class FarmReader(fileReader: FileReader, filename: String) {

    private val plots =
        fileReader.readFileLines(filename)
            .map { it.toCharArray() }
            .toTypedArray()
    private val farmMap = FarmMap(plots)

    fun totalCost(): Long {
        farmMap.populateRegions()
        return farmMap.totalCost()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val farmReader = FarmReader(FileReader(), "day12.txt")
    println(farmReader.totalCost())
    println(sw.stop().elapsed())
}
