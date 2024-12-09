package common

import kotlin.test.Test
import kotlin.test.assertEquals

class FileReaderTest {
    @Test fun loadsFileLines() {
        val classUnderTest = FileReader()

        val actual = classUnderTest.readFile("test.txt")

        val expected = listOf("line1", "line2", "line3")
        assertEquals(expected, actual)
    }

    @Test fun noFile() {
        val classUnderTest = FileReader()

        val actual = classUnderTest.readFile("doesnotexist.txt")

        assertEquals(emptyList(), actual)
    }
}
