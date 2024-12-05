package aoc2024

import AocRunner
import InputType
import StringLineParser

class Day3(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    3,
    StringLineParser,
    inputType
) {
    private val pattern = """mul\((\d+),(\d+)\)""".toRegex()

    override fun partOne(): Long {
        return lines.mul()
    }

    override fun partTwo(): Long {
//        return lines.flatMap { extractDo(it) }.mul()
        return extractDo(lines.joinToString("")).mul()
    }

    private fun List<String>.mul() =
        flatMap { pattern.findAll(it) }.sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }

}

fun extractDo(instr: String): List<String> {
    val split = instr.split("don't()")
    return listOf(split.first()) + split.drop(1).flatMap { it.split("do()").takeIf { dontPart -> dontPart.size > 1 }?.drop(1) ?: emptyList() }
}


fun main() {
    Day3().run()
}
