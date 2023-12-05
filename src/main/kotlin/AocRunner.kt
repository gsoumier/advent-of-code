import kotlin.system.measureTimeMillis

interface LineParser<T>{
    fun parseLine(index: Int, line: String) : T
    fun parseLine(line: String) : T = parseLine(0, line)
}

abstract class AocRunner<T, Out>(day: Int, val parser: LineParser<T>, inputType: InputType = InputType.FINAL) {

    val inputLines: List<T> = aocInputStream(day, inputType).bufferedReader().readLines()
        .mapIndexed { index, line -> parser.parseLine(index, line) }
        .onEachIndexed{ index, line -> onEach(index, line) }

    open fun onEach(index: Int, line: T) {

    }

    abstract fun partOne() : Out
    abstract fun partTwo() : Out

    fun run() {
        val part1TimeInMillis = measureTimeMillis { println("Part 1 result: ${partOne()}") }
        println("Part 1 duration $part1TimeInMillis ms")
        val part2TimeInMillis = measureTimeMillis { println("Part 2 : ${partTwo()}") }
        println("Part 2 duration $part2TimeInMillis ms")
    }
}
