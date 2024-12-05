package aoc2024

import AocRunner
import InputType
import LineParser
import kotlin.math.abs

typealias Levels = List<Int>

fun Levels.subReports() = List(size) { index: Int ->
    toMutableList().apply { removeAt(index) }
}

fun Levels.isSafe(): Boolean {
    return allSameSign() && allBetweenOneAndThree()
}

private fun Levels.allSameSign(): Boolean {
    val sign = this[1] - this[0]
    return this.zipWithNext().all { (a, b) -> (b - a) * sign > 0 }
}

private fun Levels.allBetweenOneAndThree(): Boolean {
    return this.zipWithNext().all { (a, b) -> abs(b - a) in (1..3) }
}

class Day2Parser : LineParser<Levels> {
    override fun parseLine(index: Int, line: String): Levels {
        return line.split(" ").map { it.toInt() }
    }
}

class Day2(inputType: InputType = InputType.FINAL) : AocRunner<Levels, Long>(
    2,
    Day2Parser(),
    inputType
) {
    override fun partOne(): Long {
        return lines.count { it.isSafe() }.toLong()
    }

    override fun partTwo(): Long {
        return lines.count { initialReport ->
            initialReport.isSafe() || initialReport.subReports().any { it.isSafe() }
        }.toLong()
    }
}

fun main() {
    Day2().run()
}
