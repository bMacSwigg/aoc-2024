package day13

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RationalTest {
    @Test fun reduces() {
        val twoThirds = Rational.reduce(8, 12)

        assertEquals(2, twoThirds.num)
        assertEquals(3, twoThirds.den)

        val fourThirds = Rational.reduce(8, 6)

        assertEquals(4, fourThirds.num)
        assertEquals(3, fourThirds.den)

        val five = Rational.reduce(10, 2)

        assertEquals(5, five.num)
        assertEquals(1, five.den)
    }

    @Test fun denominatorAlwaysPositive() {
        val numNegative = Rational.reduce(-8, 12)

        assertEquals(-2, numNegative.num)
        assertEquals(3, numNegative.den)

        val denNegative = Rational.reduce(8, -6)

        assertEquals(-4, denNegative.num)
        assertEquals(3, denNegative.den)

        val bothNegative = Rational.reduce(-10, -2)

        assertEquals(5, bothNegative.num)
        assertEquals(1, bothNegative.den)
    }

    @Test fun equality() {
        val first = Rational.reduce(3, 6)
        val second = Rational.reduce(2, 4)

        assertTrue(first == second)
    }

    @Test fun additionSameDen() {
        val first = Rational.reduce(1, 5)
        val second = Rational.reduce(2, 5)

        val actual = first + second

        assertEquals(Rational.reduce(3, 5), actual)
    }

    @Test fun additionDifferentDen() {
        val first = Rational.reduce(1, 5)
        val second = Rational.reduce(1, 3)

        val actual = first + second

        assertEquals(Rational.reduce(8, 15), actual)
    }

    @Test fun subtractionSameDen() {
        val first = Rational.reduce(2, 5)
        val second = Rational.reduce(1, 5)

        val actual = first - second

        assertEquals(Rational.reduce(1, 5), actual)
    }

    @Test fun subtractionDifferentDen() {
        val first = Rational.reduce(1, 3)
        val second = Rational.reduce(1, 5)

        val actual = first - second

        assertEquals(Rational.reduce(2, 15), actual)
    }

    @Test fun multiplication() {
        val first = Rational.reduce(1, 3)
        val second = Rational.reduce(3, 5)

        val actual = first * second

        assertEquals(Rational.reduce(1, 5), actual)
    }

    @Test fun division() {
        val first = Rational.reduce(1, 3)
        val second = Rational.reduce(3, 5)

        val actual = first / second

        assertEquals(Rational.reduce(5, 9), actual)
    }

    @Test fun negation() {
        val first = Rational.reduce(1, 2)

        val actual = first.neg()

        assertEquals(Rational.reduce(-1, 2), actual)
    }

    @Test fun doubleNegation() {
        val first = Rational.reduce(1, 2)

        val actual = first.neg().neg()

        assertEquals(first, actual)
    }

    @Test fun isIntTrue() {
        val first = Rational.reduce(4, 2)

        assertTrue(first.isInt())
    }

    @Test fun isIntFalse() {
        val first = Rational.reduce(3, 2)

        assertFalse(first.isInt())
    }
}
