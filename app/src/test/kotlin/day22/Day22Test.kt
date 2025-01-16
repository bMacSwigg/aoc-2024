package day22

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    @Test fun codesComplexity() {
        val classUnderTest = InputReader(FileReader(), "day22-example.txt")

        val actual = classUnderTest.sum2000steps()

        assertEquals(37327623L, actual)
    }
}
