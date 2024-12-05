
data class Day5Line(
    val line: String,
) {

}

class Day5Parser : LineParser<Day5Line> {
    override fun parseLine(index: Int, line: String): Day5Line {
        return Day5Line(
            line,
        )
    }
}

class Day5(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    5,
    StringLineParser,
    inputType
) {
    val allRules = lines.splitWhen {it.isEmpty()}[0].map { it.split("|").map { it.toInt() } }.map { it[0] to it[1] }
    val rules = allRules.groupBy({ it.second }, { it.first })
    val updates = lines.splitWhen {it.isEmpty()}[1].map { it.split(",").map { it.toInt() } }

    override fun partOne(): Long {
        return updates
            .filter {
                !it.isInWrongOrder()
            }.sumOf { it[it.size/2] }.toLong()
    }

    override fun partTwo(): Long {
        return updates
            .filter {
                it.isInWrongOrder()
            }.map{
                it.sort()
            }
            .sumOf { it[it.size/2] }.toLong()
    }

    private fun List<Int>.isInWrongOrder(): Boolean {
        forEachIndexed { index, page ->
            rules[page]?.let {
                pageRules -> if (this.drop(index+1).any { pageRules.contains(it) }) return true
            }
        }
        return false
    }

    private fun List<Int>.sort() : List<Int> {
        val res = toMutableList()
        forEachIndexed { i, a -> forEachIndexed{ j, b ->
            if(allRules.contains(res[i] to res[j]))
                res.flip(i to j)
        } }
        return res
    }
//    private fun List<Int>.sort() : List<Int> {
//        val res = toMutableList()
//        forEachIndexed { i, a -> forEachIndexed{ j, b ->
//            if(allRules.contains(a to b))
//                res.flip(i to j)
//        } }
//        return res
//    }
}

private fun <E> MutableList<E>.flip(toSwich: Pair<Int, Int>) {
    val a = this[toSwich.first]
    val b = this[toSwich.second]
    this[toSwich.first] = b
    this[toSwich.second] = a
}


fun main() {
    Day5().run()
}
