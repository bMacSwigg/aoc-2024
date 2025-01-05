package day13

import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MatrixTest {
    @Test fun addition() {
        val first = Matrix(Rational.ofInt(1), Rational.ofInt(2), Rational.ofInt(3), Rational.ofInt(4))
        val second = Matrix(Rational.ofInt(5), Rational.ofInt(6), Rational.ofInt(7), Rational.ofInt(8))

        val actual = first + second

        val expected = Matrix(Rational.ofInt(6), Rational.ofInt(8), Rational.ofInt(10), Rational.ofInt(12))
        assertEquals(expected, actual)
    }

    @Test fun subtraction() {
        val first = Matrix(Rational.ofInt(5), Rational.ofInt(6), Rational.ofInt(7), Rational.ofInt(8))
        val second = Matrix(Rational.ofInt(1), Rational.ofInt(3), Rational.ofInt(5), Rational.ofInt(7))

        val actual = first - second

        val expected = Matrix(Rational.ofInt(4), Rational.ofInt(3), Rational.ofInt(2), Rational.ofInt(1))
        assertEquals(expected, actual)
    }

    @Test fun multiplication() {
        val first = Matrix(Rational.ofInt(1), Rational.ofInt(2), Rational.ofInt(3), Rational.ofInt(4))
        val second = Matrix(Rational.ofInt(5), Rational.ofInt(6), Rational.ofInt(7), Rational.ofInt(8))

        val actual = first * second

        val expected = Matrix(Rational.ofInt(19), Rational.ofInt(22), Rational.ofInt(43), Rational.ofInt(50))
        assertEquals(expected, actual)
    }

    @Test fun timesVector() {
        val mat = Matrix(Rational.ofInt(1), Rational.ofInt(3), Rational.ofInt(4), Rational.ofInt(0))
        val vec = Pair(1L, 5L)

        val actual = mat.timesLongVector(vec)

        val expected = Pair(Rational.ofInt(16), Rational.ofInt(4))
        assertEquals(expected, actual)
    }

    @Test fun determinant() {
        val mat = Matrix(Rational.ofInt(8), Rational.ofInt(5), Rational.ofInt(4), Rational.ofInt(5))

        val actual = mat.determinant()

        assertEquals(Rational.ofInt(20), actual)
    }

    @Test fun inverse() {
        val mat = Matrix(Rational.ofInt(-3), Rational.ofInt(1), Rational.ofInt(5), Rational.ofInt(0))

        val actual = mat.inverse()

        val expected = Matrix(Rational.ZERO, Rational.reduce(1, 5), Rational.ONE, Rational.reduce(3, 5))
        assertEquals(expected, actual)
    }

    @Test fun inverseDoesNotExist() {
        val mat = Matrix(Rational.ofInt(1), Rational.ofInt(2), Rational.ofInt(3), Rational.ofInt(6))

        assertThrows<IllegalStateException> { mat.inverse() }
    }
}
