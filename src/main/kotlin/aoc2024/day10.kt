package aoc2024

import AocRunner
import CharPoint
import InputType
import LineParser
import StringLineParser
import toCharMap

class Day10(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    10,
    StringLineParser,
    inputType
) {
    val map = lines.toCharMap()

    val trailheads = map.charPoints.filter { it.value == '0' }

    override fun partOne(): Long {
        return trailheads.sumOf { trailhead -> bfs(trailhead).first.count{it.value == '9'} }.toLong()
    }

    private fun bfs(trailhead: CharPoint): Pair<MutableSet<CharPoint>, MutableMap<CharPoint, Int>> {
        val visited = mutableSetOf<CharPoint>()
        val queue = ArrayDeque<CharPoint>()
        val passages = mutableMapOf<CharPoint, Int>().withDefault { 0 }
        queue.add(trailhead)
        passages[trailhead] = 1
        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            if (vertex !in visited) {
                visited.add(vertex)
                vertex.coord.mainNeighbours().mapNotNull { map[it] }
                    .filter { it.value.digitToInt() == vertex.value.digitToInt() + 1 }
                    .let { neighbors ->
                        neighbors.forEach {
                            passages.compute(it){ _, count -> (count ?: 0) + passages[vertex]!! }
                        }
                        queue.addAll(neighbors.filterNot { it in visited })
                    }
            }
        }
        return visited to passages
    }

    override fun partTwo(): Long {
        return trailheads.sumOf { trailhead -> bfs(trailhead).second.filter{it.key.value == '9'}.values.sum() }.toLong()
    }

}


fun main() {
    Day10().run()
}
