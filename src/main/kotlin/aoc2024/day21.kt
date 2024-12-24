package aoc2024

import AocRunner
import CharMap
import Coord
import Direction
import InputType
import StringLineParser
import Vector
import com.github.shiguruikai.combinatoricskt.permutations
import toCharMap

class Day21(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    21,
    StringLineParser,
    inputType
) {

    val numericCombinations = mutableMapOf<Vector, Set<String>>()
    val directionalCombinations = mutableMapOf<Vector, Set<String>>()

    val directionalCache = mutableMapOf<Pair<String, Int>, Long>()

    val numericMap = listOf(
        "789",
        "456",
        "123",
        " 0A",
    ).toCharMap()

    val directionalMap = listOf(
        " ^A",
        "<v>",
    ).toCharMap()

    override fun partOne(): Long {
        return lines.sumOf { findFullBestCombination(it, 2) * it.dropLast(1).toLong() }
    }

    override fun partTwo(): Long {
        return lines.sumOf { findFullBestCombination(it, 25) * it.dropLast(1).toLong() }
    }


    fun findFullBestCombination(code: String, hazardRobotNumber: Int): Long {
        val depressurizedCombinations = findBestCombination(code, numericMap, numericCombinations)
        return depressurizedCombinations.minOf { numeric ->
            numeric.split("A").map { "${it}A" }.dropLast(1).sumOf { findDirectionalBestCombination(it, hazardRobotNumber) }
        }.toLong()
    }

    private fun findDirectionalBestCombination(code: String, times : Int): Long {
        directionalCache[code to times]?.let { return it }
        if(times == 0) return code.length.toLong()
        return findBestCombination(code, directionalMap, directionalCombinations).minOf { sequence ->
            sequence.split("A").map { "${it}A" }.dropLast(1).sumOf { findDirectionalBestCombination(it, times-1) }
        }.also { directionalCache[code to times] = it }
    }

    fun findBestCombination(code: String, charMap: CharMap, cache: MutableMap<Vector, Set<String>>): Set<String> {
        return code.toVectors(charMap).fold(setOf("")) { acc, v ->
            acc.flatMap { comb ->
                v.combinations(charMap, cache).map { comb + it }
            }.toSet()
        }
    }

    private fun Vector.combinations(charMap: CharMap, cache: MutableMap<Vector, Set<String>>): Set<String> {
        cache[this]?.let { return it }
        return decompose().permutations().toList().toSet()
            .filter { isNotPassingGap(charMap, a, it) }
            .map { it.toArrowString() }
            .toSet()
            .also { cache[this] = it }
    }

    private fun List<Direction>.toArrowString() = fold("") { acc, d ->
        acc + when (d) {
            Direction.N -> "^"
            Direction.E -> ">"
            Direction.S -> "v"
            else -> "<"
        }
    } + "A"

    private fun isNotPassingGap(charMap: CharMap, origin: Coord, dirs: List<Direction>): Boolean {
        var current = origin
        dirs.forEach {
            current = current.to(it)
            if (charMap[current]?.value == ' ') {
                return false
            }
        }
        return true
    }

    private fun String.toVectors(charMap: CharMap): List<Vector> {
        var current = charMap.find { it.value == 'A' }!!.coord
        return asSequence()
            .map { char -> charMap.charPoints.find { it.value == char }!! }
            .map { Vector(current, it.coord).also { current = it.b } }
            .toList()
    }
}

fun main() {
    Day21().run()
}
