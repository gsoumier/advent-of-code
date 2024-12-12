package aoc2024

import AocRunner
import CharMap
import CharPoint
import Coord
import Direction
import InputType
import StringLineParser
import toCharMap

data class Region(val plantType: Char, val plots: MutableList<Coord> = mutableListOf()) {

    fun area(): Long {
        return plots.size.toLong()
    }

    fun perimeter(map: CharMap): Long {
        return plots.sumOf { plot ->
            Direction.cardinals()
                .count { plot.hasBorder(it, map) }
        }
            .toLong()
    }

    private fun Coord.hasBorder(direction: Direction, map: CharMap) =
        map[to(direction)]?.takeIf { neighbour -> neighbour.value == plantType } == null

    val minX get() = plots.minOf { it.x }
    val maxX get() = plots.maxOf { it.x }
    fun minX(y: Int) = plots.filter { it.y == y }.minOf { it.x }
    fun maxX(y: Int) = plots.filter { it.y == y }.maxOf { it.x }
    val minY get() = plots.minOf { it.y }
    val maxY get() = plots.maxOf { it.y }
    fun minY(x: Int) = plots.filter { it.x == x }.minOf { it.y }
    fun maxY(x: Int) = plots.filter { it.x == x }.maxOf { it.y }

    fun sides(map: CharMap): Long {
        var countX = 0L
        var countY = 0L

        (minY..maxY).forEach { y ->
            var northBorder = false
            var southBorder = false
            (minX(y)..maxX(y)).forEach { x ->
                val coord = Coord(x, y)
                if (!plots.contains(coord)) {
                    northBorder = false
                    southBorder = false
                } else {
                    val newNorthBorder = coord.hasBorder(Direction.N, map)
                    if (newNorthBorder && !northBorder) countY++;
                    northBorder = newNorthBorder
                    val newSouthBorder = coord.hasBorder(Direction.S, map)
                    if (newSouthBorder && !southBorder) countY++;
                    southBorder = newSouthBorder
                }
            }
        }
        (minX..maxX).forEach { x ->
            var westBorder = false
            var eastBorder = false
            (minY(x)..maxY(x)).forEach { y ->
                val coord = Coord(x, y)
                if (!plots.contains(coord)) {
                    eastBorder = false
                    westBorder = false
                } else {
                    val newWestBorder = coord.hasBorder(Direction.W, map)
                    if (newWestBorder && !westBorder) countX++;
                    westBorder = newWestBorder
                    val newEastBorder = coord.hasBorder(Direction.E, map)
                    if (newEastBorder && !eastBorder) countX++;
                    eastBorder = newEastBorder
                }
            }
        }

        return countX + countY

    }


}

class Day12(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    12,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()

    val regions = findRegions()

    private fun findRegions(): List<Region> {
        val classed = mutableSetOf<Coord>()
        return map.charPoints.mapNotNull { plot ->
            takeIf { plot.coord !in classed }?.let {
                findRegion(plot, classed)
            }
        }
    }

    private fun findRegion(point: CharPoint, visited: MutableSet<Coord>): Region {
        val region = Region(point.value)
        val queue = ArrayDeque<CharPoint>()
        queue.add(point)
        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            if (vertex.coord !in visited) {
                visited.add(vertex.coord)
                region.plots.add(vertex.coord)
                vertex.coord.mainNeighbours().mapNotNull { map[it] }
                    .filter { it.value == vertex.value }
                    .let { neighbors ->
                        queue.addAll(neighbors.filterNot { it.coord in visited })
                    }
            }
        }
        return region
    }

    override fun partOne(): Long {
        return regions.sumOf { region -> region.area() * region.perimeter(map) }
    }

    override fun partTwo(): Long {
        return regions.sumOf { region -> region.area() * region.sides(map) }
    }

}


fun main() {
    Day12().run()
}
