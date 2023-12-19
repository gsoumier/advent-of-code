import kotlin.math.max
import kotlin.math.min

data class Day18Line(
    val direction: Direction,
    val length: Int,
    val color: String
) {
    val realDirection: Direction = when (color.last()) {
        '0' -> Direction.E
        '1' -> Direction.S
        '2' -> Direction.W
        else -> Direction.N
    }

    @OptIn(ExperimentalStdlibApi::class)
    val realLength: Int = color.dropLast(1).hexToInt()
}

class Day18Parser : LineParser<Day18Line> {
    override fun parseLine(index: Int, line: String): Day18Line {
        return line.split(" ").let {
            Day18Line(
                it[0].toDir(),
                it[1].toInt(),
                it[2].drop(2).dropLast(1)
            )
        }
    }
}

private fun String.toDir(): Direction {
    return when (this) {
        "R" -> Direction.E
        "D" -> Direction.S
        "L" -> Direction.W
        else -> Direction.N
    }
}

class Day18(inputType: InputType = InputType.FINAL) : AocRunner<Day18Line, Long>(
    18,
    Day18Parser(),
    inputType
) {
    override fun partOne(): Long {
        return countFromBigBorders(false)
    }

    fun List<Border>.isIn(): Boolean {
        val b = groupBy { it }.mapValues { it.value.size }
        return (b.count(Border.CROSS_DIR) + min(
            b.count(Border.EDGE_LEFT),
            b.count(Border.EDGE_RIGHT)
        )) % 2 != 0
    }

    private fun Map<Border, Int>.count(border: Border): Int =
        getOrDefault(border, 0)

    enum class Border {
        EDGE_RIGHT,
        EDGE_LEFT,
        SAME_DIR,
        CROSS_DIR
    }
    data class BigBorder(val startCoord: Coord, val endCord: Coord) {
        fun isVertical() = startCoord.x == endCord.x
        fun horizontalBorderType(x: Int) : Border {
            return when(x){
                startX() -> Border.EDGE_LEFT
                endX() -> Border.EDGE_RIGHT
                else -> Border.CROSS_DIR
            }
        }
        fun verticalBorderType(y: Int) : Border {
            return when(y){
                startY() -> Border.EDGE_LEFT
                endY() -> Border.EDGE_RIGHT
                else -> Border.CROSS_DIR
            }
        }
        fun startX() = min(startCoord.x, endCord.x)
        fun endX() = max(startCoord.x, endCord.x)
        fun startY() = min(startCoord.y, endCord.y)
        fun endY() = max(startCoord.y, endCord.y)
        fun xRange() = startX()..endX()
        fun yRange() = startY()..endY()
    }

    override fun partTwo(): Long {

        return countFromBigBorders(true)
    }

    private fun toCoord(it: Coord, opp: Day18Line, part2: Boolean) = if(part2) it.to(opp.realDirection, opp.realLength) else it.to(opp.direction, opp.length)

    private fun countFromBigBorders(part2: Boolean): Long {
        val bigBorderList = lines.fold(
            emptyList<BigBorder>()
        ) { acc, opp ->
            acc + (acc.lastOrNull()?.endCord ?: Coord(0, 0)).let {
                BigBorder(it, toCoord(it, opp, part2))
            }
        }
        val verticals = bigBorderList.filter { it.isVertical() }
        val horizontals = bigBorderList.filter { !it.isVertical() }

        val interrestingX = horizontals.flatMap { listOf(it.startCoord, it.endCord) }.map { it.x }.toSet().sorted()

        var insideCount = 0L
        interrestingX.dropLast(1).forEachIndexed { xIndex, x ->
            val endX = interrestingX[xIndex + 1]
            val interrestingY = horizontals.filter {
                it.endX() > x || endX > it.startX()
            }.map { it.startCoord.y }.toSet().sorted()
            interrestingY.dropLast(1).forEachIndexed { yIndex, y ->
                val endY = interrestingY[yIndex + 1]
                val width = endX - x - 1
                val height = endY - y - 1
                if (Coord(x, y).let { it.isNotBorder(bigBorderList) && it.isInsideBorder(verticals, horizontals) }) {
                    insideCount += 1.also {
//                        println("$it pour horizontal x [$x, $endX] y [$y, $endY]")
                    }
                }
                if (Coord(x + 1, y).let { it.isNotBorder(horizontals) && it.isInsideBorder(verticals, horizontals) }) {
                    insideCount += width.also {
//                        println("$it pour horizontal x [$x, $endX] y [$y, $endY]")
                    }
                }
                if (Coord(x, y + 1).let { it.isNotBorder(verticals) && it.isInsideBorder(verticals, horizontals) }) {
                    insideCount += height.also {
//                        println("$it pour vertical x [$x, $endX] y [$y, $endY]")
                    }
                }
                if (width > 0 && height > 0 && Coord(x + 1, y + 1).isInsideBorder(verticals, horizontals)) {
                    insideCount += (width.toLong() * height.toLong()).also {
//                        println("$it pour carre x [$x, $endX] y [$y, $endY]")
                    }
                }
            }
        }
        val borderCount = bigBorderList.sumOf { it.startCoord.distanceTo(it.endCord).toLong() }
        return insideCount + borderCount
    }

    private fun Coord.isInsideBorder(verticals: List<BigBorder>, horizontals: List<BigBorder>): Boolean {
        return (isInColBigBorder(horizontals) && isInLineBigBorder(verticals)).also {
//            println("Coord $this is inside : $it")
        }
    }


    private fun Coord.isInColBigBorder(horizontals: List<BigBorder>): Boolean {
        return horizontals
            .filter { it.startY() < y && x in it.xRange()}
            .map { it.horizontalBorderType(x) }
            .isIn()
    }

    private fun Coord.isInLineBigBorder(verticals: List<BigBorder>): Boolean {
        return verticals
            .filter { it.startX() < x && y in it.yRange()}
            .map { it.verticalBorderType(y) }
            .isIn()
    }
}

private fun Coord.isNotBorder(borders: List<Day18.BigBorder>): Boolean {
    return borders.none { x in it.xRange() && y in it.yRange() }
}


fun main() {
    Day18().run()
}
