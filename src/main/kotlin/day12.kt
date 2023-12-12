data class Day12Line(
    val damagedString: String,
    val damagedValues: List<Int>
)

class Day12Parser : LineParser<Day12Line> {
    override fun parseLine(index: Int, line: String): Day12Line {
        val (str, values) = line.split(" ")
        return Day12Line(
            str,
            values.toNumberList(",")
        )
    }
}

class Day12(inputType: InputType = InputType.FINAL) : AocRunner<Day12Line, Long>(
    12,
    Day12Parser(),
    inputType
) {
    private val calculated = mutableMapOf<String, Long>()

    override fun partOne(): Long =
        lines.sumOf { countFoldArrangements(it.damagedString, it.damagedValues) }

    override fun partTwo(): Long =
        lines.sumOf { countFoldArrangements(it.damagedString, it.damagedValues, 5) }

    private fun countFoldArrangements(damageString: String, damageList: List<Int>, foldNb: Int = 1): Long {
        val intRange = 0..<foldNb
        val foldDamage = intRange.joinToString("?") { damageString }
        val foldValues = intRange.flatMap { damageList }

        return countArrangements(foldDamage, foldValues)
    }

    private fun countArrangements(damageString: String, damageList: List<Int>): Long {
        if (damageList.isEmpty()) {
            return if (damageString.contains('#')) 0L else 1L
        }
        calculated[cacheKey(damageString, damageList)]?.let { return it }

        val nextDamage = damageList.first()
        val remainingDamages = damageList.drop(1)
        val minForNext = remainingDamages.minLength()

        return (0..<damageString.length - nextDamage - minForNext).map { nbPoints ->
            candidate(nbPoints, nextDamage, remainingDamages.isNotEmpty())
        }.filter {
            it.matchesStartOf(damageString)
        }.sumOf { validCandidate ->
            countArrangements(damageString.substring(validCandidate.length).dropWhile { it == '.' }, remainingDamages)
        }.also {
            calculated[cacheKey(damageString, damageList)] = it
        }
    }

    private fun candidate(
        nbPoints: Int,
        nbDamage: Int,
        isLastToPosition: Boolean
    ) = "".padEnd(nbPoints, '.') + "".padEnd(nbDamage, '#') + (".".takeIf { isLastToPosition } ?: "")

    private fun cacheKey(damageString: String, damageList: List<Int>): String =
        damageString + " " + damageList.joinToString(",")
}

fun String.matchesStartOf(other: String): Boolean =
    zip(other.substring(other.indices)).all { it.matches() }

fun Pair<Char, Char>.matches(): Boolean =
    second == '?' || first == second

fun List<Int>.minLength() = sum() + size - 1

fun main() {
    Day12().run()
}
