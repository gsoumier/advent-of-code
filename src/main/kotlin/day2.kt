data class Game(val id: Int, val draws: List<Draw>) {
    fun isPossible(limits: Map<String, Int>): Boolean {
        return draws.all { draw -> limits.all { (draw.elements[it.key] ?: 0) <= it.value } }
    }

    fun getPower() =
        colors.map { color -> draws.mapNotNull { it.elements[color] }.max() }.reduce { acc, i -> acc * i }
}

data class Draw(val elements: Map<String, Int>){

}


fun String.toGame() : Game {
    val split = split(": ")

    val id = split[0].substring(5).toInt()
    return Game(id, split[1]
        .split(";")
        .map {
            Draw(
                it.split(",")
                    .map { it.trim().split(" ") }
                    .map { it[1] to it[0].toInt() }
                    .toMap()
            )
        })
}

val colors = listOf("red", "green", "blue")
val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun main(args: Array<String>) {
    val games = object {}.javaClass.getResourceAsStream("day2.txt").bufferedReader().readLines().map { it.toGame() }
    print(games.sumOf { it.getPower() })
}
