package day9

import common.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day9Test {
    @Test fun bruteChecksumSmall() {
        val classUnderTest = DiskMapReader(FileReader(), "day9-example-small.txt")

        val actual = classUnderTest.bruteChecksum()

        assertEquals(60, actual)
    }

    @Test fun cleverChecksumSmall() {
        val classUnderTest = DiskMapReader(FileReader(), "day9-example-small.txt")

        val actual = classUnderTest.cleverChecksum()

        assertEquals(60, actual)
    }

    @Test fun bruteChecksum() {
        val classUnderTest = DiskMapReader(FileReader(), "day9-example.txt")

        val actual = classUnderTest.bruteChecksum()

        assertEquals(1928, actual)
    }

    @Test fun cleverChecksum() {
        val classUnderTest = DiskMapReader(FileReader(), "day9-example.txt")

        val actual = classUnderTest.cleverChecksum()

        assertEquals(1928, actual)
    }

    @Test fun fullFileChecksum() {
        val classUnderTest = DiskMapReader(FileReader(), "day9-example.txt")

        val actual = classUnderTest.fullFileChecksum()

        assertEquals(2858, actual)
    }
}
