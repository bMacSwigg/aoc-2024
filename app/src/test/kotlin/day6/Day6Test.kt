package day6

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {
    @Test fun countPositions() {
        val classUnderTest = InputReader(FileReader(), "day6-example.txt")

        val actual = classUnderTest.countPositions()

        assertEquals(41, actual)
    }

    @Test fun countLoops() {
        val classUnderTest = InputReader(FileReader(), "day6-example.txt")

        val actual = classUnderTest.countLoops()

        assertEquals(6, actual)
    }
}
