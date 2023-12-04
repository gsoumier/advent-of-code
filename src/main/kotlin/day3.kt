import kotlin.math.abs

data class Day3(
    val lineIndex: Int,
    val numbers: List<SchematicNumber>,
    val symbols: List<SchematicSymbol>,
)

data class SchematicNumber(val value: Int, val startIndex: Int, val endIndex: Int, val lineIndex: Int){
    fun isPartNumber(symbols: List<SchematicSymbol>) : Boolean{
        return symbols.any { isCloseTo(it) }
    }

    fun isCloseTo(symbol: SchematicSymbol): Boolean {
        if(abs(symbol.lineIndex - lineIndex) > 1) return false
        return (symbol.startIndex == endIndex || symbol.endIndex == startIndex) || symbol.startIndex >= startIndex && symbol.endIndex <= endIndex
    }
}

data class SchematicSymbol(val value: String, val startIndex: Int, val endIndex: Int, val lineIndex: Int)

fun String.toDay3(lineIndex: Int) : Day3 {
    return Day3(
        lineIndex,
        Regex("\\b(\\d+)\\b").findAll(this)
            .map { SchematicNumber(it.groupValues[1].toInt(), it.range.first, it.range.last +1, lineIndex) }.toList(),
        Regex("[^\\d.]").findAll(this)
            .map { SchematicSymbol(it.value, it.range.first, it.range.last +1, lineIndex) }.toList(),
        )
}

fun main(args: Array<String>) {
    var lineIndex = 0
    val lines = object {}.javaClass.getResourceAsStream("day3.txt").bufferedReader().readLines().map { it.toDay3(lineIndex++) }
    val numbers = lines.flatMap { it.numbers }
    val gears = lines.flatMap { it.symbols }.filter { it.value == "*" }
    print(gears.map { gear -> numbers.filter { it.isCloseTo(gear) } }.filter { it.size == 2 }.map { it.map { it.value }.reduce { acc, i -> acc * i } }.sum())
}
