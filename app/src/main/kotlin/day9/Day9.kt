package day9

import com.google.common.base.Stopwatch
import common.FileReader
import java.util.LinkedList
import kotlin.math.abs

class BruteChecksum(diskMap: List<Int>) {
    private val blocks: MutableList<Int> = mutableListOf()

    init {
        var isFile = true
        for ((ind, space) in diskMap.withIndex()) {
            if (isFile && space == 0) println(ind)
            val id = ind / 2
            for (i in 1..space) {
                if (isFile) {
                    blocks.addLast(id)
                } else {
                    blocks.addLast(-1)
                }
            }
            isFile = !isFile
        }
    }

    fun compress() {
        var left = 0
        var right = blocks.size-1
        while (left <= right) {
            if (blocks[left] != -1) {
                left++
            } else if (blocks[right] == -1) {
                right--
            } else {
                blocks[left] = blocks[right]
                blocks[right] = -1
            }
        }
    }

    fun checksum(): Long {
        var sum = 0L
        for ((ind, id) in blocks.withIndex()) {
            if (id != -1) {
                sum += ind * id
            }
        }
        return sum
    }

    override fun toString(): String {
        return blocks.joinToString("") { if (it >= 0) it.toString() else "." }
    }
}

class CleverChecksum(diskMap: List<Int>) {

    private val diskMap = diskMap.toMutableList()
    private var left = 0
    private var right = diskMap.size-1
    private var blockIndex = 0
    private var sum = 0L

    fun checksum(): Long {
        while (left <= right) {
            if (left % 2 == 0) {
                // even means on a file
                sum += fileChecksumInPlace()
            } else{
                // odd means on free space -- but could be size 0
                if (diskMap[left] > 0){
                    sum += fileChecksumFromEnd()
                } else {
                    // size 0 free space, so increase left
                    left++
                }
            }
        }
        return sum
    }

    private fun fileChecksumInPlace(): Int {
        val id = left / 2
        val result = blockIndex * id
        blockIndex++
        diskMap[left]--
        if (diskMap[left] == 0) {
            left++
        }
        return result
    }

    private fun fileChecksumFromEnd(): Int {
        val id = right / 2
        val result = blockIndex * id
        blockIndex++
        diskMap[left]--
        if (diskMap[left] == 0) {
            left++
        }
        diskMap[right]--
        if (diskMap[right] == 0) {
            // skip past free space at the end
            right -= 2
        }
        return result
    }
}

data class FreeSpace(var position: Int, var size: Int)

data class File(val id: Int, var position: Int, var size: Int) {
    fun checksum(): Long {
        return id.toLong() * (size.toLong() * position + (size.toLong() * (size-1)) / 2)
    }

    fun spaceBetween(other: File): FreeSpace {
        if (other.position < (position + size)) throw IllegalArgumentException()
        return FreeSpace(
            position + size,
            other.position - (position + size))
    }
}

class FullFileMove(diskMap: List<Int>) {

    private val files: MutableList<File> = mutableListOf()
    private val freeSpaces: List<FreeSpace>

    init {
        var position = 0
        for ((ind, size) in diskMap.withIndex()) {
            if (ind % 2 == 0) {
                // this is a file
                files.add(File(ind / 2, position, size))
                position += size
            } else {
                // this is free space
                position += size
            }
        }
        freeSpaces = files.zipWithNext().map { it.first.spaceBetween(it.second) }
    }

    fun compress() {
        for (file in files.reversed()) {
            for (space in freeSpaces) {
                if (space.position > file.position) break
                if (space.size < file.size) continue
                file.position = space.position
                space.position += file.size
                space.size -= file.size
            }
        }
    }

    fun checksum(): Long {
        return files.sumOf { it.checksum().toLong() }
    }

    override fun toString(): String {
        return files.joinToString()
    }
}

class DiskMapReader(fileReader: FileReader, filename: String) {

    private val diskMap =
        fileReader.readFileText(filename)
            .map { it.toString().toInt() }
    private val clever = CleverChecksum(diskMap)
    private val brute = BruteChecksum(diskMap)
    private val fullFileMove = FullFileMove(diskMap)

    fun bruteChecksum(): Long {
        brute.compress()
        return brute.checksum()
    }

    fun cleverChecksum(): Long {
        return clever.checksum()
    }

    fun fullFileChecksum(): Long {
        fullFileMove.compress()
        return fullFileMove.checksum()
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val diskMapReader = DiskMapReader(FileReader(), "day9.txt")
    println(diskMapReader.bruteChecksum())
    println(diskMapReader.cleverChecksum())
    println(diskMapReader.fullFileChecksum())
    println(sw.stop().elapsed())
}
