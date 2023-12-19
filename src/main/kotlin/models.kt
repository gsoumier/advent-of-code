import kotlin.math.abs

data class Coord(
    val x: Int,
    val y: Int,
) : Comparable<Coord> {

    fun to(direction: Direction, length: Int = 1) = when (direction) {
        Direction.N -> north(length)
        Direction.E -> east(length)
        Direction.S -> south(length)
        Direction.W -> west(length)
    }

    fun west(length: Int = 1) = Coord(x - length, y)

    fun south(length: Int = 1) = Coord(x, y + length)

    fun east(length: Int = 1) = Coord(x + length, y)

    fun north(length: Int = 1) = Coord(x, y - length)

    fun distanceTo(other: Coord): Int {
        return abs(other.y - y) + abs(other.x - x)
    }

    override fun compareTo(other: Coord): Int {
        return 10000 * (y - other.y) + x - other.x
    }

    fun neighbours() : List<Coord> {
       return listOf(east(), south(), west(), north())
    }
}

enum class Direction {
    N, E, S, W;

    fun opposite(): Direction {
        return when (this) {
            N -> S
            E -> W
            S -> N
            W -> E
        }
    }


}

fun horizontals() = listOf(Direction.E, Direction.W)

fun verticals() = listOf(Direction.N, Direction.S)

data class CharPoint(val coord: Coord, var value: Char){
    val intValue: Int
        get() {
            return value.digitToInt()
        }
}

class CharMap(val charPoints: List<CharPoint>) {
    val map = charPoints.associateBy { it.coord }
    operator fun get(coord: Coord) = map[coord]
    fun find(predicate: (CharPoint) -> Boolean): CharPoint? {
        return charPoints.find(predicate)
    }
}

