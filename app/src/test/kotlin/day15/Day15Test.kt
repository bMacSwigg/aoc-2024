package day15

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    @Test fun sumGpsSmall() {
        val classUnderTest = InputReader(FileReader(), "day15-example-small.txt")

        classUnderTest.runCommands()
        val actual = classUnderTest.warehouse.sumGps()

        assertEquals(2028, actual)
    }

    @Test fun sumGps() {
        val classUnderTest = InputReader(FileReader(), "day15-example.txt")

        classUnderTest.runCommands()
        val actual = classUnderTest.warehouse.sumGps()

        assertEquals(10092, actual)
    }

    @Test fun biggerWarehouse_sumGps() {
        val classUnderTest = InputReader(FileReader(), "day15-example.txt")

        classUnderTest.runBiggerCommands()
        val actual = classUnderTest.biggerWarehouse.sumGps()

        assertEquals(9021, actual)
    }
}
