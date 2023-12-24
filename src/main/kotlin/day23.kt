import kotlin.math.max

class Day23(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    23,
    StringLineParser,
    inputType
) {
    val charMap = lines.toCharMap()
    val startPoint = charMap[Coord(1, 0)]!!
    val end = Coord(nbCols - 2, nbLines - 1)
    val endPoint = charMap[end]!!

    val intersections = charMap.charPoints.filter { charMap.neighboursInMap(it.coord).filter { it.second.value != '#' }.size > 2 } + startPoint + endPoint

    override fun partOne(): Long {
        return getLongestPathsForIntersection(
            startPoint,
            endPoint,
            emptySet(),
            intersections.associateWith { findConnections(it, true) }).inc().toLong()
    }

    private fun getNextIntersection(
        start: CharPoint,
        direction: Direction,
        visitedInter: Set<Coord>,
        slopeSlippery: Boolean,
        checkIntersection: Boolean = true,
    ): Triple<CharPoint, Int, Direction>? {
        var current = start
        var dir = direction
        var straightPathLength = 1
        var neighbours = possibleNeighbours(current, visitedInter, dir, slopeSlippery, checkIntersection)
        var singleNeighbour = neighbours.singleOrNull()
        while (singleNeighbour != null) {
            singleNeighbour.takeIf { it.second.coord == end }?.let { return Triple(charMap[end]!!,straightPathLength, it.first) }
            current = singleNeighbour.second
            dir = singleNeighbour.first
            straightPathLength++
            neighbours = possibleNeighbours(current, visitedInter, dir, slopeSlippery)
            singleNeighbour = neighbours.singleOrNull()
        }
        return Triple(current, straightPathLength, dir).takeIf { neighbours.isNotEmpty() }
    }

    private fun possibleNeighbours(
        start: CharPoint,
        visited: Set<Coord>,
        direction: Direction,
        slopeSlippery: Boolean,
        checkIntersection: Boolean = true,
    ) = charMap.neighboursInMap(start.coord)
        .filter { it.second.value != '#' }
        .filter { it.first != direction.opposite() }
        .filter { !checkIntersection || it.second.coord !in visited }
        .filter { !slopeSlippery || it.second.slopeDirection() != it.first.opposite() }
        .filter { !slopeSlippery || start.slopeDirection()?.let { slopeDir -> slopeDir == it.first } ?: true }

    override fun partTwo(): Long {
        return getLongestPathsForIntersection(
            startPoint,
            endPoint,
            emptySet(),
            intersections.associateWith { findConnections(it, false) }).inc().toLong()
    }

    private fun findConnections(point: CharPoint, slopeSlippery: Boolean): List<Pair<CharPoint, Int>> {
        return charMap.neighboursInMap(point.coord)
            .filter { it.second.value != '#' }
            .mapNotNull { getNextIntersection(it.second, it.first, emptySet(), slopeSlippery, false) }
            .map { it.first to it.second }
    }

    private fun getLongestPathsForIntersection(
        start: CharPoint,
        end: CharPoint,
        visitedInter: Set<Coord> = emptySet(),
        connections: Map<CharPoint, List<Pair<CharPoint, Int>>>,
    ): Int {
        if(start == end){
            return 0
        }

        var maxDistance = Int.MIN_VALUE
        connections[start]?.filter {
            it.first.coord !in visitedInter
        }?.forEach {
            (neighbour, dist) -> maxDistance = max(maxDistance, dist + getLongestPathsForIntersection(neighbour, end, visitedInter + start.coord, connections))
        }

        return maxDistance
    }

}

private fun CharPoint.slopeDirection(): Direction? {
    return when (value) {
        '<' -> Direction.W
        '>' -> Direction.E
        '^' -> Direction.N
        'v' -> Direction.S
        else -> null
    }
}


fun main() {
    Day23().run()
}
