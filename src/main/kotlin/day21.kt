data class Day21Line(
    val line: String,
) {

}

class Day21Parser : LineParser<Day21Line> {
    override fun parseLine(index: Int, line: String): Day21Line {
        return Day21Line(
            line,
        )
    }
}

class Day21(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    21,
    StringLineParser,
    inputType
) {
    val charMap = lines.toCharMap()

    val start = charMap.charPoints.filter { it.value == 'S' }.map { it.coord }.first()

    val initialState = GardenState(
        charMap,
        setOf(start),
        charMap.charPoints.filter { it.value == '#' }.map { it.coord }.toSet(),
    )

    override fun partOne(): Long {
        return initialState.positionsAfter().startingPos.size.toLong()
    }

    fun GardenState.positionsAfter(
        nbStep: Int = 64,
        infiniteMap: Boolean = false,
    ) = (1..nbStep).fold(this@positionsAfter) { state, _ ->
        state.addStep(infiniteMap)
    }


    override fun partTwo(): Long {
        val calculatePart2 = calculatePart2(26501365)
        println("Part 2 methode 1 $calculatePart2")
        val calculatePart2Bis = calculatePart2Method2(26501365)
        println("Part 2 methode 2 $calculatePart2Bis")
        return calculatePart2Bis
    }

    fun calculatePart2Direct(steps: Int) : Int {
        val realState = initialState.positionsAfter(steps, true)
        val coordIntMap = realState.getNumberByMapCoord().map { it.key to it.value }.groupBy { it.second }.mapValues { it.value.size }
        println(coordIntMap)
        val res = realState.startingPos.size
        println("Part 2 straight method $res")
        return res
    }

    fun calculatePart2(nbTotalIter: Int, cyclicIters: Int = 1): Long {
        var state = initialState
        val cyclicLength = nbCols
        val (totalCyclicIters, nbIterMore) = nbTotalIter.quotientAndReminder(cyclicLength)

        (1..cyclicIters * 2).forEach { iter ->
            state = state.positionsAfter(cyclicLength, true)
            println("Iter $iter nb : ${state.startingPos.size}")
        }
        state = state.positionsAfter(nbIterMore, true)

        val numbersByMapCoord = state.getNumberByMapCoord()
        return ResultCalculator(
            numbersByMapCoord[Coord(0, 0)]!!,
            numbersByMapCoord[Coord(1, 0)]!!,
            numbersByMapCoord.sumCombinationsOf(cyclicIters, cyclicIters),
            numbersByMapCoord.sumCombinationsOf(2 * cyclicIters, 1),
            numbersByMapCoord.sumCombinationsOf(2 * cyclicIters, 0)
        ).calculateResult(totalCyclicIters)
    }

    fun calculatePart2Method2(nbTotalIter: Int): Long {
        val totalCyclicIters = nbTotalIter/nbLines

        return ResultCalculator(
            calculateFrom(start, 2*nbLines),
            calculateFrom(start, 2*nbLines + 1),
            charMap.corners.sumOf { calculateFrom(it, nbLines * 3/2 - 1) },
            charMap.corners.sumOf { calculateFrom(it, nbLines/2 - 1) },
            charMap.axis.sumOf { calculateFrom(it, nbLines - 1) },
        ).calculateResult(totalCyclicIters)
    }

    private fun calculateFrom(start: Coord, iters: Int): Int {
        return initialState.copy(startingPos = setOf(start)).positionsAfter(iters, false).startingPos.size
    }

    data class ResultCalculator(
        val evenCount: Int,
        val oddCount: Int,
        val bigTriangleCount: Int,
        val smallTriangleCount: Int,
        val axisTriangleCount: Int
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

private fun Map<Coord, Int>.sumCombinationsOf(i: Int, j: Int): Int {
    return setOf(
        Coord(i, j),
        Coord(j, -i),
        Coord(-j, i),
        Coord(-i, -j),
    ).mapNotNull { this[it] }.sum()
}

data class GardenState(val charMap: CharMap, val startingPos: Set<Coord>, val rocks: Set<Coord>) {
    fun addStep(infiniteMap: Boolean): GardenState {
        return copy(startingPos = startingPos
            .flatMap { charMap.neighboursInMap(it, infiniteMap).map { it.second.coord } }
            .filter { it.isNotRock() }
            .toSet()
        )
    }

    fun getNumberByMapCoord(): Map<Coord, Int> {
        return startingPos.groupBy { charMap.mapCoord(it) }.mapValues { it.value.size }

    }

    private fun Coord.isNotRock() = charMap.get(this, true)?.value != '#'

}


fun main() {
    Day21().run()
}
