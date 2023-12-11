
data class DayXXXLine(
    val line: String,
) {

}

class DayXXXParser : LineParser<DayXXXLine> {
    override fun parseLine(index: Int, line: String): DayXXXLine {
        return DayXXXLine(
            line,
        )
    }
}

class DayXXX(inputType: InputType = InputType.FINAL) : AocRunner<DayXXXLine, Long>(
    12, // FIXME : bien penser à changer
    DayXXXParser(),
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
    DayXXX().run()
}
