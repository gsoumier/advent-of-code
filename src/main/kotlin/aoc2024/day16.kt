package aoc2024

import AocRunner
import Coord
import Direction
import InputType
import StringLineParser
import toCharMap
import java.util.PriorityQueue

typealias Horizontal = Boolean

class Day16(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    16,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()

    val start = map.find { it.value == 'S' }!!.coord
    val end = map.find { it.value == 'E' }!!.coord

    override fun partOne(): Long {
        return dijkstra().first.let {
            listOfNotNull(
                it[end to true],
                it[end to false],
            ).min()
        }
    }

    override fun partTwo(): Long {
        val dijkstra = dijkstra()
        val node = dijkstra.first.let { dist ->
            listOf(
                end to true,
                end to false,
            ).minBy { dist[it] ?: Long.MAX_VALUE }
        }
        return dijkstra.second[node]!!.flatten().distinct().count().toLong()
    }

    fun dijkstra(): Pair<MutableMap<Pair<Coord, Horizontal>, Long>, MutableMap<Pair<Coord, Horizontal>, MutableList<List<Coord>>>> {

        val initial = start to true

        val distances = mutableMapOf<Pair<Coord, Horizontal>, Long>()
            .withDefault { Long.MAX_VALUE }
            .apply { put(initial, 0L) }
        val priorityQueue = PriorityQueue<Pair<Pair<Coord, Horizontal>, Long>>(compareBy { it.second })
            .apply { add(initial to 0L) }
        val previous = mutableMapOf<Pair<Coord, Horizontal>, MutableList<List<Coord>>>()
            .apply { put(initial, mutableListOf(listOf(start))) }

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDist) = priorityQueue.poll()
            val (coord, prevDir) = node

            val neighbours = map.neighboursInMap(coord).filter { it.second.value in setOf('.','E') }
            neighbours.forEach { (dir, charPoint) ->
                val neighbourCoord = charPoint.coord
                val horizontal = dir in setOf(Direction.W, Direction.E)
                val totalDist = currentDist + if (prevDir == horizontal) 1 else 1001
                val adjacent = neighbourCoord to horizontal
                val prevDist = distances.getValue(adjacent)
                if (totalDist < prevDist) {
                    distances[adjacent] = totalDist
                    priorityQueue.add(adjacent to totalDist)
                    previous[adjacent] = previous[node]!!.map { it + neighbourCoord }.toMutableList()
                }
                if(totalDist == prevDist){
                    previous[adjacent]!!.addAll(previous[node]!!.map { it + neighbourCoord })
                }
            }
        }
        return distances to previous
    }
}


fun main() {
    Day16().run()
}
