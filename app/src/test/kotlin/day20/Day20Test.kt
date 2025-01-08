package day20

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {
    @Test fun all2psCheatsAboveZero() {
        val classUnderTest = InputReader(FileReader(), "day20-example.txt")

        val actual = classUnderTest.allCheatsAbove(2, 0)

        assertEquals(44, actual)
    }

    @Test fun all2psCheatsAboveTen() {
        val classUnderTest = InputReader(FileReader(), "day20-example.txt")

        val actual = classUnderTest.allCheatsAbove(2, 10)

        assertEquals(8, actual)
    }

    @Test fun all20psCheatsAboveFifty() {
        val classUnderTest = InputReader(FileReader(), "day20-example.txt")

        val actual = classUnderTest.allCheatsAbove(20, 50)

        assertEquals(253, actual)
    }
}
