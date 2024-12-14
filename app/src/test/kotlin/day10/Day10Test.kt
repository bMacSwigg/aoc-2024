package day10

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    @Test fun totalScoreSmall() {
        val classUnderTest = MapReader(FileReader(), "day10-example-small.txt")

        val actual = classUnderTest.totalScore()

        assertEquals(1, actual)
    }

    @Test fun totalScore() {
        val classUnderTest = MapReader(FileReader(), "day10-example.txt")

        val actual = classUnderTest.totalScore()

        assertEquals(36, actual)
    }

    @Test fun totalRatingSmall() {
        val classUnderTest = MapReader(FileReader(), "day10-example-small.txt")

        val actual = classUnderTest.totalRating()

        assertEquals(16, actual)
    }

    @Test fun totalRating() {
        val classUnderTest = MapReader(FileReader(), "day10-example.txt")

        val actual = classUnderTest.totalRating()

        assertEquals(81, actual)
    }
}
