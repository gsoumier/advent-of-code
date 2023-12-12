data class Day12Line(
    val damagedString: String,
    val damagedValues: List<Int>
) {



}

fun Pair<Char, Char>.matches(): Boolean {
    return second == '?' || first == second
}

fun String.matches(damageString: String): Boolean {
    return zip(damageString).all {
        it.matches()
    }
}

fun List<Int>.minLength() = sum() + size - 1

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

    fun countFoldArrangments(damageString: String, damageList: List<Int>, foldNb: Int = 1): Long {
        val intRange = 0..<foldNb
        val foldDamage = intRange.joinToString("?") { damageString }
        val foldValues = intRange.flatMap { damageList }

        return countArrangments(foldDamage, foldValues)
    }

    fun countArrangments(damageString: String, damageList: List<Int>): Long {
        var res = 0L
        if (damageList.isEmpty()) {
            return if(damageString.contains('#')) 0L else 1L
        }

        val simpleDamageString = damageString.dropWhile { it == '.' }
            .takeIf { it.length >= damageList.minLength() }
            ?: return 0

        calculated[cacheKey(simpleDamageString, damageList)]?.let { return it }

        val placedDamages = simpleDamageString.count { it == '#' }
        val potentialDamages = simpleDamageString.count { it == '?' }
        damageList.sum().takeIf { it in placedDamages..placedDamages+potentialDamages }
            ?: return 0

        val remainingDamage = damageList.drop(1)
        val minForNext = remainingDamage.minLength()
        val damage = damageList.first()
        var i = 0
        while (i + damage + 1 + minForNext <= simpleDamageString.length) {
            val start = (0..<i).map { '.' }.joinToString("") + stringOfLength(damage) + (".".takeIf { remainingDamage.isNotEmpty() } ?: "")
            if (start.matches(simpleDamageString.substring(start.indices)))
                res += countArrangments(simpleDamageString.substring(start.length), remainingDamage)
            i++
        }
        calculated[cacheKey(simpleDamageString, damageList)] = res
        return res
    }

    private fun stringOfLength(damage: Int, c: Char = '#') = (0..<damage).map { c }
        .joinToString("")

    private fun cacheKey(damageString: String, damageList: List<Int>): String {
        return damageString + " " + damageList.joinToString(",")
    }


    override fun partOne(): Long {
        return lines.sumOf { countFoldArrangments(it.damagedString, it.damagedValues) }
    }

    override fun partTwo(): Long {
        return lines.sumOf { countFoldArrangments(it.damagedString, it.damagedValues, 5) }
    }

}


fun main() {
    Day12().run()
}
