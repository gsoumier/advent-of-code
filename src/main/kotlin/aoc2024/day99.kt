package aoc2024

import AocRunner
import InputType
import LineParser

data class Day99Line(
    val line: String,
) {

}

class Day99Parser : LineParser<Day99Line> {
    override fun parseLine(index: Int, line: String): Day99Line {
        return Day99Line(
            line,
        )
    }
}

class Day99(inputType: InputType = InputType.FINAL) : AocRunner<Day99Line, Long>(
    99,
    Day99Parser(),
    inputType
) {
    override fun partOne(): Long {
        TODO("Not yet implemented")
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }

}


fun main() {
    Day99().run()
}
