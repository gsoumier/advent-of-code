data class Fraction(val num: Long, val den: Long) {
    override fun equals(other: Any?): Boolean {
        if (other !is Fraction) return false
        return num * other.den == other.num * den
    }

    fun toDouble(): Double {
        return num.toDouble() / den
    }
}

data class Day24Line(
    val coord: ZCoordLong,
    val delta: ZCoordLong,
) {
    fun lineEq(): LineEquation {
        return LineEquation(
            Fraction(delta.y, delta.x),
            Fraction(coord.y * delta.x - coord.x * delta.y, delta.x)
        )
    }

    fun getFutureCrossWith(other: Day24Line): Pair<Double, Double>? {
        if (lineEq().a == other.lineEq().a)
            return null
        val xCross = other.lineEq().b.toDouble().minus(lineEq().b.toDouble())
            .div(lineEq().a.toDouble().minus(other.lineEq().a.toDouble()))
        val yCross = lineEq().y(xCross)
        return (xCross to yCross).takeIf { isInFuture(it) && other.isInFuture(it) }
    }

    private fun isInFuture(cross: Pair<Double, Double>): Boolean {
        return cross.first.isXInFuture() && cross.second.isYInFuture()
    }

    private fun Double.isXInFuture(): Boolean {
        return ((this - coord.x) * delta.x) > 0
    }

    private fun Double.isYInFuture(): Boolean {
        return ((this - coord.y) * delta.y) > 0
    }
}


data class LineEquation(val a: Fraction, val b: Fraction) {
    fun y(x: Double): Double {
        return a.toDouble() * x + b.toDouble()
    }
}

object Day24Parser : LineParser<Day24Line> {
    override fun parseLine(index: Int, line: String): Day24Line {
        return line.split(" @ ").let { (s, e) ->
            Day24Line(
                s.parseZCoordLong(),
                e.parseZCoordLong(),
            )
        }
    }
}

class Day24(inputType: InputType = InputType.FINAL) : AocRunner<Day24Line, Long>(
    24,
    Day24Parser,
    inputType
) {
    override fun partOne(): Long {
        return findCrossIn(200000000000000L, 400000000000000L).toLong()
    }

    fun findCrossIn(min: Long, max: Long): Int {
        val cross =
            lines.flatMapIndexed { i1, l1 -> lines.mapIndexedNotNull { i2, l2 -> (l1 to l2).takeIf { i2 > i1 } } }
                .mapNotNull { (l1, l2) -> l1.getFutureCrossWith(l2) }
        return cross.count { it.isIn(min..max) }
    }

    override fun partTwo(): Long {
        val possibleResults = testDeltaFor(lines.first(), lines.last(), 1000)
        val result = possibleResults.filter {
                possibleMagicalThrow -> lines.all { testMagicalThrow(it, possibleMagicalThrow) }
        }
        return result.singleOrNull()?.let { it.coord.let { c -> c.x + c.y + c.z } } ?: 0L
    }

    private fun testDeltaFor(lineA: Day24Line, lineB: Day24Line, deltaMax: Long): List<Day24Line> {
        return (-deltaMax..deltaMax).flatMap { dx ->
            (-deltaMax..deltaMax).mapNotNull { dy ->
                calculateTa(lineA, lineB, dx, dy)?.let { ta ->
                    calculateTb(lineA, lineB, dx, ta)?.let { tb ->
                        calculateDz(lineA, lineB, ta, tb)?.let { dz ->
                            val delta = ZCoordLong(dx, dy, dz)
                            Day24Line(calculateInitial(lineA, ta, delta), delta)
                        }
                    }
                }

            }
        }
    }


    private fun testMagicalThrow(lineA: Day24Line, candidate: Day24Line): Boolean {
       return calculateTa(lineA, candidate)?.let { ta ->
            testImpact(lineA, candidate, ta) { it.x }
                    && testImpact(lineA, candidate, ta) { it.y }
                    && testImpact(lineA, candidate, ta) { it.z }
       } ?: false

    }


    private fun testImpact(lineA: Day24Line, candidate: Day24Line, ta: Long, coord: (ZCoordLong) -> Long): Boolean {
        return coord(candidate.coord) + ta * coord(candidate.delta) == (coord(lineA.coord) + ta * coord(lineA.delta))
    }

    private fun calculateInitial(lineA: Day24Line, ta: Long, delta: ZCoordLong): ZCoordLong {
        return ZCoordLong(
            calculateInitial(lineA, ta, delta, { it.x }),
            calculateInitial(lineA, ta, delta, { it.y }),
            calculateInitial(lineA, ta, delta, { it.z }),
        )
    }

    private fun calculateInitial(lineA: Day24Line, ta: Long, delta: ZCoordLong, coord: (ZCoordLong) -> Long): Long {
        return coord(lineA.coord) - ta * (coord(delta) - coord(lineA.delta))
    }

    private fun calculateTa(lineA: Day24Line, lineB: Day24Line, dx: Long, dy: Long): Long? {
        val xa = lineA.coord.x
        val xb = lineB.coord.x
        val ya = lineA.coord.y
        val yb = lineB.coord.y
        val dxa = lineA.delta.x
        val dxb = lineB.delta.x
        val dya = lineA.delta.y
        val dyb = lineB.delta.y
        val numerator = (xa - xb) * (dy - dyb) - (ya - yb) * (dx - dxb)
        val denominator = ((dx - dxa) * (dy - dyb) - (dy - dya) * (dx - dxb)).takeUnless { it == 0L } ?: return null
        if (numerator % denominator != 0L) return null
        return numerator / denominator
    }


    private fun calculateTa(lineA: Day24Line, candidate: Day24Line): Long? {
        val xa = lineA.coord.x
        val x0 = candidate.coord.x
        val dxa = lineA.delta.x
        val dx = candidate.delta.x
        val numerator = (xa - x0)
        val denominator = ((dx - dxa)).takeUnless { it == 0L } ?: return null
        if (numerator % denominator != 0L) return null
        return numerator / denominator
    }

    private fun calculateTb(lineA: Day24Line, lineB: Day24Line, dx: Long, ta: Long): Long? {
        val xa = lineA.coord.x
        val xb = lineB.coord.x
        val dxa = lineA.delta.x
        val dxb = lineB.delta.x
        val numerator = (xb - xa) + ta * (dx - dxa)
        val denominator = (dx - dxb).takeUnless { it == 0L } ?: return null
        if (numerator % denominator != 0L) return null
        return numerator / denominator
    }

    private fun calculateDz(lineA: Day24Line, lineB: Day24Line, ta: Long, tb: Long): Long? {
        val za = lineA.coord.z
        val zb = lineB.coord.z
        val dza = lineA.delta.z
        val dzb = lineB.delta.z
        val numerator = (zb - za) - ta * dza + tb * dzb
        val denominator = (tb - ta).takeUnless { it == 0L } ?: return null
        if (numerator % denominator != 0L) return null
        return (numerator / denominator)
    }

}

private fun Pair<Double, Double>.isIn(range: LongRange): Boolean {
    return first.toLong() in range && second.toLong() in range
}


fun main() {
    Day24().run()
}
