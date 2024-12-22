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
        "X0A",
    ).toCharMap()

    val directionalMap = listOf(
        "X^A",
        "<v>",
    ).toCharMap()

    override fun partOne(): Long {
        return lines.sumOf { findFullBestCombination(it) * it.dropLast(1).toLong() }
    }

    override fun partTwo(): Long {
        return lines.sumOf { findFullBestCombination(it, 25) * it.dropLast(1).toLong() }
    }


    fun findFullBestCombination(code: String, hazardRobotNumber: Int = 2): Long {
        val depressurizedCombinations = findBestCombination(code, numericMap, numericCombinations)
        return depressurizedCombinations.minOf { numeric ->
            numeric.split("A").map { "${it}A" }.dropLast(1).sumOf { findDirectionalBestCombination(it, hazardRobotNumber) }
        }.toLong()
    }

    private fun findDirectionalBestCombination(code: String, times : Int): Long {
        directionalCache[code to times]?.let { return it }
        if(times == 0) return code.length.toLong()
        return findBestCombination(code, directionalMap, directionalCombinations).keepMinLength().minOf { sequence ->
            sequence.split("A").map { "${it}A" }.dropLast(1).sumOf { findDirectionalBestCombination(it, times-1) }
        }.also { directionalCache[code to times] = it }
    }

    fun findBestCombination(code: String, charMap: CharMap, cache: MutableMap<Vector, Set<String>>): Set<String> {
        val vectors = toVectors(code, charMap)
        val allCombinations = vectors.fold(setOf("")) { acc, v ->
            acc.flatMap { comb ->
                combinations(v, charMap, cache).map { comb + it }
            }.toSet()
        }
        return allCombinations.keepMinLength()
    }

    private fun Set<String>.keepMinLength(): Set<String> {
        val minLength = minOf { it.length }
        return filter { it.length == minLength }.toSet()
    }

    fun combinations(vector: Vector, charMap: CharMap, cache: MutableMap<Vector, Set<String>>): Set<String> {
        cache[vector]?.let { return it }
        return vector.decompose().permutations().toList().toSet()
            .filter { isNotPassingGap(charMap, vector.a, it) }
            .map {
                it.fold("") { acc, d ->
                    acc + when (d) {
                        Direction.N -> "^"
                        Direction.E -> ">"
                        Direction.S -> "v"
                        else -> "<"
                    }
                } + "A"
            }
            .toSet()
            .also { cache[vector] = it }
    }

    private fun isNotPassingGap(charMap: CharMap, origin: Coord, dirs: List<Direction>): Boolean {
        var current = origin
        dirs.forEach {
            current = current.to(it)
            if (charMap[current]?.value == 'X') {
                return false
            }
        }
        return true
    }

    private fun toVectors(code: String, charMap: CharMap): List<Vector> {
        var current = charMap.find { it.value == 'A' }!!.coord
        return code.asSequence()
            .map { char -> charMap.charPoints.find { it.value == char }!! }
            .map { Vector(current, it.coord).also { current = it.b } }
            .toList()
    }


}


fun main() {
    Day21().run()
}
