package aoc2024

import AocRunner
import InputType
import LineParser
import StringLineParser
import splitWhen

internal typealias Pattern = String
internal typealias Design = String

class Day19(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    19,
    StringLineParser,
    inputType
) {

    val towelPatterns : Set<Pattern>
    val design : List<Design>

    val cache = mutableMapOf<Design, Long>()

    init {
        val (pat, des) = lines.splitWhen { it.isEmpty() }
        towelPatterns = pat.first().split(", ").toSet()
        design = des
    }

    override fun partOne(): Long {
        return design.count { findPossiblesPattern(it) > 0 }.toLong()
    }

    fun findPossiblesPattern(design: Design): Long {
        if(design.isEmpty())
            return 1
        return cache[design] ?: towelPatterns.filter { design.startsWith(it) }
            .sumOf { pattern -> findPossiblesPattern(design.substringAfter(pattern)) }
            .also { cache.putIfAbsent(design, it) }
    }

    override fun partTwo(): Long {
        return design.sumOf { findPossiblesPattern(it) }
    }

}


fun main() {
    Day19().run()
}
