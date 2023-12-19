
data class Day16Line(
    val line: String,
) {

}

class Day16Parser : LineParser<Day16Line> {
    override fun parseLine(index: Int, line: String): Day16Line {
        return Day16Line(
            line,
        )
    }
}

class Day16(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    16,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap().map

    fun goToNext(point: CharPoint, direction: Direction, alreadyVisited : MutableList<Pair<CharPoint, Direction>>){
        val next = map[point.coord.to(direction)] ?: return
        if(alreadyVisited.contains(next to direction)) return
        alreadyVisited.add(next to direction)
        next.value.newDirections(direction).forEach { goToNext(next, it, alreadyVisited) }
    }

    override fun partOne(): Long {
        return countVisited(Coord(-1, 0), Direction.E)
    }

    private fun countVisited(coord: Coord, direction: Direction): Long {
        val alreadyVisited = mutableListOf<Pair<CharPoint, Direction>>()
        goToNext(
            CharPoint(coord, '.'),
            direction,
            alreadyVisited
        )
        return alreadyVisited.map { it.first }.toSet().size.toLong()
    }

    override fun partTwo(): Long {
        return (lines.indices.flatMap {
            listOf(Coord(-1, it) to Direction.E, Coord(lines.first().length, it) to Direction.W)
        } + lines.first().indices.flatMap {
            listOf(Coord(it, -1) to Direction.S, Coord(it, lines.size) to Direction.W)
        }).maxOf { countVisited(it.first, it.second) }

    }

}

private fun Char.newDirections(direction: Direction): List<Direction> {
    return when(this){
        '|' -> if(direction in verticals()) return listOf(direction) else verticals()
        '-' -> if(direction in horizontals()) return listOf(direction) else horizontals()
        '/' -> when(direction) {
            Direction.N -> Direction.E
            Direction.E -> Direction.N
            Direction.S -> Direction.W
            Direction.W -> Direction.S
        }.let { listOf(it) }
        '\\' -> when(direction) {
            Direction.N -> Direction.W
            Direction.W -> Direction.N
            Direction.S -> Direction.E
            Direction.E -> Direction.S
        }.let { listOf(it) }
        else -> listOf(direction)
     }
}


fun main() {
    Day16().run()
}
