package day12

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test fun totalCostMerging() {
        val classUnderTest = FarmReader(FileReader(), "day12-example-merging.txt")

        val actual = classUnderTest.totalCost()

        assertEquals(28, actual)
    }

    @Test fun totalCostSmall() {
        val classUnderTest = FarmReader(FileReader(), "day12-example-small.txt")

        val actual = classUnderTest.totalCost()

        assertEquals(140, actual)
    }

    @Test fun totalCostExclave() {
        val classUnderTest = FarmReader(FileReader(), "day12-example-exclave.txt")

        val actual = classUnderTest.totalCost()

        assertEquals(772, actual)
    }

    @Test fun totalCost() {
        val classUnderTest = FarmReader(FileReader(), "day12-example.txt")

        val actual = classUnderTest.totalCost()

        assertEquals(1930, actual)
    }
}
