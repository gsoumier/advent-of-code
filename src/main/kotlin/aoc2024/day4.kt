package aoc2024

import AocRunner
import Direction
import InputType
import StringLineParser
import toCharMap

class Day4(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    4,
    StringLineParser,
    inputType
) {

    private val charMap = lines.toCharMap()

    override fun partOne(): Long {
        return charMap.charPoints
            .filter { it.value == 'X' }
            .sumOf { charPoint ->
                Direction.extended()
                    .mapNotNull { direction -> charMap.getWord(charPoint.coord, direction, 4) }
                    .count { it == "XMAS" }
            }.toLong()
    }

    override fun partTwo(): Long {
        return charMap.charPoints
            .filter { it.value == 'A' }
            .count { charPoint ->
                Direction.interCardinals()
                    .mapNotNull { direction ->
                        charMap.getWord(charPoint.coord.to(direction), direction.opposite(), 3)
                    }.count { it == "MAS" } == 2
            }.toLong()
    }

}


fun main() {
    Day4().run()
}
