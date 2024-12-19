package aoc2024

import AocRunner
import Coord
import Coordinates
import Direction
import InputType
import Path
import StringLineParser
import dijkstra
import toCharMap
import java.util.PriorityQueue

typealias Horizontal = Boolean

class Day16(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    16,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()

    val start = map.find { it.value == 'S' }!!.coord
    val end = map.find { it.value == 'E' }!!.coord

    override fun partOne(): Long {
        return dijkstraImpl().filter {
            it.key.coord == end
        }.minOf { it.value.distance }

}

override fun partTwo(): Long {
    val dijkstra = dijkstraImpl()
    return dijkstra
        .filter { it.key.coord == end }
        .minBy { it.value.distance }
        .value.possiblePaths
        .flatten()
        .map { it.coord }
        .distinct()
        .count().toLong()
}

data class Day16Node(val coord: Coord, val horizontal: Boolean) : Coordinates by coord

fun dijkstraImpl(): Map<Day16Node, Path<Day16Node>> {
    return dijkstra(
        map = map,
        initial = Day16Node(start, true),
        neighbourFilter = { it.charPoint.value in setOf('.', 'E') },
        distanceCalculator = { (prev, next) -> if (prev.horizontal == next.horizontal) 1 else 1001 },
        nodeBuilder = { Day16Node(it.charPoint.coord, it.direction in setOf(Direction.W, Direction.E)) }
    )
}
}


fun main() {
    Day16().run()
}
