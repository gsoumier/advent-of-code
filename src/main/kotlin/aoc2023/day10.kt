package aoc2023

import AocRunner
import CharPoint
import Coord
import Direction
import InputType
import StringLineParser
import toCharMap
import kotlin.math.min


/**
 * | is a vertical pipe connecting north and south.
 * - is a horizontal pipe connecting east and west.
 * L is a 90-degree bend connecting north and east.
 * J is a 90-degree bend connecting north and west.
 * 7 is a 90-degree bend connecting south and west.
 * F is a 90-degree bend connecting south and east.
 * . is ground; there is no pipe in this CharPoint.
 * S is the starting position
 */
enum class TileType(val value: Char, val from: Direction, val to: Direction) {
    VERTICAL_PIPE('|', Direction.N, Direction.S),
    HORIZONTAL_PIPE('-', Direction.E, Direction.W),
    TURN_L('L', Direction.N, Direction.E),
    TURN_J('J', Direction.N, Direction.W),
    TURN_7('7', Direction.S, Direction.W),
    TURN_F('F', Direction.S, Direction.E);

    fun from(direction: Direction): Direction? {
        if (direction == from) return to
        if (direction == to) return from
        return null
    }
}

val turns = setOf(
    TileType.TURN_L,
    TileType.TURN_J,
    TileType.TURN_7,
    TileType.TURN_F,
)

fun CharPoint.type(): TileType? = aoc2023.TileType.entries.find { it.value == value }

fun CharPoint.whereTo(from: Direction): Direction? = type()?.from(from.opposite())
    

class Day10(inputType: InputType = InputType.FINAL) : AocRunner<String, Int>(
    10,
    StringLineParser,
    inputType
) {

    val charMap = lines.toCharMap()

    val start = charMap.find { it.value == 'S' } ?: error("No start found")

    fun way(startingDirection: Direction): List<CharPoint>? {
        val res = mutableListOf<CharPoint>()
        var currentDirection = startingDirection
        var current = start.getTile(currentDirection) ?: return null
        current.whereTo(currentDirection) ?: return null
        while (current.value != 'S') {
            res.add(current)
            currentDirection = current.whereTo(currentDirection) ?: error("No way")
            current = current.getTile(currentDirection) ?: error("Outside map")
        }
        return res
    }

    private fun CharPoint.getTile(direction: Direction) =
        charMap[coord.to(direction)]

    override fun partOne(): Int {
        val ways = getWays()
        return ways.minOf { (it.size + 1) / 2 }
    }

    private fun getWays() = Direction.entries.mapNotNull { way(it) }

    private fun Coord.isInLine(border: List<CharPoint>): Boolean {
        val borderEncountered = border
            .filter { it.coord.y == y && it.coord.x < x }
            .filter { it.value != '-' }
        val nbN = borderEncountered.countFrom(Direction.N)
        val nbS = borderEncountered.countFrom(Direction.S)
        val count = borderEncountered.count { it.value == '|' } + min(nbN, nbS)
        return count % 2 != 0
    }

    private fun Coord.isInCol(border: List<CharPoint>): Boolean {
        val borderEncountered = border
            .filter { it.coord.y < y && it.coord.x == x }
            .filter { it.value != '|' }
        val nbW = borderEncountered.countTo(Direction.W)
        val nbE = borderEncountered.countTo(Direction.E)
        val count = borderEncountered.count { it.value == '-' } + min(nbE, nbW)
        return count % 2 != 0
    }

    private fun List<CharPoint>.countTo(direction: Direction) =
        count { border -> border.type()?.takeIf { it in turns }?.to == direction }


    private fun List<CharPoint>.countFrom(direction: Direction) =
        count { border -> border.type()?.takeIf { it in turns }?.from == direction }


    override fun partTwo(): Int {
        val border = getWays().first() + start.copy(value = '|')
        val res = charMap.charPoints.filter { it !in border }.filter {
            it.coord.isInLine(border) && it.coord.isInCol(border)
        }
        return res.count()
    }

}


fun main() {
    Day10().run()
}
