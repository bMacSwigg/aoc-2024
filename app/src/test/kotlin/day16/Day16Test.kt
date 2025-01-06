package day16

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    @Test fun dijkstra() {
        val classUnderTest = InputReader(FileReader(), "day16-example.txt")

        val actual = classUnderTest.maze.dijkstra()

        assertEquals(7036, actual)
    }

    @Test fun dijkstraBigger() {
        val classUnderTest = InputReader(FileReader(), "day16-example-bigger.txt")

        val actual = classUnderTest.maze.dijkstra()

        assertEquals(11048, actual)
    }
}
