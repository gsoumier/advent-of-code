import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.InputStream

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
    FINAL, SAMPLE
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
