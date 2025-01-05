package day13

data class Matrix(val a: Rational, val b: Rational,
                  val c: Rational, val d: Rational) {

    operator fun plus(other: Matrix): Matrix {
        return Matrix(a + other.a, b + other.b, c + other.c, d + other.d)
    }

    operator fun minus(other: Matrix): Matrix {
        return Matrix(a - other.a, b - other.b, c - other.c, d - other.d)
    }

    operator fun times(other: Matrix): Matrix {
        return Matrix(a * other.a + b * other.c,
                      a * other.b + b * other.d,
                      c * other.a + d * other.c,
                      c * other.b + d * other.d)
    }

    private fun timesVector(v: Pair<Rational, Rational>): Pair<Rational, Rational> {
        return Pair(
            a * v.first + b * v.second,
            c * v.first + d * v.second
        )
    }

    fun timesIntVector(v: Pair<Int, Int>): Pair<Rational, Rational> {
        return timesVector(Pair(Rational.ofInt(v.first), Rational.ofInt(v.second)))
    }

    fun determinant(): Rational {
        return (a * d) - (b * c)
    }

    fun inverse(): Matrix {
        val det = determinant()
        if (det == Rational.ZERO) {
            throw IllegalStateException()
        }
        return Matrix(d / det, b.neg() / det, c.neg() / det, a / det)
    }
}
