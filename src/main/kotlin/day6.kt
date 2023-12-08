data class Day6Line(
    val values: List<Long>,
    val value: Long
) {

}

data class Race(val time: Long, val distanceRecord: Long) {
    fun distance(pushTime: Long): Long {
        return (time - pushTime) * pushTime
    }

    fun wayOfWinning(): List<Long> {
        return (1..<time).filter { distance(it) > distanceRecord }
    }
}

class Day6Parser : LineParser<Day6Line> {
    override fun parseLine(index: Int, line: String): Day6Line {
        val numbers = line.split(" ").filter { it.isNotBlank() }.drop(1)
        return Day6Line(
            numbers.map { it.toLong() },
            (numbers.joinToString("").toLong()),
        )
    }
}

class Day6(inputType: InputType = InputType.FINAL) : AocRunner<Day6Line, Long>(
    6, // FIXME : bien penser à changer
    Day6Parser(),
    inputType
) {
    val races = lines[0].values.zip(lines[1].values).map { Race(it.first, it.second) }
    val fullRace = Race(lines[0].value, lines[1].value)

    override fun partOne(): Long {
        return races.map { it.wayOfWinning().count() }.reduce { acc, i -> acc * i }.toLong()
    }

    override fun partTwo(): Long {
        return fullRace.wayOfWinning().count().toLong()
    }

}


fun main() {
    Day6().run()
}
