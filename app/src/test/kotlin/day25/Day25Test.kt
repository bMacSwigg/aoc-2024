package day25

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {
    @Test fun countViablePairs() {
        val classUnderTest = InputReader(FileReader(), "day25-example.txt")

        val actual = classUnderTest.countViablePairs()

        assertEquals(3L, actual)
    }
}
