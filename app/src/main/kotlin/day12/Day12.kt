package day12

import com.google.common.base.Stopwatch
import common.FileReader
import common.Point

data class Cell(val p: Point, val edges: List<Point>)

data class Region(val plotType: Char, val cells: MutableList<Cell>) {

    fun add(c: Cell) {
        cells.addLast(c)
    }

    fun merge(other: Region) {
        cells.addAll(other.cells)
    }

    private fun perimeter(): Int {
        return cells.sumOf { it.edges.size }
    }

    private fun area(): Int {
        return cells.size
    }

    fun cost(): Long {
        return perimeter().toLong() * area().toLong()
    }

    fun sideCost(): Long {
        val counter = RegionSideCounter(this)
        return counter.countSides().toLong() * area().toLong()
    }
}

data class Side(val dir: Point, val points: MutableList<Point>) {

    fun add(p: Point) {
        points.add(p)
    }

    fun merge(other: Side) {
        if (dir != other.dir) throw IllegalArgumentException()
        points.addAll(other.points)
    }
}

class RegionSideCounter(private val r: Region) {
    // wow I hate this data structure... oof.
    // it's a map from a pair of (coords on map, direction) to a Side
    // in other words, "at [x, y] and to the Point.LEFT"
    private var sidesMap: MutableMap<Pair<Point, Point>, Side> = mutableMapOf()
    private val sides: MutableSet<Side> = mutableSetOf()

    init {
        for (c in r.cells) {
            for (dir in c.edges) {
                when(dir) {
                    Point.LEFT -> processLeftEdge(c)
                    Point.UP -> processUpEdge(c)
                    Point.RIGHT -> processRightEdge(c)
                    Point.DOWN -> processDownEdge(c)
                }
            }
        }
    }

    fun countSides(): Int {
        return sides.size
    }

    private fun processLeftEdge(c: Cell) {
        val below = r.cells.find { it.p == (c.p + Point.DOWN) }
        val above = r.cells.find { it.p == (c.p + Point.UP) }
        processEdge(c, Point.LEFT, below, above)
    }

    private fun processUpEdge(c: Cell) {
        val left = r.cells.find { it.p == (c.p + Point.LEFT) }
        val right = r.cells.find { it.p == (c.p + Point.RIGHT) }
        processEdge(c, Point.UP, left, right)
    }
    private fun processRightEdge(c: Cell) {
        val above = r.cells.find { it.p == (c.p + Point.UP) }
        val below = r.cells.find { it.p == (c.p + Point.DOWN) }
        processEdge(c, Point.RIGHT, above, below)
    }
    private fun processDownEdge(c: Cell) {
        val right = r.cells.find { it.p == (c.p + Point.RIGHT) }
        val left = r.cells.find { it.p == (c.p + Point.LEFT) }
        processEdge(c, Point.DOWN, right, left)
    }

    private fun processEdge(c: Cell, dir: Point, adj1: Cell?, adj2: Cell?) {
        val side1 = sidesMap[Pair(adj1?.p, dir)]
        val side2 = sidesMap[Pair(adj2?.p, dir)]

        if (side1 != null) {
            side1.add(c.p)
            sidesMap[Pair(c.p, dir)] = side1
            if (side2 != null) {
                side1.merge(side2)
                sidesMap.replaceAll { _, side -> if (side == side2) side1 else side }
                sides.removeIf { side2 == it }
            }
        } else if (side2 != null) {
            side2.add(c.p)
            sidesMap[Pair(c.p, dir)] = side2
        } else {
            val side = Side(dir, mutableListOf(c.p))
            sidesMap[Pair(c.p, dir)] = side
            sides.add(side)
        }
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
        val perimeter = mutableListOf<Point>()
        if (plotType != plotType(p + Point.UP)) {
            perimeter += Point.UP
        }
        if (plotType != plotType(p + Point.RIGHT)) {
            perimeter += Point.RIGHT
        }
        if (plotType != plotType(p + Point.DOWN)) {
            perimeter += Point.DOWN
        }
        if (plotType != plotType(p + Point.LEFT)) {
            perimeter += Point.LEFT
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

    fun totalSideCost(): Long {
        return regions.sumOf { it.sideCost() }
    }
}

class FarmReader(fileReader: FileReader, filename: String) {

    private val plots =
        fileReader.readFileLines(filename)
            .map { it.toCharArray() }
            .toTypedArray()
    private val farmMap = FarmMap(plots)

    init {
        farmMap.populateRegions()
    }

    fun totalCost(): Long {
        return farmMap.totalCost()
    }

    fun totalSideCost(): Long {
        return farmMap.totalSideCost()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val farmReader = FarmReader(FileReader(), "day12.txt")
    println(farmReader.totalCost())
    println(farmReader.totalSideCost())
    println(sw.stop().elapsed())
}
