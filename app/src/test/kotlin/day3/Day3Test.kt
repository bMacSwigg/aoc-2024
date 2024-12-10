package day3

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {
    @Test fun sum() {
        val classUnderTest = ProgramReader(FileReader(), "day3-example.txt")

        val actual = classUnderTest.sum()

        assertEquals(161, actual)
    }

    @Test fun conditionalSum() {
        val classUnderTest = ProgramReader(FileReader(), "day3-example-conditional.txt")

        val actual = classUnderTest.conditionalSum()

        assertEquals(48, actual)
    }
}
