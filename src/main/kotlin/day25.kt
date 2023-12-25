data class Day25Line(
    val component: String,
    val connectedComponents: List<String>,
)

class Day25Parser : LineParser<Day25Line> {
    override fun parseLine(index: Int, line: String): Day25Line {
        return line.split(": ").let { (comp, connected) ->
            Day25Line(
                comp, connected.split(" ")
            )
        }
    }
}

private fun Set<Pair<String, String>>.containsIgnoringOrder(e: Pair<String, String>): Boolean {
    return contains(e) || contains(e.second to e.first)
}

class Day25(inputType: InputType = InputType.FINAL) : AocRunner<Day25Line, Long>(
    25,
    Day25Parser(),
    inputType
) {

    private val connections: List<Pair<String, String>> =
        lines.flatMap { it.connectedComponents.map { c -> it.component to c } }

    private val components = connections.flatMap { listOf(it.first, it.second) }.toSet()

    private val componentsDirectConnections: Map<String, Set<String>> = components.associateWith { comp ->
        connections.mapNotNull { conn ->
            conn.second.takeIf { conn.first == comp } ?: conn.first.takeIf { conn.second == comp }
        }.toSet()
    }

    override fun partOne(): Long {
        connections.forEach { c1 ->
            findShortestLinkBetween(c1.first, c1.second, setOf(c1))!!.zipWithNext().forEach { c2 ->
                findShortestLinkBetween(c1.first, c1.second, setOf(c1, c2))!!.zipWithNext().forEach { c3 ->
                    getSeparateGroups(setOf(c1, c2, c3))?.let {
                        return (it.first * it.second).toLong()
                    }
                }
            }
        }
        return 0L
    }

    private fun findShortestLinkBetween(
        source: String,
        target: String,
        brokenConn: Set<Pair<String, String>>
    ): List<String>? {
        val dist = mutableMapOf<String, Int>()
        val prev = mutableMapOf<String, String?>()
        val q = (mutableSetOf<String>())

        q.add(source)
        dist[source] = 0

        while (q.isNotEmpty()) {
            val u = q.minByOrNull { dist[it] ?: 0 } ?: break
            q.remove(u)

            if (u == target) {
                break
            }

            componentsDirectConnections[u]!!
                .filter { !brokenConn.containsIgnoringOrder(u to it) }
                .forEach { node ->
                    val alt = dist[u]!! + 1
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
        return ShortestPathResult(prev, dist, source, target).shortestPath()
    }

    class ShortestPathResult(
        val prev: Map<String, String?>,
        val dist: Map<String, Int>,
        val source: String,
        val target: String
    ) {

        fun shortestPath(from: String = source, to: String = target, list: List<String> = emptyList()): List<String>? {
            val last = prev[to] ?: return if (from == to) {
                list + to
            } else {
                null
            }
            return shortestPath(from, last, list)?.let { it + to }
        }

        fun shortestDistance(): Int? {
            return dist[target]
        }
    }


    private fun getSeparateGroups(brokenConn: Set<Pair<String, String>>): Pair<Int, Int>? {
        val first = brokenConn.first()
        val group1 = getGroupSize(brokenConn, setOf(first.first), emptySet()) ?: return null
        return group1 to components.size - group1
    }

    private fun getGroupSize(
        brokenConn: Set<Pair<String, String>>,
        newCompInGroup: Set<String>,
        compAlreadyInGroup: Set<String>
    ): Int? {
        val newGroup = compAlreadyInGroup + newCompInGroup
        if (newGroup.size == components.size) {
            return null
        }
        if (brokenConn.any { it.first in newGroup && it.second in newGroup }) {
            return null
        }

        val newNewComp = newCompInGroup.flatMap { newComp ->
            componentsDirectConnections[newComp]!!.filter {
                !brokenConn.containsIgnoringOrder(newComp to it)
            }
        }.filter { it !in newGroup }.toSet()

        if (newNewComp.isEmpty())
            return newGroup.size

        return getGroupSize(
            brokenConn,
            newNewComp,
            newGroup,
        )
    }

    override fun partTwo(): Long {
        return 2023L
    }
}

fun main() {
    Day25().run()
}
