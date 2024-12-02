package aoc2023

import AocRunner
import InputType
import LineParser

data class Day8Line(
    val place: String,
    val nextPlaces: Pair<String, String>
)

fun Pair<String, String>.next(instruction: Char): String =
    when (instruction) {
        'L' -> first
        else -> second
    }

class Day8Parser : LineParser<Day8Line> {
    private val regexp = Regex("""\w{3}""")

    override fun parseLine(index: Int, line: String): Day8Line? {
        return index.takeIf { it > 1 }
            ?.let { regexp.findAll(line).map { it.value }.toList() }
            ?.let { Day8Line(it[0], it[1] to it[2]) }
    }
}

class Day8(inputType: InputType = InputType.FINAL) : AocRunner<Day8Line, Long>(8, Day8Parser(), inputType) {
    private val places = lines.associate { it.place to it.nextPlaces }
    private val instructions = stringList[0]

    private fun followInstructions(origin: String): String {
        var currentInstruction = instructions
        var place = origin
        while (currentInstruction.isNotEmpty()) {
            place = places[place]?.next(currentInstruction[0]) ?: error("Place not found $place")
            currentInstruction = currentInstruction.drop(1)
        }
        return place
    }

    private fun getNbIterToDest(origin: String, isDest: (String) -> Boolean = { it == "ZZZ" }): Long {
        var nbIter = 0L
        var place = origin
        while (!isDest(place)) {
            nbIter++
            place = followInstructions(place)
        }
        return nbIter
    }

    private fun Long.toTotalNbSteps() = this * instructions.length

    override fun partOne(): Long {
        return getNbIterToDest("AAA").toTotalNbSteps()
    }

    override fun partTwo(): Long {
        val origins = lines.filter { it.place.last() == 'A' }.map { it.place }
        return origins.map { origin ->
            getNbIterToDest(origin) { it.endsWith('Z') }
        }.reduce { acc, l -> acc * l }.toTotalNbSteps()
    }
}


fun main() {
    Day8().run()
}
