package aoc2024

import AocRunner
import Coord
import Direction
import InputType
import LineParser
import StringLineParser
import dijkstra
import toCharMap


class Day20(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    20,
    StringLineParser,
    inputType
) {

    val map = lines.toCharMap()

    val start = map.charPoints.find { it.value == 'S' }!!.coord
    val end = map.charPoints.find { it.value == 'E' }!!.coord

    val dijikstra = dijkstra(map, start, nodeBuilder = { it.charPoint.coord })

    val path = dijikstra[end]!!.possiblePaths.first()

    val minCheat = if (inputType == InputType.FINAL) 100 else 10
    val part2MinCheat = if (inputType == InputType.FINAL) 100 else 70

    override fun partOne(): Long {
        return path.dropLast(1)
            .mapIndexed { index, coord -> index to coord }
            .sumOf { indexedCoord -> Direction.cardinals().count { indexedCoord.hasCheat(it) } }.toLong()
    }

    override fun partTwo(): Long {
        return path.dropLast(1)
            .mapIndexed { index, coord -> index to coord }
            .flatMap { from ->
                path.mapIndexed { endIndex, endCoord -> endIndex to endCoord }
                    .drop(from.first + part2MinCheat)
                    .map { to-> Shortcut(from, to) }
            }.count { it.isValid(part2MinCheat) }.toLong()
    }

    private fun Pair<Int, Coord>.hasCheat(direction: Direction): Boolean {
        return map[second.to(direction)]?.value == '#'
                && path.indexOf(second.to(direction, 2)) - first > minCheat
    }
}

typealias Distance = Int

data class Shortcut(val from: Pair<Distance, Coord>, val to: Pair<Distance, Coord>) {
    val distance = from.second.distanceTo(to.second)
    fun isValid(minToWin: Int = 100): Boolean {
        return distance <= 20 && to.first - from.first - distance >= minToWin
    }
}

fun main() {
    Day20().run()
}
