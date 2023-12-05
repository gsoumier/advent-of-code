
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

class DayXXX(inputType: InputType = InputType.FINAL) : AocRunner<DayXXXLine, Int>(
    6, // FIXME : bien penser à changer
    DayXXXParser(),
    inputType
) {
    override fun partOne(): Int {
        TODO("Not yet implemented")
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    override fun onEach(index: Int, line: DayXXXLine) {
        super.onEach(index, line)
    }
}


fun main() {
    DayXXX().run()
}
