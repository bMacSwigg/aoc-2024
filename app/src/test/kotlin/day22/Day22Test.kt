package day22

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    @Test fun sum2000steps() {
        val classUnderTest = InputReader(FileReader(), "day22-example.txt")

        val actual = classUnderTest.sum2000steps()

        assertEquals(37327623L, actual)
    }

    @Test fun findMax() {
        val classUnderTest = InputReader(FileReader(), "day22-example2.txt")

        val actual = classUnderTest.findMax()

        assertEquals(23, actual)
    }
}
