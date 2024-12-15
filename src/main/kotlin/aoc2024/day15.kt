package aoc2024

import AocRunner
import CharMap
import CharPoint
import Direction
import InputType
import StringLineParser
import splitWhen
import toCharMap

class Day15(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    15,
    StringLineParser,
    inputType
) {
    val initialMap: CharMap
    val initialWideMap: CharMap
    val instructions: List<Direction>

    init {
        val (mapStr, instr) = lines.splitWhen { it.isEmpty() }
        initialMap = mapStr.toCharMap()
        initialWideMap = mapStr.map {
            it.map {
                when (it) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    else -> "@."
                }
            }.joinToString("")
        }.toCharMap()
        instructions = instr.flatMap {
            it.map {
                when (it) {
                    '^' -> Direction.N
                    '>' -> Direction.E
                    'v' -> Direction.S
                    else -> Direction.W
                }
            }
        }
    }

    override fun partOne(): Long {
        val finalMap = instructions.fold(initialMap, ::moveAll)
        return finalMap.calculateResult('O')
    }

    override fun partTwo(): Long {
        val finalMap = instructions.fold(initialWideMap, ::moveAll)
        return finalMap.calculateResult('[')
    }

    private fun CharMap.calculateResult(boxChar: Char) = charPoints.filter { it.value == boxChar }.sumOf {
        it.coord.let { c -> c.y * 100 + c.x }
    }.toLong()

    fun moveAll(map: CharMap, direction: Direction): CharMap {
        val robotPos = map.charPoints.first { it.value == '@' }
        val blocksToMove =
            blocksToMove(map, direction, listOf(listOf(robotPos)))?.flatten() ?: return map
        val newPos = blocksToMove.map { it.coord.to(direction) }
        val newEmptyBlock = blocksToMove.filter { it.coord !in newPos }
        return blocksToMove
            .fold(map) { acc, toMove ->
                acc.changeChar(toMove.coord.to(direction), toMove.value)
            }.let {
                newEmptyBlock.fold(it) { acc, empty ->
                    acc.changeChar(empty.coord, '.')
                }
            }
    }

    fun blocksToMove(map: CharMap, direction: Direction, blocksToMove: List<List<CharPoint>>): List<List<CharPoint>>? {
        val newBlocks: List<CharPoint> = blocksToMove.last()
            .map { map[it.coord.to(direction)]!! }
            .filter { !blocksToMove.last().contains(it) }
            .filter { it.value != '.' }
        if (newBlocks.any { it.value == '#' }) {
            return null
        }
        if (newBlocks.all { it.value == '.' }) {
            return blocksToMove
        }
        val allNewBlocks = newBlocks.flatMap {
            when (it.value) {
                '[' -> listOf(it, map[it.coord.to(Direction.E)]!!)
                ']' -> listOf(it, map[it.coord.to(Direction.W)]!!)
                else -> listOf(it)
            }
        }.distinct()
        return blocksToMove(map, direction, blocksToMove + listOf(allNewBlocks))
    }

}

fun main() {
    Day15().run()
}
