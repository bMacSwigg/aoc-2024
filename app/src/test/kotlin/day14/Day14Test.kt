package day14

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    @Test fun calculateSafetyFactor() {
        val classUnderTest = RobotsReader(FileReader(), "day14-example.txt", Pair(11L, 7L))

        val actual = classUnderTest.calculateSafetyFactor()

        assertEquals(12, actual)
    }
}
