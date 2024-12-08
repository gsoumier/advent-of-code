package aoc2024

import AocRunner
import Coord
import InputType
import StringLineParser
import Vector
import allPairs
import toCharMap


class Day8(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    8,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()

    override fun partOne(): Long {
        return antinodes(false)
    }

    private fun antinodes(multiple: Boolean) = map.charPoints
        .map { it.value }
        .filter { it != '.' }
        .distinct()
        .flatMap { antinodes(it, multiple) }
        .distinct()
        .count().toLong()

    private fun antinodes(antena: Char, multiple: Boolean = true): List<Coord> {
        val antenas = map.charPoints.filter { it.value == antena }.map { it.coord }
        val antinodes = antenas.allPairs()
            .map { (a, b) -> Vector(a, b) }
            .flatMap {
                antinodes(it.b, it, multiple) + antinodes(it.a, it.inverse(), multiple)
            }
            .filter { map[it] != null }
        return antinodes
    }

    private fun antinodes(c: Coord, v: Vector, multiple: Boolean): List<Coord> {
        if(!multiple)
            return listOf(c.to(v)).filter { map[it] != null }
        var times = 1
        val result = mutableListOf(v.a, v.b)
        var antinode = c.to(v, times)
        while(map[antinode] != null) {
            result.add(antinode)
            times++
            antinode = c.to(v, times)
        }
        return result
    }

    override fun partTwo(): Long {
        return antinodes(multiple = true)
    }

}


fun main() {
    Day8().run()
}
