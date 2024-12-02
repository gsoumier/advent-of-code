package aoc2023

import AocRunner
import Coord
import InputType
import StringLineParser
import getCols
import kotlin.math.max
import kotlin.math.min



private const val galaxy = '#'

class Day11(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    11,
    StringLineParser,
    inputType
) {

    private val galaxies = lines.flatMapIndexed { lineIndex: Int, s: String ->
        s.mapIndexedNotNull { charIndex, c ->
            Coord(
                charIndex,
                lineIndex
            ).takeIf { c == galaxy }
        }
    }
    private val cols = lines.getCols()

    private val emptyLines = lines.mapIndexedNotNull { index, s -> index.takeUnless { s.contains(galaxy) } }
    private val emptyCols = cols.mapIndexedNotNull { index, s -> index.takeUnless { s.contains(galaxy) } }


    private fun distanceBetween(coord: Coord, other: Coord, emptyCount: Long = 2L): Long {
        val xDistance = axisDistance(coord, other, emptyCount, { it.x }, emptyCols)
        val yDistance = axisDistance(coord, other, emptyCount, { it.y }, emptyLines)
        return xDistance + yDistance
    }

    private fun axisDistance(coord: Coord, other: Coord, emptyCount: Long, axis: (Coord)-> Int, emptyCrossAxis: List<Int> ): Long {
        val minX = min(axis(coord), axis(other)).toLong()
        val maxX = max(axis(coord), axis(other)).toLong()
        val nbEmptyCols = emptyCrossAxis.count { it in minX..maxX }
        val axisDistance = maxX - minX - nbEmptyCols + nbEmptyCols * emptyCount
        return axisDistance
    }

    fun calculate(emptyCount: Long = 2L): Long {
        return galaxies.flatMapIndexed { index: Int, coord: Coord ->
            galaxies
                .filterIndexed { otherIndex, _ -> otherIndex > index }
                .map { otherCoord -> distanceBetween(coord, otherCoord, emptyCount = emptyCount) }
        }.sum()
    }

    override fun partOne(): Long {
        return calculate()
    }

    override fun partTwo(): Long {
        return calculate(1_000_000L)
    }
}


fun main() {
    Day11().run()
}
