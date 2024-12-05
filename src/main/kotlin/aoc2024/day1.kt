package aoc2024

import AocRunner
import InputType
import LineParser
import kotlin.math.abs

data class Day1Line(
    val n1: Long,
    val n2: Long,
) {

}

class Day1Parser : LineParser<Day1Line> {
    override fun parseLine(index: Int, line: String): Day1Line {
        val (n1, n2) = line.split("   ").map { it.toLong() }
        return Day1Line(
            n1, n2
        )
    }
}

class Day1(inputType: InputType = InputType.FINAL) : AocRunner<Day1Line, Long>(
    1,
    Day1Parser(),
    inputType
) {
    override fun partOne(): Long {
        return lines.map { it.n1 }.sorted().zip(lines.map { it.n2 }.sorted()).sumOf { abs(it.first - it.second) }
    }

    override fun partTwo(): Long {
        return lines.sumOf { leftLine ->
            leftLine.n1 * lines.count { leftLine.n1 == it.n2 }
        }
    }

}


fun main() {
    Day1().run()
}
