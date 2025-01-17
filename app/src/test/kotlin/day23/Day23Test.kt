package day23

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test fun cyclesStartingWithT() {
        val classUnderTest = InputReader(FileReader(), "day23-example.txt")

        val actual = classUnderTest.startingWithT(classUnderTest.findAll3Cycles())

        assertEquals(7, actual.size)
    }
}
