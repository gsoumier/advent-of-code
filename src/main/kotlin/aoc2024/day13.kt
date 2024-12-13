package aoc2024

import AocRunner
import Coord
import InputType
import LongCoord
import StringLineParser
import Vector
import splitWhen

data class ClawMachine(
    val a: Vector,
    val b: Vector,
    val prize: LongCoord,
) {
    private val unitConvertionError = 10000000000000L
    private val fixedPrize = LongCoord(unitConvertionError + prize.x, unitConvertionError + prize.y)

    fun resolution(fixUnitConvertion: Boolean = false): Long? {
        val p = if (fixUnitConvertion) fixedPrize else prize
        val bPush = calculateB(p)
            ?.takeIf { fixUnitConvertion || it <= 100L } ?: return null
        val aPush = LongCoord(0, 0).to(b, bPush).pushToWin(a, p)
            ?.takeIf { fixUnitConvertion || it <= 100L } ?: return null
        return 3 * aPush + bPush
    }

    private fun calculateB(p: LongCoord): Long? {
        return (a.nX * p.y - a.nY * p.x).dividedBy(a.nX.toLong() * b.nY - a.nY * b.nX)
    }

    private fun LongCoord.pushToWin(toPush: Vector, p: LongCoord): Long? {
        if (x > p.x || y > p.y) return null
        return ((p.x - x) / toPush.nX)
            .takeIf { to(toPush, it) == p }
    }
}

fun Long.dividedBy(divisor: Long): Long? {
    return divideBy(divisor).takeIf { it.second == 0L }?.first
}

fun Long.divideBy(divisor: Long): Pair<Long, Long> {
    return this / divisor to this % divisor
}

class Day13(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    13,
    StringLineParser,
    inputType
) {


    private val machines = lines.splitWhen { it.isEmpty() }.map { (a, b, p) ->
        ClawMachine(
            a.parseCoord().toVector(),
            b.parseCoord().toVector(),
            p.parseCoord()
        )
    }

    override fun partOne(): Long {
        return machines.mapNotNull { it.resolution() }.sum()
    }

    override fun partTwo(): Long {
        return machines.mapNotNull { it.resolution(fixUnitConvertion = true) }.sum()
    }

}

private fun String.parseCoord(): LongCoord {
    val (_, coords) = split(": ")
    val (x, y) = coords.split(", ").map { it.drop(2).toLong() }
    return LongCoord(x, y)
}

private fun LongCoord.toVector(): Vector {
    return Vector(Coord(x.toInt(), y.toInt()))
}


fun main() {
    Day13().run()
}
