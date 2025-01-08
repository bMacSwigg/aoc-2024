package day19

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test fun countPossible() {
        val classUnderTest = InputReader(FileReader(), "day19-example.txt")

        val actual = classUnderTest.countPossible()

        assertEquals(6, actual)
    }

    @Test fun countAllCombos() {
        val classUnderTest = InputReader(FileReader(), "day19-example.txt")

        val actual = classUnderTest.countAllCombos()

        assertEquals(16, actual)
    }
}
