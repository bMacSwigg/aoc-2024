package day1

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Test {
    @Test fun diff() {
        val classUnderTest = ListReader(FileReader(), "day1-example.txt")

        val actual = classUnderTest.calculateDiff()

        assertEquals(11, actual)
    }

    @Test fun similarity() {
        val classUnderTest = ListReader(FileReader(), "day1-example.txt")

        val actual = classUnderTest.calculateSimilarity()

        assertEquals(31, actual)
    }
}