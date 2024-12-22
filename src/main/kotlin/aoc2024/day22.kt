package aoc2024

import AocRunner
import InputType
import LineParser
import StringLineParser
import kotlin.math.absoluteValue

class Day22(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    22,
    StringLineParser,
    inputType
) {

    val secrets = lines.map { it.toLong() }

    val buyersEvolvePrices = mutableMapOf<Long, MutableMap<String, Int>>()


    override fun partOne(): Long {
        return secrets.sumOf { secret -> (1..2000).fold(secret) { s, _ -> s.evolve() } }
    }

    private fun evolutionBestPrice(secret: Long): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        val history = mutableListOf(secret % 10)
        var currentSecret = secret
        (1..2000).forEach {
            currentSecret = currentSecret.evolve()
            history.add(currentSecret % 10)
            history.takeIf { it.size > 4 }
                ?.takeLast(5)
                ?.zipWithNext { a, b -> b - a }
                ?.joinToString(",")
                ?.let {
                    result.compute(it) { _, v -> v ?: (currentSecret % 10).toInt() }
                }
        }
        return result
    }

    override fun partTwo(): Long {
        val evolutionsPrices: List<Map<String, Int>> = secrets.map { evolutionBestPrice(it) }
        val evolutionKeys = evolutionsPrices.flatMap { it.keys }.toSet()
        return evolutionKeys.maxOf { key -> evolutionsPrices.sumOf { it[key]?.toLong() ?: 0 } }
    }

}

internal fun Long.evolve1() = times(64).mix(this).prune()
internal fun Long.evolve2() = div(32).mix(this).prune()
internal fun Long.evolve3() = times(2048).mix(this).prune()
internal fun Long.evolve() = evolve1().evolve2().evolve3()

internal fun Long.mix(other: Long) = xor(other)

internal fun Long.prune() = this % 16777216

fun main() {
    Day22().run()
}
