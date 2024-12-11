package day4

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day4Test {
    @Test fun countXmas() {
        val classUnderTest = BoardReader(FileReader(), "day4-example.txt")

        val actual = classUnderTest.countXmas()

        assertEquals(18, actual)
    }

    @Test fun countMasX() {
        val classUnderTest = BoardReader(FileReader(), "day4-example.txt")

        val actual = classUnderTest.countMasX()

        assertEquals(9, actual)
    }
}
