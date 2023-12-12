import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.InputStream

fun aocInputStream(aocDay: Int, type: InputType = InputType.FINAL): InputStream =
    object {}.javaClass.getResourceAsStream("day$aocDay.$type.txt")!!

fun String.toNumberList(s: String = " ") =
    split(s).map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }

enum class InputType {
    FINAL, SAMPLE
}

fun List<String>.getCols() = (0..<first().length).map { colIndex -> map { it[colIndex] } }

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
