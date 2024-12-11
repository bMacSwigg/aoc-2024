package day5

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    @Test fun sumValidMiddles() {
        val classUnderTest = InputReader(FileReader(), "day5-example.txt")

        val actual = classUnderTest.sumValidMiddles()

        assertEquals(143, actual)
    }

    @Test fun fixAndSumMiddles() {
        val classUnderTest = InputReader(FileReader(), "day5-example.txt")

        val actual = classUnderTest.fixAndSumMiddles()

        assertEquals(123, actual)
    }
}
