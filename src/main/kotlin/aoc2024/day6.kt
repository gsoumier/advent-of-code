package aoc2024

import AocRunner
import CharMap
import Coord
import Direction
import InputType
import StringLineParser
import NavigationPoint
import toCharMap


data class Patrol(
    val visited: MutableList<NavigationPoint>,
) {
    constructor(initialCoord: Coord) : this(mutableListOf(NavigationPoint(initialCoord, Direction.N)))

    val optVisited = mutableSetOf<String>()

    fun add(next: NavigationPoint): Patrol? {
        if (optVisited.contains(next.toString()))
            return null
        visited.add(next)
        optVisited.add(next.toString())
        return this
    }

    fun turn(): Patrol = apply { visited.removeLast().let { visited.add(it.turnRight()) } }
}

class Day6(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    6,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()
    val initial: Patrol
        get() = Patrol(map.find { it.value == '^' }!!.coord)

    override fun partOne(): Long {
        val finalPatrol = doPatrol(map, initial)!!
        return finalPatrol.visited.map { it.coord }.distinct().size.toLong()
    }

    override fun partTwo(): Long {
        val distinct = doPatrol(map, initial)!!.visited.drop(1).map { it.coord }.distinct()
        return distinct.count { doPatrol(map.changeChar(it, '#'), initial) == null }.toLong()
    }
}

fun doPatrol(charMap: CharMap, patrol: Patrol): Patrol? {
    val next = patrol.visited.last().goStraight()
    val (_, value) = charMap[next.coord] ?: return patrol
    return doPatrol(charMap, if (value == '#') patrol.turn() else patrol.add(next) ?: return null)
}


fun main() {
    Day6().run()
}
