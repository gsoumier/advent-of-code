val digits = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

fun main(args: Array<String>) {
    print(object {}.javaClass.getResourceAsStream("day1.txt").bufferedReader().readLines().sumOf { line ->
        val firstLast = ("" + line.withFirstDigitReplaced().first { it.isDigit() } + line.withLastDigitReplaced()
            .last { it.isDigit() }).toInt()
        println(line)
        println(firstLast)
        println("-----")
        firstLast
    }
    )
}

private fun String.withFirstDigitReplaced(): String {
    return digits.keys
        .map { it to indexOf(it) }
        .filter { it.second >= 0 }
        .minByOrNull { (_, v) -> v }
        ?.let { replaceFirst(it.first, digits[it.first]!!) } ?: this
}

private fun String.withLastDigitReplaced(): String {
    return digits.keys
        .map { it to lastIndexOf(it) }
        .filter { it.second >= 0 }
        .maxByOrNull { (_, v) -> v }
        ?.let { replaceRange(it.second, it.second + it.first.length, digits[it.first]!!) } ?: this
}

