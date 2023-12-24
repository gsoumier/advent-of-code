data class Node(val coord: Coord, val dir: Direction, val stepsInDir: Int)

fun findShortestPath(map: CharMap, source: Coord, target: Coord, part2: Boolean = false): ShortestPathResult {
    val dist = mutableMapOf<Node, Int>()
    val prev = mutableMapOf<Node, Node?>()
    val q = (mutableSetOf<Node>())

    val startNode = Node(source, Direction.E, 0)
    q.add(startNode)
    dist[startNode] = 0

    while (q.isNotEmpty()) {
        val u = q.minByOrNull { dist[it] ?: 0 } ?: break
        q.remove(u)

        if (u.coord == target) {
            break
        }

        map.neighboursInMap(u.coord)
            .filter { it.first != u.dir.opposite() }
            .filter { !part2 || it.second.coord != target || it.first == u.dir && u.stepsInDir >= 4  }
            .filter { !part2 || it.first == u.dir || u.stepsInDir >= 4  }
            .map {
                Node(
                    it.second.coord,
                    it.first,
                    if (it.first == u.dir) u.stepsInDir + 1 else 1
                ) to it.second.intValue
            }
            .filter { it.first.stepsInDir < (11.takeIf { part2 } ?: 4) }
            .forEach { (node, cost) ->
                val alt = dist[u]!! + cost
                val prevDist = dist[node]
                if (prevDist == null) {
                    q.add(node)
                }
                if (prevDist == null || alt < prevDist) {
                    dist[node] = alt
                    prev[node] = u
                }
            }
    }
    return ShortestPathResult(prev, dist, source, target)
}

class ShortestPathResult(val prev: Map<Node, Node?>, val dist: Map<Node, Int>, val source: Coord, val target: Coord) {

    fun shortestPath(from: Coord = source, to: Coord = target, list: List<Coord> = emptyList()): List<Coord> {
        val shortestTargetNode = dist.filter { it.key.coord == to }.minBy { it.value }.key
        return shortestPath(from, shortestTargetNode, list)
    }

    private fun shortestPath(from: Coord, to: Node, list: List<Coord> = emptyList()): List<Coord> {
        val last = prev[to] ?: return if (from == to.coord) {
            list + to.coord
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to.coord
    }

    fun shortestDistance(): Int? {
        return dist.filter { it.key.coord == target }.minOfOrNull { it.value }
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
        if (isDebug)
            res.shortestPath().forEach { println(it) }
        return res.shortestDistance()!!.toLong()
    }

    override fun partTwo(): Long {
        val res = findShortestPath(map, origin, dest, true)
        if (isDebug)
            res.shortestPath().forEach { println(it) }
        return res.shortestDistance()!!.toLong()
    }

}


fun main() {
    Day17().run()
}
