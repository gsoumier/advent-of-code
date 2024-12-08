package aoc2024

import AocRunner
import InputType
import LineParser
import aoc2024.Operator.*

enum class Operator {
    PLUS {
        override fun apply(a: Long, b: Long) = a + b
    },
    MULTIPLY {
        override fun apply(a: Long, b: Long) = a * b
    },
    CONCAT {
        override fun apply(a: Long, b: Long) = "$a$b".toLong()
    };

    abstract fun apply(a: Long, b: Long): Long
}

val cache = mutableMapOf<String, List<List<Operator>>>()

data class Day7Line(
    val result: Long,
    val values: List<Long>,
) {
    fun hasResolutions(allowedOperators: List<Operator>): Boolean {
        return possibleOperators(values.size - 1, allowedOperators).any { calculate(it) == result }
    }

    private fun calculate(operators: List<Operator>): Long {
        return operators.foldIndexed(values.first()) { index, acc, operator -> operator.apply(acc, values[index + 1]) }
    }

}

fun possibleOperators(size: Int, allowedOperators: List<Operator>): List<List<Operator>> {
    val cacheKey = allowedOperators.joinToString { it.name } + size
    cache[cacheKey]?.let { return it }
    if (size == 1)
        return allowedOperators.map { listOf(it) }
    return possibleOperators(size - 1, allowedOperators)
        .flatMap { allowedOperators.map { op -> listOf(op) + it } }
        .also { cache[cacheKey] = it }
}

class Day7Parser : LineParser<Day7Line> {
    override fun parseLine(index: Int, line: String): Day7Line {
        val (result, values) = line.split(": ")
        return Day7Line(
            result.toLong(), values.split(" ").map { it.toLong() },
        )
    }
}

class Day7(inputType: InputType = InputType.FINAL) : AocRunner<Day7Line, Long>(
    7,
    Day7Parser(),
    inputType
) {
    override fun partOne(): Long {
        return lines.filter { it.hasResolutions(listOf(PLUS, MULTIPLY)) }.sumOf { it.result }
    }

    override fun partTwo(): Long {
        return lines.filter { it.hasResolutions(Operator.entries) }.sumOf { it.result }
    }

}


fun main() {
    Day7().run()
}
