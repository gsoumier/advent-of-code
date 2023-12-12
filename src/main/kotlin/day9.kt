
data class Day9Line(
    val numbers: List<Long>,
) {
    fun diffSequences() : List<List<Long>>{
        val res = mutableListOf(numbers)
        var current = numbers
        while(!current.all { it == 0L }){
            current = current.mapIndexedNotNull { index, l -> current.getOrNull(index+1)?.let { it- l } }
            res.add(current)
        }
//        println(res)
        return res
    }

    fun nextNumbers(): List<Long>{
        val res = mutableListOf(0L)
        diffSequences().reversed().forEachIndexed { index, longs -> res.add(if(index  ==0) 0L else longs.last() + res.last())  }
        return res
    }

    fun previousNumbers(): List<Long>{
        val res = mutableListOf(0L)
        diffSequences().reversed().forEachIndexed { index, longs -> res.add(if(index  ==0) 0L else longs.first() - res.last())  }
        return res
    }

}

class Day9Parser : LineParser<Day9Line> {
    override fun parseLine(index: Int, line: String): Day9Line {
        return Day9Line(
            line.split( " ").map { it.toLong() },
        )
    }
}

class Day9(inputType: InputType = InputType.FINAL) : AocRunner<Day9Line, Long>(
    9,
    Day9Parser(),
    inputType
) {
    override fun partOne(): Long {
        return lines.map { it.nextNumbers().last() }.sum()
    }

    override fun partTwo(): Long {
        return lines.map { it.previousNumbers().last() }.sum()
    }

}


fun main() {
    Day9().run()
}
