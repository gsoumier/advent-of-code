package aoc2024

import AocRunner
import InputType
import StringLineParser
import allPairs
import splitWhen


class Day5(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    5,
    StringLineParser,
    inputType
) {
    private val allRules =
        lines.splitWhen { it.isEmpty() }[0].map { it.split("|").map { it.toInt() } }.map { it[0] to it[1] }
    private val updates = lines.splitWhen { it.isEmpty() }[1].map { it.split(",").map { it.toInt() } }

    override fun partOne() = updates
        .filter(::isCorrectOrder)
        .sumOf { it[it.size / 2] }.toLong()

    override fun partTwo() = updates
        .filterNot(::isCorrectOrder)
        .map { it.sortedWith(::compare) }
        .sumOf { it[it.size / 2] }.toLong()

    private fun isCorrectOrder(pages: List<Int>): Boolean {
        return pages.allPairs().all { (a, b) -> compare(a, b) > 0 }
    }

    private fun compare(a: Int, b: Int) = if(allRules.contains(b to a)) -1 else 1

}

fun main() {
    Day5().run()
}
