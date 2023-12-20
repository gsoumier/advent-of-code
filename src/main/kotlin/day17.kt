data class Edge(val coord1:Coord, val coord2: Coord, val dist: Dist)

data class Dist(val heatLoss: Int, val direction: Direction? = null, val stepsInDir: Int? = 1) : Comparable<Dist> {
    override fun compareTo(other: Dist): Int {
        return heatLoss - other.heatLoss
    }

    operator fun plus(other: Dist) : Dist {
        return Dist(
            heatLoss + other.heatLoss,
            other.direction,
            if(direction == other.direction) (stepsInDir?:0) + (other.stepsInDir?:0) else other.stepsInDir
        ).takeIf { (it.stepsInDir?:0) < 4 && direction != other.direction?.opposite() } ?: Dist(Int.MAX_VALUE)
    }

}

fun findShortestPath(map: CharMap, source: Coord, target: Coord): ShortestPathResult {

    val edges = map.charPoints.flatMap { point -> map.neighboursInMap(point).map { Edge(point.coord, it.second.coord, Dist(it.second.intValue, it.first)) } }

    val dist = mutableMapOf<Coord, MutableMap<Direction,Dist>>()
    val prev = mutableMapOf<Coord, MutableMap<Direction,Coord?>>()
    val q = map.map.keys.toSortedSet()

    q.forEach { v ->
        dist[v] = Direction.entries.associateWith { Dist(Int.MAX_VALUE) }.toMutableMap()
        prev[v] = Direction.entries.associateWith {null}.toMutableMap()
    }
    dist[source] = Direction.entries.associateWith { Dist(0) }.toMutableMap()

    while (q.isNotEmpty()) {
        val u = q.minByOrNull { dist[it]?.minOf { it.value } ?: Dist(0) }
        q.remove(u)

        if (u == target) {
            break
        }

        edges
            .filter { it.coord1 == u }
            .forEach { edge ->
                val v = edge.coord2

                Direction.entries.forEach { dir->
                    val alt = dist[u]!!.minOf { it.value + edge.dist }
                        if (alt < dist[v]!![dir]!!) {
                        dist[v]!![dir] = alt
                        prev[v]!![dir] = u
                    }
                }
            }
    }

    return ShortestPathResult(prev, dist, source, target)
}

class ShortestPathResult(val prev: Map<Coord, Map<Direction,Coord?>>, val dist: Map<Coord, Map<Direction,Dist>>, val source: Coord, val target: Coord) {

//    fun shortestPath(from: Coord = source, to: Coord = target, list: List<Coord> = emptyList()): List<Coord> {
//        val last = prev[to] ?: return if (from == to) {
//            list + to
//        } else {
//            emptyList()
//        }
//        return shortestPath(from, last, list) + to
//    }

    fun shortestDistance(): Int? {
        val shortest = dist[target]?.minOf { it.value }
        if (shortest?.heatLoss == Integer.MAX_VALUE) {
            return null
        }
        return shortest?.heatLoss
    }
}

class Day17(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    17,
    StringLineParser,
    inputType
) {
    var map = lines.toCharMap()

    var origin = Coord(0, 0)
    var dest = Coord(nbCols - 1, nbLines - 1)




    override fun partOne(): Long {
        val res = findShortestPath(map, origin, dest)
//        res.shortestPath().forEach { println(it) }
        return res.shortestDistance()!!.toLong()
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }

}


fun main() {
    Day17().run()
}
