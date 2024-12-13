package day7

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day7Test {
    @Test fun sumOfPossibleResults() {
        val classUnderTest = InputReader(FileReader(), "day7-example.txt")

        val actual = classUnderTest.sumOfPossibleResults()

        assertEquals(3749, actual)
    }

    @Test fun sumOfPossibleResultsWithConcatenation() {
        val classUnderTest = InputReader(FileReader(), "day7-example.txt")

        val actual = classUnderTest.sumOfPossibleResultsWithConcatenation()

        assertEquals(11387, actual)
    }
}
