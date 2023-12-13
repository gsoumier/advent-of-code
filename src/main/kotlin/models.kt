import kotlin.math.abs

data class Coord(
    val x: Int,
    val y: Int,
) {
    fun to(direction: Direction) = when (direction) {
        Direction.N -> Coord(x, y - 1)
        Direction.E -> Coord(x + 1, y)
        Direction.S -> Coord(x, y + 1)
        Direction.W -> Coord(x - 1, y)
    }

    fun distanceTo(other: Coord) : Int {
        return abs(other.y - y ) + abs(other.x - x)
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

data class CharPoint(val coord: Coord, var value: Char)

class CharMap(val charPoints: List<CharPoint>){
    val map = charPoints.associateBy { it.coord }
    operator fun get(coord: Coord) = map[coord]
    fun find(predicate: (CharPoint) -> Boolean): CharPoint? {
        return charPoints.find(predicate)
    }
}

