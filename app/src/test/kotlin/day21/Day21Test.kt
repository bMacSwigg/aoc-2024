package day21

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test fun codesComplexity() {
        val classUnderTest = InputReader(FileReader(), "day21-example.txt")

        val actual = classUnderTest.codesComplexity()

        assertEquals(126384, actual)
    }
}
