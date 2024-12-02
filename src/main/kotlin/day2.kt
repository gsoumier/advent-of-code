import kotlin.math.abs


typealias Levels = List<Int>

data class Report(
    val levels: Levels,
) {
    fun subReports() = List(levels.size) { index: Int ->
        Report(levels.toMutableList().apply { removeAt(index) })
    }
}

private fun Report.isSafe(): Boolean {
    return levels.allSameSign() && levels.allBetweenOneAndThree()
}

private fun Levels.allSameSign(): Boolean {
    val sign = this[1] - this[0]
    return this.zipWithNext().all { (a, b) -> (b - a) * sign > 0 }
}

private fun Levels.allBetweenOneAndThree(): Boolean {
    return this.zipWithNext().all { (a, b) -> abs(b - a) in (1..3) }
}

class Day2Parser : LineParser<Report> {
    override fun parseLine(index: Int, line: String): Report {
        return Report(
            line.split(" ").map { it.toInt() },
        )
    }
}

class Day2(inputType: InputType = InputType.FINAL) : AocRunner<Report, Long>(
    2,
    Day2Parser(),
    inputType
) {
    override fun partOne(): Long {
        return lines.count { it.isSafe() }.toLong()
    }

    override fun partTwo(): Long {
        return lines.count { initialReport ->
            initialReport.isSafe() || initialReport.subReports().any { it.isSafe() }
        }.toLong()
    }


}


fun main() {
    Day2().run()
}
