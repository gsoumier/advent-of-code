package aoc2024

import AocRunner
import CharMap
import Coord
import Direction
import InputType
import StringLineParser
import toCharMap

data class Patrol(
    val visited: List<Pair<Coord, Direction>>,
) {
    constructor(initialCoord: Coord) : this(listOf(initialCoord to Direction.N))
    fun add(next: Pair<Coord, Direction>): Patrol = Patrol(visited + next)
    fun turn(): Patrol = Patrol(visited.dropLast(1) + visited.last().let { it.first to it.second.quarterClockwise() })
    fun isInLoop(): Boolean = visited.count { it == visited.last() } > 1
}

fun Pair<Coord, Direction>.next(): Pair<Coord, Direction> = first.to(second) to second

class Day6(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    6,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()
    val initial = Patrol(map.find { it.value == '^' }!!.coord)

    override fun partOne(): Long {
        val finalPatrol = doPatrol(map, initial)!!
        return finalPatrol.visited.map { it.first }.distinct().size.toLong()
    }

    override fun partTwo(): Long {
        val distinct = doPatrol(map, initial)!!.visited.drop(1).map { it.first }.distinct()
        return distinct.count { doPatrol(map.changeChar(it, '#'), initial) == null }.toLong()
    }
}

fun doPatrol(charMap: CharMap, patrol: Patrol): Patrol? {
    if (patrol.isInLoop())
        return null
    val next = patrol.visited.last().next()
    val (_, value) = charMap[next.first] ?: return patrol
    if (value != '#') {
        return doPatrol(charMap, patrol.add(next))
    }
    return doPatrol(charMap, patrol.turn())

}


fun main() {
    Day6().run()
}
