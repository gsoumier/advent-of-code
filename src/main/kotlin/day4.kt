fun Coord.to(direction: Next, forward: Int = 1): Coord {
    return copy(x = x + direction.pX * forward, y = y + direction.pY * forward)
}

class Day4(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    4,
    StringLineParser,
    inputType
) {

    val charMap = lines.toCharMap()
    val iter = lines.indices

    fun getWord(coord: Coord, dir: Next, forward: Int = 1, size: Int = 4): String? {
        return (0..<size).map {
            charMap.get(coord.to(dir, forward * it)) ?: return null
        }.joinToString("") { it.value.toString() }
    }

    override fun partOne(): Long {
        return charMap.charPoints.sumOf { charPoint ->
            listOfNotNull(
                getWord(charPoint.coord, Next.HORIZONTAL, 1),
                getWord(charPoint.coord, Next.HORIZONTAL, -1),
                getWord(charPoint.coord, Next.VERTICAL, 1),
                getWord(charPoint.coord, Next.VERTICAL, -1),
                getWord(charPoint.coord, Next.DIAGONAL_NE_SW, 1),
                getWord(charPoint.coord, Next.DIAGONAL_NE_SW, -1),
                getWord(charPoint.coord, Next.DIAGONAL_SE_NW, 1),
                getWord(charPoint.coord, Next.DIAGONAL_SE_NW, -1),
            ).count { it == "XMAS" }
        }.toLong()
    }

    override fun partTwo(): Long {
        return charMap.charPoints.count { charPoint ->
            getWord(
                charPoint.coord.to(Next.DIAGONAL_NE_SW, -1), Next.DIAGONAL_NE_SW, 1, 3
            ) in setOf("MAS", "SAM")
                    &&
                    getWord(
                        charPoint.coord.to(Next.DIAGONAL_SE_NW, -1), Next.DIAGONAL_SE_NW, 1, 3
                    ) in setOf("MAS", "SAM")
        }.toLong()
    }

}


fun main() {
    Day4().run()
}
