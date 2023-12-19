import kotlin.math.min

data class CurrentWay(val nbLines: Int, val nbCols: Int, val path: List<CharPoint>) {
    fun current() = path.last()
    fun nextAllowed(): List<Coord> {
        val current = current().coord
        val pairs = path.map { it.coord }.zipWithNext()
        return current.neighbours()
            .filter { it.x in 0..<nbCols && it.y in 0..<nbLines }
            .filter { path.asReversed().getOrNull(1)?.coord != it }
            .filter {
                path.size < 4 ||
                        (listOf(it) + path.asReversed().subList(0, 4).map { it.coord }).isNotSameDirection(it)
            }
            .filter { !pairs.contains(current to it) }
    }

    fun add(charPoint: CharPoint): CurrentWay {
        return copy(path = path + charPoint)
    }

    fun heat(): Int {
        return path.sumOf { it.intValue }
    }

    fun cacheKey(): String {
        return path.asReversed().subList(0, min(1, path.size)).map { it.coord }.reversed().toString()
    }
}

fun List<Coord>.isNotSameDirection(coord: Coord): Boolean {
    return !(all { it.x == coord.x } || all { it.y == coord.y })
}

class Day17(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    17,
    StringLineParser,
    inputType
) {
    var map = lines.toCharMap().map

    val cache = mutableMapOf<String, List<CharPoint>>()
    val startCache = mutableMapOf<Coord, Int>()

    var origin = map[Coord(0, 0)]!!
    var dest = map[Coord(nbCols - 1, nbLines - 1)]!!
    val currentMinFound =
        (1..<nbCols).flatMap { listOf(map[Coord(it, it - 1)]!!, map[Coord(it, it)]!!) }.fold(listOf(origin)){ acc, charPoint -> (acc + charPoint).also {
            startCache[charPoint.coord] = it.sumOf { it.intValue }
        } }.sumOf { it.intValue }

    fun calculateMinHeatPathToDest(current: Coord, currentWay: CurrentWay, maxHeat: Int? = null): List<CharPoint>? {
        if (current == dest.coord)
            return listOf(dest)

        val heat = currentWay.heat()
        startCache.compute(current) {
            _, minHeat -> min(minHeat ?: heat, heat)
        }
        startCache[current]?.let { if(heat > it) return null }
        maxHeat?.let {
            if (heat > maxHeat) return null
        }
        if (heat > currentMinFound) {
            return null
        }


        cache[currentWay.cacheKey()]?.also {
//            println("List : $it retrieve from cache")
        }?.let { return it }

        val nextAllowed = currentWay.nextAllowed().takeUnless { it.isEmpty() } ?: return null
        if (nextAllowed.contains(dest.coord)) {
            return listOf(map[current]!!, dest).also { cache[currentWay.cacheKey()] = it }
        }

        val firstPath = nextAllowed.firstNotNullOfOrNull {
            calculateMinHeatPathToDest(it, currentWay.add(map[it]!!))
        } ?: return null
        val newMaxHeat = min(
            firstPath.sumOf { it.intValue } + heat, maxHeat ?: Int.MAX_VALUE)

        return (nextAllowed
            .mapNotNull { next ->
                calculateMinHeatPathToDest(
                    next,
                    currentWay.add(map[next]!!),
                    newMaxHeat
                )?.let { listOf(map[current]!!) + it }
            }
            .minByOrNull { it.sumOf { it.intValue } }
            ?: (listOf(map[current]!!) + firstPath))
            .also { cache[currentWay.cacheKey()] = it }
    }

    override fun partOne(): Long {
        val res = calculateMinHeatPathToDest(origin.coord, CurrentWay(nbLines, nbCols, listOf(origin)))!!
        return res.drop(1).sumOf { it.intValue }.toLong()
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }

}


fun main() {
    Day17().run()
}
