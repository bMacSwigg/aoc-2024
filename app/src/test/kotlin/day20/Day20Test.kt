package day20

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {
    @Test fun allCheatsAboveZero() {
        val classUnderTest = InputReader(FileReader(), "day20-example.txt")

        val actual = classUnderTest.allCheatsAbove(0)

        assertEquals(44, actual)
    }

    @Test fun allCheatsAboveTen() {
        val classUnderTest = InputReader(FileReader(), "day20-example.txt")

        val actual = classUnderTest.allCheatsAbove(10)

        assertEquals(8, actual)
    }
}
