package day11

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    @Test fun countStones6() {
        val classUnderTest = StoneReader(FileReader(), "day11-example.txt")

        classUnderTest.blink(6, true)
        val actual = classUnderTest.countStones()

        assertEquals(22, actual)
    }

    @Test fun countStones25() {
        val classUnderTest = StoneReader(FileReader(), "day11-example.txt")

        classUnderTest.blink(25)
        val actual = classUnderTest.countStones()

        assertEquals(55312, actual)
    }

    @Test fun calculate6() {
        val classUnderTest = StoneReader(FileReader(), "day11-example.txt")

        val actual = classUnderTest.calculate(6)

        assertEquals(22, actual)
    }

    @Test fun calculate25() {
        val classUnderTest = StoneReader(FileReader(), "day11-example.txt")

        val actual = classUnderTest.calculate(25)

        assertEquals(55312, actual)
    }
}
