package aoc2024

import AocRunner
import InputType
import StringLineParser

class Day11(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    11,
    StringLineParser,
    inputType
) {

    val initial = lines.first().split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toLong() }

    val after25Cache = mutableMapOf(
        0L to blinkManyTimes(listOf(0), 25),
        1L to blinkManyTimes(listOf(1), 25),
        2024L to blinkManyTimes(listOf(2024), 25),
    )

    override fun partOne(): Long {
        return blinkManyTimes(initial, 25).sumOf { it.second }.toLong()
    }

    fun blinkManyTimes(initial: List<Long>, times: Int = 25) = (0..<times).fold(initial) { a, _ -> blink(a) }.groupBy { it }.map { (k, v) -> k to v.size.toLong() }

    override fun partTwo(): Long {
        val times25 = blinkManyTimes(initial, 25)
        val time50: List<Pair<Long, Long>> = times25.flatMap { (stone, count) -> (after25Cache[stone] ?: blinkManyTimes(listOf(stone)).also { after25Cache[stone] = it }).map { it.first to count * it.second } }
            .groupBy { it.first }.map { (k, v) -> k to v.sumOf { it.second } }
        val time75: List<Pair<Long, Long>> = time50.flatMap { (stone, count) -> (after25Cache[stone] ?: blinkManyTimes(listOf(stone)).also { after25Cache[stone] = it }).map { it.first to count * it.second } }
            .groupBy { it.first }.map { (k, v) -> k to v.sumOf { it.second } }

        return time75.sumOf { it.second }.toLong()
    }

    fun blink(stones: List<Long>): List<Long> {
        return stones.flatMap {
            when {
                it == 0L -> listOf(1L)
                "$it".length % 2 == 0 -> "$it".let { stone ->
                    listOf(
                        stone.substring(0, stone.length / 2),
                        stone.substring(stone.length / 2)
                    ).map { it.toLong() }
                }

                else -> listOf(it * 2024)
            }
        }
    }
}




fun main() {
    Day11().run()
}
