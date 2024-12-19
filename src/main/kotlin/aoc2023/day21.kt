package aoc2023

import AocRunner
import CharMap
import Coord
import InputType
import StringLineParser
import pow2
import quotientAndReminder
import toCharMap

class Day21(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    21,
    StringLineParser,
    inputType
) {
    val charMap = lines.toCharMap()
    private val start = charMap.charPoints.filter { it.value == 'S' }.map { it.coord }.first()
    val initialState = GardenState(
        charMap,
        setOf(start),
        charMap.charPoints.filter { it.value == '#' }.map { it.coord }.toSet(),
    )

    override fun partOne(): Long {
        return initialState.positionsAfter(64).plots
    }


    override fun partTwo(): Long {
        return calculatePart2Method1(26501365)
    }

    fun calculatePart2Method1(nbTotalIter: Int, cyclicIters: Int = 2): Long {
        var state = initialState
        val cyclicLength = nbCols
        val (totalCyclicIters, nbIterMore) = nbTotalIter.quotientAndReminder(cyclicLength)

        (1..cyclicIters * 2).forEach { iter ->
            state = state.positionsAfter(cyclicLength, true)
            println("Iter $iter nb : ${state.plots}")
        }
        state = state.positionsAfter(nbIterMore, true)

        val numbersByMapCoord = state.getNumberByMapCoord()
        return ResultCalculator(
            numbersByMapCoord[Coord(1, 0)]!!,
            numbersByMapCoord[Coord(0, 0)]!!,
            numbersByMapCoord.sumCombinationsOf(cyclicIters, cyclicIters),
            numbersByMapCoord.sumCombinationsOf(2 * cyclicIters, 1),
            numbersByMapCoord.sumCombinationsOf(2 * cyclicIters, 0)
        ).calculateResult(totalCyclicIters)
    }

    /**
     * Ne fonctionne pas avec le cas de test !!!
     */
    fun calculatePart2Method2(nbTotalIter: Int): Long {
        val totalCyclicIters = nbTotalIter / nbLines
        val odd = nbTotalIter % 2

        println()
        (3..18).forEach { nb ->
            charMap.corners.sumOf { cornerCoord ->
                calculateFrom(
                    cornerCoord,
                    nb
                ).also { println("Test for corner $cornerCoord : $it") }
            }.also {
                println("Test sum for nb iter $nb : $it")
                println()
            }
        }

        return ResultCalculator(
            calculateFrom(start, 2 * nbLines + 1 - odd),
            calculateFrom(start, 2 * nbLines + odd),
            charMap.corners.sumOf { cornerCoord ->
                calculateFrom(cornerCoord, nbLines * 3 / 2 + 1 - odd - 1)
            },
            charMap.corners.sumOf { cornerCoord ->
                calculateFrom(cornerCoord, (nbLines) / 2 + 1 - odd - 1)
            },
            charMap.axis.sumOf { calculateFrom(it, nbLines - 1) },
        ).calculateResult(totalCyclicIters)
    }

    private fun calculateFrom(start: Coord, iters: Int): Long {
        val state = initialState.copy(possiblePlots = setOf(start))
        return state.positionsAfter(iters, false).plots
    }

    data class ResultCalculator(
        val evenCount: Long,
        val oddCount: Long,
        val bigTriangleCount: Long,
        val smallTriangleCount: Long,
        val axisTriangleCount: Long
    ) {
        fun calculateResult(nbIters: Int): Long {
            println(this)

            return (nbIters.pow2() * evenCount)
                .plus(nbIters.dec().pow2() * oddCount)
                .plus(nbIters.dec() * bigTriangleCount)
                .plus(nbIters * smallTriangleCount)
                .plus(axisTriangleCount)
        }
    }
}

private fun Map<Coord, Long>.sumCombinationsOf(i: Int, j: Int): Long {
    return setOf(
        Coord(i, j),
        Coord(j, -i),
        Coord(-j, i),
        Coord(-i, -j),
    ).mapNotNull { this[it] }.sum()
}

data class GardenState(
    private val charMap: CharMap,
    private val possiblePlots: Set<Coord>,
    private val rocks: Set<Coord>
) {

    fun positionsAfter(
        nbStep: Int,
        mapRepeatable: Boolean = false,
    ): GardenState {
        val res = (1..nbStep).fold(this) { state, _ ->
            state.copy(possiblePlots = state.possiblePlots
                .flatMap { state.charMap.neighboursInMap(it, mapRepeatable = mapRepeatable).map { it.charPoint.coord } }
                .filter { it.isNotRock(mapRepeatable) }
                .toSet()
            )
        }
        return res
    }

    fun getNumberByMapCoord(): Map<Coord, Long> {
        return possiblePlots.groupBy { charMap.mapCoord(it) }.mapValues { it.value.size.toLong() }
    }

    private fun Coord.isNotRock(mapRepeatable: Boolean = false) = charMap.get(this, mapRepeatable)?.value != '#'

    val plots: Long
        get() = possiblePlots.size.toLong()
}


fun main() {
    Day21().run()
}
