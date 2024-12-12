package aoc2024

import AocRunner
import CharMap
import CharPoint
import Coord
import Coords
import Direction
import InputType
import StringLineParser
import toCharMap

data class Region(val plantType: Char, val plots: Coords) {

    fun area(): Long {
        return plots.size.toLong()
    }

    fun perimeter(map: CharMap): Long {
        return plots.sumOf { plot ->
            Direction.cardinals()
                .count { plot.hasBorder(it, map) }
        }.toLong()
    }

    fun sides(map: CharMap): Long {
        var count = 0L
        with(plots) {
            (minY()..maxY()).forEach { y ->
                val dir = listOf(Direction.N, Direction.S)
                val borders = dir.associateWith { false }.toMutableMap()
                (minX(y)..maxX(y)).forEach { x ->
                    count += countNewBorders(Coord(x, y), dir, borders, map)
                }
            }
            (minX()..maxX()).forEach { x ->
                val dir = listOf(Direction.W, Direction.E)
                val borders = dir.associateWith { false }.toMutableMap()
                (minY(x)..maxY(x)).forEach { y ->
                    count += countNewBorders(Coord(x, y), dir, borders, map)
                }
            }
        }
        return count
    }

    private fun Coord.hasBorder(direction: Direction, map: CharMap) =
        map[to(direction)]?.takeIf { neighbour -> neighbour.value == plantType } == null

    private fun countNewBorders(
        coord: Coord,
        dir: List<Direction>,
        borders: MutableMap<Direction, Boolean>,
        map: CharMap,
    ): Long {
        var count = 0L
        if (!plots.contains(coord)) {
            dir.forEach { borders[it] = false }
        } else {
            dir.forEach {
                val newBorder = coord.hasBorder(it, map)
                if (newBorder && borders[it] == false) count++
                borders[it] = newBorder
            }
        }
        return count
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
        val plots = mutableListOf<Coord>()
        val queue = ArrayDeque<CharPoint>()
        queue.add(point)
        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            if (vertex.coord !in visited) {
                visited.add(vertex.coord)
                plots.add(vertex.coord)
                vertex.coord.mainNeighbours().mapNotNull { map[it] }
                    .filter { it.value == vertex.value }
                    .let { neighbors ->
                        queue.addAll(neighbors.filterNot { it.coord in visited })
                    }
            }
        }
        return Region(point.value, Coords(plots))
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
