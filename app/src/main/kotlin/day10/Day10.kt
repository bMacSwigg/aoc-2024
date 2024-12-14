package day10

import com.google.common.base.Stopwatch
import common.FileReader
import common.Point

class TopoMap(private val elevations: Array<IntArray>) {

    private val width = elevations[0].size
    private val height = elevations.size

    fun nextSteps(p: Point): List<Point> {
        val points = listOf(
            Point(p.x + 1, p.y),
            Point(p.x, p.y + 1),
            Point(p.x - 1, p.y),
            Point(p.x, p.y - 1)
        )
        return points.filter { connected(p, it) }
    }

    private fun connected(p1: Point, p2: Point): Boolean {
        if (!p2.inBounds(width, height)) return false
        if (elevationAt(p2) != elevationAt(p1) + 1) return false
        return true
    }

    fun elevationAt(p: Point): Int {
        return elevations[p.x][p.y]
    }
}

data class Trail(val points: List<Point>) {
    fun add(p: Point): Trail {
        return Trail(points.plus(p))
    }

    fun end(): Point {
        return points.last()
    }

    fun isComplete(): Boolean {
        // since we know trails must go 0..9
        return points.size == 10
    }
}

class Trailhead(private val map: TopoMap, val start: Point) {

    private val reachable: MutableSet<Point> = mutableSetOf()
    private val toCheck: MutableList<Point> = mutableListOf(start)
    private val nines: MutableSet<Point> = mutableSetOf()

    private val completeTrails: MutableSet<Trail> = mutableSetOf()
    private val partialTrails: MutableList<Trail> = mutableListOf(Trail(listOf(start)))

    fun populateReachable() {
        while (toCheck.isNotEmpty()) {
            val point = toCheck.removeFirst()
            if (reachable.contains(point)) {
                // already checked this point
                continue
            }
            val newPoints = map.nextSteps(point).filter { !toCheck.contains(it) }
            toCheck.addAll(newPoints)
            reachable.add(point)
            if (map.elevationAt(point) == 9) {
                nines.add(point)
            }
        }
    }

    fun populateTrails() {
        while (partialTrails.isNotEmpty()) {
            val trail = partialTrails.removeFirst()
            val newPoints = map.nextSteps(trail.end())
            // If this is empty, then we will just drop this trail, since it doesn't go anywhere
            val newTrails = newPoints.map { trail.add(it) }
            newTrails.partition { it.isComplete() }.apply {
                completeTrails.addAll(first)
                partialTrails.addAll(second)
            }
        }
    }

    fun score(): Int {
        return nines.size
    }

    fun rating(): Int {
        return completeTrails.size
    }
}

class MapReader(fileReader: FileReader, filename: String) {

    private val lines = fileReader.readFileLines(filename)
    private val elevations = Array(lines[0].length) { IntArray(lines.size) }
    private val map: TopoMap
    private val trailheads: MutableList<Trailhead> = mutableListOf()

    init {
        for ((y, line) in lines.withIndex()) {
            for ((x, c) in line.withIndex()) {
                val elev = c.toString().toInt()
                elevations[x][y] = elev
            }
        }
        map = TopoMap(elevations)
        for ((x, col) in elevations.withIndex()) {
            for ((y, elev) in col.withIndex()) {
                if (elev == 0) {
                    trailheads.add(Trailhead(map, Point(x, y)))
                }
            }
        }
    }

    fun totalScore(): Long {
        trailheads.forEach { it.populateReachable() }
        return trailheads.sumOf { it.score().toLong() }
    }

    fun totalRating(): Long {
        trailheads.forEach { it.populateTrails() }
        return trailheads.sumOf { it.rating().toLong() }
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val mapReader = MapReader(FileReader(), "day10.txt")
    println(mapReader.totalScore())
    println(mapReader.totalRating())
    println(sw.stop().elapsed())
}
