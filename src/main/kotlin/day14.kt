data class Plate(
    val nbLines: Int,
    val nbCols: Int,
    val cubes: List<Coord>,
    val rounds: List<Coord>,
) {
    fun rotateQuarterHour(): Plate {
        return Plate(nbCols, nbLines, cubes.rotateQuarterHour().sorted(), rounds.rotateQuarterHour().sorted())
    }

    fun List<Coord>.rotateQuarterHour(): List<Coord> {
        return map { Coord(nbLines - 1 - it.y, it.x) }
    }

    fun tilt(): Plate {
        return copy(rounds = (0..<nbCols).flatMap { colIndex ->
            val cubesIndex = (cubes.filter { it.x == colIndex }.map { it.y } + -1).sorted()
            val roundInCol = rounds.filter { it.x == colIndex }
            cubesIndex.flatMapIndexed { index: Int, cubeIndex: Int ->
                (1..roundInCol.count {
                    it.y in cubeIndex..cubesIndex.getOrElse(
                        index + 1
                    ) { nbLines + 1 }
                }).map { Coord(colIndex, it + cubeIndex) }
            }
        })
    }

    fun weight(): Long {
        return rounds.map { nbLines - it.y }.sum().toLong()
    }

    fun print() : Plate {
//        (0..<nbLines).forEach {y->
//            println((0..<nbCols).map { x -> Coord(x, y) }.map { when {
//                it in cubes -> '#'
//                it in rounds -> 'O'
//                else -> '.'
//            } }.joinToString(""))
//        }
//        println()
        return this
    }
}

class Day14(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    14,
    StringLineParser,
    inputType
) {

    val charPos = lines.toCharMap()
    val initialPlate = Plate(
        lines.size,
        lines.first().length,
        charPos.charPoints.filter { c -> c.value == '#' }.map { it.coord },
        charPos.charPoints.filter { c -> c.value == 'O' }.map { it.coord }
    )

    override fun partOne(): Long {
        return initialPlate.tilt().weight()
    }


    override fun partTwo(): Long {
        val cache = mutableMapOf<Plate, Long>()
        val cacheRep = mutableMapOf<Plate, Long>()
        var currentPlate = initialPlate
        (1..1000).forEach {
            val newPlate = currentPlate.tilt().print() // North
                .rotateQuarterHour().tilt() // West
                .rotateQuarterHour().tilt() // South
                .rotateQuarterHour().tilt() // Est
                .rotateQuarterHour() // Back North

            val get = cache.get(newPlate)
            if(get != null) {
                cacheRep.putIfAbsent(newPlate, it-get)
            } else {
                cache.put(newPlate, it.toLong())
            }
            currentPlate = newPlate
        }

        return cacheRep.keys.first { plate -> ((1000000000 - cache[plate]!!)%cacheRep[plate]!!) == 0L }.weight()
    }

}

fun main() {
    Day14().run()
}
