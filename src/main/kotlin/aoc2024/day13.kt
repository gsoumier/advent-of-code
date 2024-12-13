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

    val forgotten = 10000000000000L
    val realPrize = LongCoord(forgotten + prize.x, forgotten + prize.y)

    fun resolution(noLimit: Boolean): Long? {
        val bPush = calculateB(noLimit)?.takeIf { noLimit || it <= 100L } ?: return null
        val aPush = LongCoord(0, 0).to(b, bPush).pushToWin(a, noLimit) ?: return null
        return 3 * aPush + bPush
    }

    fun calculateB(noLimit: Boolean) : Long?{
        val p = if(noLimit) realPrize else prize
        return (a.nX * p.y - a.nY * p.x).dividedBy(a.nX.toLong() * b.nY - a.nY * b.nX)
    }

    fun LongCoord.pushToWin(toPush: Vector, noLimit: Boolean = false): Long? {
        val p = if(noLimit) realPrize else prize
        if (x > p.x || y > p.y) return null
        return ((p.x - x) / toPush.nX)
            .takeIf { to(toPush, it) == if(noLimit) realPrize else p}
            ?.takeIf { noLimit || it <= 100 }
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


    val machines = lines.splitWhen { it.isEmpty() }.map { (a, b, p) ->
        ClawMachine(a.parseButton(), b.parseButton(), p.parsePrize())
    }

    override fun partOne(): Long {
        return machines.mapNotNull { it.resolution(false) }.sum()
    }

    override fun partTwo(): Long {
        return machines.mapNotNull { it.resolution(true) }.sum()
    }

}

private fun String.parseButton(): Vector {
    val (_, coords) = split(": ")
    val (x, y) = coords.split(", ").map { it.drop(2).toInt() }
    return Vector(Coord(x, y))
}

private fun String.parsePrize(): LongCoord {
    val (_, coords) = split(": ")
    val (x, y) = coords.split(", ").map { it.drop(2).toLong() }
    return LongCoord(x, y)
}


fun main() {
    Day13().run()
}
