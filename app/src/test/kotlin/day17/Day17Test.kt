package day17

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    @Test fun run() {
        val classUnderTest = InputReader(FileReader(), "day17-example.txt")

        classUnderTest.computer.run()
        val actual = classUnderTest.computer.getOutput()

        assertEquals("4,6,3,5,6,3,5,2,1,0", actual)
    }

    @Test fun troubleshootRegA() {
        val classUnderTest = InputReader(FileReader(), "day17-example2.txt")

        val actual = classUnderTest.troubleshootRegA(100_000, 120_000)

        assertEquals(117_440, actual)
    }

    @Test fun smarter() {
        val classUnderTest = InputReader(FileReader(), "day17-example2.txt")

        val actual = classUnderTest.smarter()

        assertEquals(117_440, actual)
    }
}
