package day13

import kotlin.math.abs

@ConsistentCopyVisibility
data class Rational private constructor(val num: Long, val den: Long) {
    companion object {
        private fun gcd(a: Long, b: Long): Long {
            if (a < 0 || b < 0) {
                return gcd(abs(a), abs(b))
            } else if (a == 0L) {
                return b
            } else if (b == 0L) {
                return a
            } else if (a < b) {
                return gcd(b, a)
            }
            // Guaranteed a >= b > 0
            return gcd(b, a % b)
        }

        private fun reduce(a: Long, b: Long): Rational {
            var divisor = gcd(a, b)
            // ensure the denominator is always positive
            if (b < 0) divisor *= -1
            return Rational(a / divisor, b / divisor)
        }

        fun reduce(a: Int, b: Int): Rational {
            return reduce(a.toLong(), b.toLong())
        }

        fun ofInt(a: Int): Rational {
            return ofLong(a.toLong())
        }

        fun ofLong(a: Long): Rational {
            return Rational(a, 1)
        }

        val ZERO: Rational = ofInt(0)
        val ONE: Rational = ofInt(1)
    }

    operator fun plus(other: Rational): Rational {
        return reduce(this.num * other.den + other.num * this.den, this.den * other.den)
    }

    operator fun minus(other: Rational): Rational {
        return reduce(this.num * other.den - other.num * this.den, this.den * other.den)
    }

    operator fun times(other: Rational): Rational {
        return reduce(this.num * other.num, this.den * other.den)
    }

    operator fun div(other: Rational): Rational {
        return reduce(this.num * other.den, this.den * other.num)
    }

    fun neg(): Rational {
        return Rational(this.num * -1, this.den)
    }

    fun isInt(): Boolean {
        return this.den == 1L
    }

    override fun toString(): String {
        return if (isInt()) {
            num.toString()
        } else {
            "$num/$den"
        }
    }
}
