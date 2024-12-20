import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.util.PriorityQueue

fun aocInputStream(aocDay: Int, type: InputType = InputType.FINAL): InputStream =
    object {}.javaClass.getResourceAsStream("day$aocDay.$type.txt")!!

fun String.toNumberList(s: String = " ") =
    split(s).map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }


fun <T> List<T>.allCombinations(size: Int): List<List<T>> {
    if (size == 1)
        return map { listOf(it) }
    return allCombinations(size - 1)
        .flatMap { map { op -> listOf(op) + it } }
}

fun <T> List<T>.allPairs() = flatMapIndexed { index: Int, a: T -> drop(index + 1).map { a to it } }

enum class InputType {
    FINAL, SAMPLE, SAMPLE2
}

fun List<String>.getCols(): List<List<Char>> = (0..<first().length).map { colIndex -> map { it[colIndex] } }

fun List<String>.getStringCols(): List<String> = getCols().map { it.joinToString("") }

fun List<String>.splitWhen(predicate: (String) -> Boolean): List<List<String>> {
    val result = mutableListOf<List<String>>()
    var sublist = mutableListOf<String>()
    for (line in this) {
        if (predicate(line)) {
            result.add(sublist.toList())
            sublist = mutableListOf()
        } else {
            sublist.add(line)
        }
    }
    result.add(sublist.toList())
    return result.toList()
}

fun <A, B> List<A>.pmap(f: suspend (A) -> B): List<B> = runBlocking {
    map { async(Dispatchers.Default) { f(it) } }.map { it.await() }
}

fun List<String>.toCharMap() = CharMap(flatMapIndexed { lineIndex: Int, s: String ->
    s.mapIndexed { charIndex, c ->
        CharPoint(
            Coord(
                charIndex,
                lineIndex
            ), c
        )
    }
})

fun Int.quotientAndReminder(divisor: Int): Pair<Int, Int> {
    return this / divisor to this % divisor
}

fun Int.pow2(): Long {
    return toLong().let { it * it }
}

fun String.parseZCoord(s: String = ",") =
    split(s).map { it.trim().toInt() }.let { (x, y, z) -> ZCoord(Coord(x, y), z) }

fun String.parseZCoordLong(s: String = ", ") =
    split(s).map { it.trim().toLong() }.let { (x, y, z) -> ZCoordLong(x, y, z) }

fun <T> List<T>.binarySearchFirst(predicate: (T) -> Boolean): Int {
    var low = 0
    var high = this.size
    while (low < high) {
        val mid = (low + high) / 2
        if (predicate(this[mid])) {
            high = mid
        } else {
            low = mid + 1
        }
    }
    return if (low < this.size && predicate(this[low])) low else -1
}

data class Path<Node>(val distance: Long, val possiblePaths: List<List<Node>>)

fun <Node : Coordinates> dijkstra(
    map: CharMap,
    initial: Node,
    neighbourFilter: (Neighbour) -> Boolean = { it.charPoint.value in setOf('.', 'E') },
    distanceCalculator: (Pair<Node, Node>) -> Long = { 1 },
    maxDistance: Long? = null,
    nodeBuilder: (Neighbour) -> Node,
): Map<Node, Path<Node>> {
    val distances = mutableMapOf<Node, Long>()
        .withDefault { Long.MAX_VALUE }
        .apply { put(initial, 0L) }
    val priorityQueue = PriorityQueue<Pair<Node, Long>>(compareBy { it.second })
        .apply { add(initial to 0L) }
    val previous = mutableMapOf<Node, MutableList<List<Node>>>()
        .apply { put(initial, mutableListOf(listOf(initial))) }

    while (priorityQueue.isNotEmpty()) {
        val (node, currentDist) = priorityQueue.poll()

        val neighbours = map.neighboursInMap(node).filter(neighbourFilter)
        neighbours.forEach { it ->
            val newNode = nodeBuilder(it)
            val totalDist = currentDist + distanceCalculator(node to newNode)
            if (totalDist <= (maxDistance ?: Long.MAX_VALUE)) {
                val prevDist = distances.getValue(newNode)
                if (totalDist < prevDist) {
                    distances[newNode] = totalDist
                    priorityQueue.add(newNode to totalDist)
                    previous[newNode] = previous[node]!!.map { it + newNode }.toMutableList()
                }
                if (totalDist == prevDist) {
                    previous[newNode]!!.addAll(previous[node]!!.map { it + newNode })
                }
            }
        }
    }
    return distances.mapValues { (node, dist) -> Path(dist, previous[node]!!) }
}

