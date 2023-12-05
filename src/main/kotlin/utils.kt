import java.io.InputStream

fun aocInputStream(aocDay: Int, type: InputType = InputType.FINAL): InputStream =
    object {}.javaClass.getResourceAsStream("day$aocDay.$type.txt")!!

fun String.toNumberList(s: String = " ") =
    split(s).map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }

enum class InputType {
    FINAL, SAMPLE
}

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
