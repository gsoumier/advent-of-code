import kotlin.math.max
import kotlin.math.min

class Day11Parser : LineParser<String> {
    override fun parseLine(index: Int, line: String): String {
        return line
    }
}

private const val galaxy = '#'

class Day11(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    11,
    Day11Parser(),
    inputType
) {

//    private val expendedUniverse = lines.expendUniverse(1)

    val galaxies = lines.flatMapIndexed { lineIndex: Int, s: String ->
        s.mapIndexedNotNull { charIndex, c ->
            Coord(
                charIndex,
                lineIndex
            ).takeIf { c == galaxy }
        }
    }
    val cols = (0..<lines.first().length).map { colIndex -> lines.map { it[colIndex] } }

    val emptyLines = lines.mapIndexedNotNull { index, s -> index.takeUnless { s.contains('#') } }
    val emptyCols = cols.mapIndexedNotNull { index, s -> index.takeUnless { s.contains('#') } }


    fun distanceBetween(coord: Coord, other: Coord, emptyCount: Long = 2L): Long {
        val minX = min(coord.x, other.x).toLong()
        val maxX = max(coord.x, other.x).toLong()
        val minY = min(coord.y, other.y).toLong()
        val maxY = max(coord.y, other.y).toLong()
        val nbEmptyLines = emptyCols.count { it in minX..maxX }
        val nbEmptyCol = emptyLines.count { it in minY..maxY }

        return maxX - minX - nbEmptyLines + nbEmptyLines * emptyCount + maxY - minY - nbEmptyCol + nbEmptyCol * emptyCount
    }


    private fun List<String>.expendUniverse(times: Int): List<String> {
        return expendLines(times).expendCols(times)
    }

    private fun List<String>.expendLines(times: Int): List<String> {
        val res = mutableListOf<String>()
        forEach {
            res.add(it)
            if (!it.contains(galaxy)) {
                (0..times).forEach { _ -> res.add(it) }
            }
        }
        return res
    }

    private fun List<String>.expendCols(times: Int): List<String> {
        var res = listOf<String>()
        (0..<first().length).forEach { colIndex ->
            val col = map { it[colIndex] }
            res = res.addCol(col)
            if (!col.contains(galaxy)) {
                (0..times).forEach { _ -> res = res.addCol(col) }
            }
        }
        return res
    }

    override fun partOne(): Long {
        return calculate()
    }

    override fun partTwo(): Long {
        return calculate(1_000_000L)
    }

    fun calculate(emptyCount: Long = 2L): Long {
        return galaxies.flatMapIndexed { index: Int, coord: Coord ->
            galaxies
                .filterIndexed { otherIndex, _ -> otherIndex > index }
                .map { otherCoord -> distanceBetween(coord, otherCoord, emptyCount = emptyCount) }
        }.sum()
    }



}

private fun List<String>.addCol(col: List<Char>): List<String> {
    return col.mapIndexed { index, c -> this.getOrElse(index) { "" } + c }
}


fun main() {
    Day11().run()
}
