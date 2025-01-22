package day4

import com.google.common.base.Stopwatch
import common.FileReader

class WordSearch(lines: List<String>) {

    private val words: Array<CharArray>
    val width = lines[0].length
    val height = lines.size

    init {
        words = Array(width) { CharArray(height) }
        for ((i, line) in lines.withIndex()) {
            for ((j, char) in line.withIndex()) {
                words[i][j] = char
            }
        }
    }

    private fun charAt(i: Int, j: Int): Char {
        if (i < 0 || i >= width || j < 0 || j >= width) {
            return '!'
        }
        return words[i][j]
    }

    fun countXmas(i: Int, j: Int): Int {
        if (charAt(i, j) != 'X') {
            return 0
        }

        var count = 0
        // check right
        if (charAt(i+1, j) == 'M' && charAt(i+2, j) == 'A' && charAt(i+3, j) == 'S') {
            count++
        }
        // check up-right
        if (charAt(i+1, j+1) == 'M' && charAt(i+2, j+2) == 'A' && charAt(i+3, j+3) == 'S') {
            count++
        }
        // check up
        if (charAt(i, j+1) == 'M' && charAt(i, j+2) == 'A' && charAt(i, j+3) == 'S') {
            count++
        }
        // check up-left
        if (charAt(i-1, j+1) == 'M' && charAt(i-2, j+2) == 'A' && charAt(i-3, j+3) == 'S') {
            count++
        }
        // check left
        if (charAt(i-1, j) == 'M' && charAt(i-2, j) == 'A' && charAt(i-3, j) == 'S') {
            count++
        }
        // check down-left
        if (charAt(i-1, j-1) == 'M' && charAt(i-2, j-2) == 'A' && charAt(i-3, j-3) == 'S') {
            count++
        }
        // check down
        if (charAt(i, j-1) == 'M' && charAt(i, j-2) == 'A' && charAt(i, j-3) == 'S') {
            count++
        }
        // check down-right
        if (charAt(i+1, j-1) == 'M' && charAt(i+2, j-2) == 'A' && charAt(i+3, j-3) == 'S') {
            count++
        }

        return count
    }

    fun countMasX(i: Int, j: Int): Int {
        if (charAt(i, j) != 'A') {
            return 0
        }

        var count = 0
        // check MSMS
        if (charAt(i-1, j+1) == 'M' &&
            charAt(i+1, j+1) == 'S' &&
            charAt(i-1, j-1) == 'M' &&
            charAt(i+1, j-1) == 'S') {
            count++
        }
        // check MMSS
        if (charAt(i-1, j+1) == 'M' &&
            charAt(i+1, j+1) == 'M' &&
            charAt(i-1, j-1) == 'S' &&
            charAt(i+1, j-1) == 'S') {
            count++
        }
        // check SMSM
        if (charAt(i-1, j+1) == 'S' &&
            charAt(i+1, j+1) == 'M' &&
            charAt(i-1, j-1) == 'S' &&
            charAt(i+1, j-1) == 'M') {
            count++
        }
        // check SSMM
        if (charAt(i-1, j+1) == 'S' &&
            charAt(i+1, j+1) == 'S' &&
            charAt(i-1, j-1) == 'M' &&
            charAt(i+1, j-1) == 'M') {
            count++
        }

        return count
    }
}

class BoardReader(fileReader: FileReader, filename: String) {

    private val lines: List<String> = fileReader.readFileLines(filename)
    private val wordSearch = WordSearch(lines)

    fun countXmas(): Int {
        var count = 0
        for (i in 0..<wordSearch.width) {
            for (j in 0..<wordSearch.height) {
                count += wordSearch.countXmas(i, j)
            }
        }
        return count
    }

    fun countMasX(): Int {
        var count = 0
        for (i in 0..<wordSearch.width) {
            for (j in 0..<wordSearch.height) {
                count += wordSearch.countMasX(i, j)
            }
        }
        return count
    }
}

fun main() {
    val sw = Stopwatch.createStarted()
    val boardReader = BoardReader(FileReader(), "day4.txt")
    println(boardReader.countXmas())
    println(boardReader.countMasX())
    println(sw.stop().elapsed())
}
