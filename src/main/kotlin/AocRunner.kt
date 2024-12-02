import kotlin.system.measureTimeMillis

interface LineParser<T>{
    fun parseLine(index: Int, line: String) : T?
    fun parseLine(line: String) : T? = parseLine(0, line)
}

object StringLineParser : LineParser<String> {
    override fun parseLine(index: Int, line: String): String = line
}

abstract class AocRunner<T, Out>(day: Int, private val parser: LineParser<T>, inputType: InputType = InputType.FINAL) {

    val stringList = aocInputStream(day, inputType).bufferedReader().readLines()
    val lines: List<T> = stringList
        .mapIndexedNotNull { index, line -> parser.parseLine(index, line) }
    val nbCols = stringList.firstOrNull()?.length ?: 0
    val nbLines = stringList.size

    abstract fun partOne() : Out
    abstract fun partTwo() : Out

    val isDebug = inputType == InputType.SAMPLE

    fun run() {
        val part1TimeInMillis = measureTimeMillis { println("Part 1 result: ${partOne()}") }
        println("Part 1 duration $part1TimeInMillis ms")
        val part2TimeInMillis = measureTimeMillis { println("Part 2 : ${partTwo()}") }
        println("Part 2 duration $part2TimeInMillis ms")
    }
}
