package aoc2024

import AocRunner
import CharMap
import Coord
import InputType
import LineParser
import Path
import binarySearchFirst
import dijkstra
import toNumberList

class Day18Parser : LineParser<Coord> {
    override fun parseLine(index: Int, line: String) = line.toNumberList(",").let {
        (x, y) -> Coord(x,y)
    }
}

class Day18(inputType: InputType = InputType.FINAL) : AocRunner<Coord, String>(
    18,
    Day18Parser(),
    inputType
) {

    val size = if(inputType == InputType.FINAL) 71 else 7
    val corrupted = if(inputType == InputType.FINAL) 1024 else 12

    val emptyMap = CharMap(size, size)

    override fun partOne(): String {
        val mapWithCorrupted = emptyMap.withChars(lines.take(corrupted).toSet(), '#')
        val dijkstra = dijkstraImpl(mapWithCorrupted, mapWithCorrupted.cNW)
        return dijkstra[mapWithCorrupted.cSE]!!.distance.toString()
    }

    override fun partTwo(): String {
       return lines.indices.toList().binarySearchFirst {
            dijkstraImpl(
                emptyMap.withChars(lines.take(it+1).toSet(), '#'),
                emptyMap.cNW
            )[emptyMap.cSE] == null
        }.let { lines[it] }.let { (x, y) -> "$x,$y" }
    }

    fun dijkstraImpl(map: CharMap, initial: Coord): Map<Coord, Path<Coord>> {
        return dijkstra(
            map = map,
            initial = initial,
            neighbourFilter = { it.charPoint.value == '.' },
            distanceCalculator = { 1 },
            nodeBuilder = { it.charPoint.coord },
        )
    }

}


fun main() {
    Day18().run()
}
