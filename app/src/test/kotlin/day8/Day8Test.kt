package day8

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {
    @Test fun countUniqueAntinodes() {
        val classUnderTest = InputReader(FileReader(), "day8-example.txt")

        val actual = classUnderTest.countUniqueAntinodes()

        assertEquals(14, actual)
    }

    @Test fun countUniqueAntinodesWithHarmonics() {
        val classUnderTest = InputReader(FileReader(), "day8-example.txt")

        val actual = classUnderTest.countUniqueAntinodesWithHarmonics()

        assertEquals(34, actual)
    }
}
