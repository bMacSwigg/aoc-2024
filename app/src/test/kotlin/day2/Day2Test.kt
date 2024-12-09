package day2

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test fun countSafe() {
        val classUnderTest = ReportReader(FileReader(), "day2-example.txt")

        val actual = classUnderTest.countSafe()

        assertEquals(2, actual)
    }

    @Test fun countSafeWithDampener() {
        val classUnderTest = ReportReader(FileReader(), "day2-example.txt")

        val actual = classUnderTest.countSafeWithDampener()

        assertEquals(4, actual)
    }
}
