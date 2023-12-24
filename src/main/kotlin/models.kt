import kotlin.math.abs
import kotlin.math.absoluteValue

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
        (y.absoluteValue + x.absoluteValue - other.y.absoluteValue - other.x.absoluteValue).takeUnless { it == 0 }?.let { return it }
        return 1000 * (y - other.y) + x - other.x
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

class CharMap(
    val charPoints: List<CharPoint>,
    val nbCols: Int = charPoints.maxOf { it.coord.x } + 1,
    val nbLines: Int = charPoints.maxOf { it.coord.y } + 1,
) {
    val map = charPoints.associateBy { it.coord }

    operator fun get(coord: Coord, mapRepeatable: Boolean = false): CharPoint? {
        return map[coord.takeUnless { mapRepeatable } ?: coord.inInitialMap()]?.value?.let { CharPoint(coord, it) }
    }

    fun Coord.inInitialMap(): Coord {
        return Coord(x.inInitialMap(nbCols), y.inInitialMap(nbLines))
    }

    fun Int.inInitialMap(size: Int): Int {
        val res = this % size
        if(res < 0)
            return res + size
        return res
    }
    fun Int.initialMapCoord(size: Int): Int {
        val res = this / size
        if(this % size < 0)
            return res - 1
        return res
    }

    fun find(predicate: (CharPoint) -> Boolean): CharPoint? {
        return charPoints.find(predicate)
    }

    fun neighboursInMap(coord: Coord, mapRepeatable: Boolean = false): List<Pair<Direction, CharPoint>> {
        return listOfNotNull(
            get(coord.east(), mapRepeatable)?.let { Direction.E to it},
            get(coord.south(), mapRepeatable)?.let { Direction.S to it},
            get(coord.west(), mapRepeatable)?.let { Direction.W to it},
            get(coord.north(), mapRepeatable)?.let { Direction.N to it},
        )
    }

    fun mapCoord(it: Coord) : Coord {
        return Coord(it.x.initialMapCoord(nbCols), it.y.initialMapCoord(nbLines))
    }

    val end = nbLines - 1
    val cNW = Coord(0, 0)
    val cNE = Coord(0, end)
    val cSE = Coord(end, end)
    val cSW = Coord(end, 0)
    val corners = setOf(cNW, cNE, cSE, cSW)
    val midW = Coord(0, end / 2)
    val midN = Coord(end / 2, 0)
    val midE = Coord(end, end / 2)
    val midS = Coord(end / 2, end)
    val axis = setOf(midW, midN, midE, midS)
}

