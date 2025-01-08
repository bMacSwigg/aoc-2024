package day18

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test fun dijkstra() {
        val classUnderTest = InputReader(FileReader(), "day18-example.txt", 7, 7)

        classUnderTest.dropBytes(12)
        val actual = classUnderTest.board.dijkstra()

        assertEquals(22, actual)
    }

    @Test fun testReachability() {
        val classUnderTest = InputReader(FileReader(), "day18-example.txt", 7, 7)

        val actual = classUnderTest.testReachability()

        assertEquals(Pair(6, 1), actual)
    }
}
