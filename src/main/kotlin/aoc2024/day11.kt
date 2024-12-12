package aoc2024

import AocRunner
import InputType
import StringLineParser

typealias BlinkTimes = Int
typealias GeneratedStones = Long
typealias Stone = Long

class Day11(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    11,
    StringLineParser,
    inputType
) {
    val initial = lines.first().split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toLong() }

    val cache = mutableMapOf<Stone, MutableMap<BlinkTimes, GeneratedStones>>().withDefault { mutableMapOf() }

    override fun partOne(): GeneratedStones {
        return initial.sumOf { it.blink(25) }
    }

    override fun partTwo(): GeneratedStones {
        return initial.sumOf { it.blink(75) }
    }

    private fun Stone.blink(times: BlinkTimes): GeneratedStones {
        if (times == 0) return 1

        cache[this]?.get(times)?.let { return it }

        return when {
            this == 0L -> listOf(1L)
            "$this".length % 2 == 0 -> "$this".let { stone ->
                listOf(
                    stone.substring(0, stone.length / 2),
                    stone.substring(stone.length / 2)
                ).map { it.toLong() }
            }
            else -> listOf(this * 2024)
        }.sumOf { it.blink(times - 1) }.also { generatedStones ->
            cache.putIfAbsent(this, mutableMapOf())
            cache[this]!![times] = generatedStones
        }
    }
}


fun main() {
    Day11().run()
}
