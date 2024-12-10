package common

import kotlin.test.Test
import kotlin.test.assertEquals

class FileReaderTest {
    @Test fun loadsFileLines() {
        val classUnderTest = FileReader()

        val actual = classUnderTest.readFileLines("test.txt")

        val expected = listOf("line1", "line2", "line3")
        assertEquals(expected, actual)
    }

    @Test fun loadsFileText() {
        val classUnderTest = FileReader()

        val actual = classUnderTest.readFileText("test.txt")

        val expected = "line1\nline2\nline3"
        assertEquals(expected, actual)
    }

    @Test fun noFile() {
        val classUnderTest = FileReader()

        val actual = classUnderTest.readFileLines("doesnotexist.txt")

        assertEquals(emptyList(), actual)
    }
}
