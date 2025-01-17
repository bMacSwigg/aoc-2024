package day24

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {
    @Test fun zValuesSmall() {
        val classUnderTest = InputReader(FileReader(), "day24-example-small.txt")

        val actual = classUnderTest.zValues()

        assertEquals(4L, actual)
    }

    @Test fun zValues() {
        val classUnderTest = InputReader(FileReader(), "day24-example.txt")

        val actual = classUnderTest.zValues()

        assertEquals(2024L, actual)
    }
}
