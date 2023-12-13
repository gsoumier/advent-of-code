class Day13(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    13,
    StringLineParser,
    inputType
) {
    private val blocks = lines.splitWhen { it.isEmpty() }

    override fun partOne(): Long {
        return blocks.sumOf { it.getBlockScore() }.toLong()
    }

    override fun partTwo(): Long {
        return blocks.sumOf { block ->
            val originalSymAxis = block.symAxis()
            block.toCharMap().map.keys
                .map { coord ->
                    block.change(coord) { if (it == '.') '#' else '.' }.getBlockScore(originalSymAxis)
                }
                .first { it > 0 }
        }.toLong()
    }

    private fun Pair<List<Int>, List<Int>>.remove(exept: Pair<List<Int>, List<Int>>): Pair<List<Int>, List<Int>> {
        return first.remove(exept.first) to second.remove(exept.second)
    }

    private fun List<Int>.remove(exept: List<Int>): List<Int> {
        return filter { it !in (exept) }
    }

    private fun Pair<List<Int>, List<Int>>.getBlockSymScore(): Int {
        return first.sum() * 100 + second.sum()
    }

    private fun List<String>.symAxis(): Pair<List<Int>, List<Int>> {
        val horizontalAxis = findSymmetryAxis()
        val verticalAxis = getStringCols().findSymmetryAxis()
        return Pair(horizontalAxis, verticalAxis)
    }

    private fun List<String>.findSymmetryAxis(): List<Int> {
        return (1..<size).filter { isSymmetryAxis(it) }.also { if (it.size > 1) println("two symetrical") }
    }

    private fun List<String>.isSymmetryAxis(i: Int): Boolean {
        return (0..size / 2).map { getOrNull(i - 1 - it) to getOrNull(i + it) }
            .all { it.first == null || it.second == null || it.first == it.second }
    }


    private fun List<String>.getBlockScore(
        symAxisToExclude: Pair<List<Int>, List<Int>> = emptyList<Int>() to emptyList()
    ) = symAxis().remove(symAxisToExclude).getBlockSymScore()

}

private fun List<String>.change(coord: Coord, replace: (Char) -> Char): List<String> {
    return mapIndexed { index, s -> if (coord.y == index) s.change(coord.x, replace) else s }
}

private fun String.change(x: Int, replace: (Char) -> Char): String {
    return mapIndexed { index, c -> if (x == index) replace(c) else c }.joinToString("")
}

private fun Char.change(): Char {
    return if (this == '.') '#' else '.'
}


fun main() {
    Day13().run()
}
