package day13

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {
    @Test fun sumSolutions() {
        val classUnderTest = EquationsReader(FileReader(), "day13-example.txt")

        val actual = classUnderTest.sumSolutions()

        assertEquals(480, actual)
    }

    @Test fun sumSolutionsBruteForce() {
        val classUnderTest = EquationsReader(FileReader(), "day13-example.txt")

        val actual = classUnderTest.sumSolutionsBruteForce()

        assertEquals(480, actual)
    }
}
