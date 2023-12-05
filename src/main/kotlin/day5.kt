data class RangeLine(
    val destRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long,
) {
    fun getDestValue(sourceVal: Long): Long? {
        return sourceVal
            .takeIf { sourceRangeStart <= it && it < sourceRangeStart + rangeLength }
            ?.let { it - sourceRangeStart + destRangeStart }
    }

    fun getSourceValue(destVal: Long): Long? {
        return destVal
            .takeIf { destRangeStart <= it && it < destRangeStart + rangeLength }
            ?.let { it - destRangeStart + sourceRangeStart }
    }
}

class RangeMapper(val source: String, val destination: String, private val definedRanges: List<RangeLine>) {
    fun getDestValue(sourceValue: Long) =
        definedRanges.firstNotNullOfOrNull { it.getDestValue(sourceValue) } ?: sourceValue

    fun getSourceValue(destValue: Long) =
        definedRanges.firstNotNullOfOrNull { it.getSourceValue(destValue) } ?: destValue
}

class Day5Parser : LineParser<String> {
    override fun parseLine(index: Int, line: String): String {
        return line
    }

    private fun parseRangeLine(line: String): RangeLine {
        val longs = line.split(" ").map { it.toLong() }
        return RangeLine(longs[0], longs[1], longs[2])
    }

    fun parseMapper(mapperBlockLines: List<String>): RangeMapper {
        val (origin, destination) = mapperBlockLines.first().split(" ")[0].split("-to-")
        return RangeMapper(origin, destination, mapperBlockLines.drop(1).map { parseRangeLine(it) })
    }
}

class Day5(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(5, Day5Parser(), inputType) {

    private val seeds: List<Long> = inputLines.first().substring(7).split(" ").map { it.toLong() }.toList()

    private val mappers: List<RangeMapper> = inputLines.drop(2).splitWhen { it.isBlank() }.map { Day5Parser().parseMapper(it) }

    private fun getDestValue(value: Long, type: String = "seed"): Long {
        val nextMapper: RangeMapper = mappers.firstOrNull { it.source == type } ?: return value
        return getDestValue(nextMapper.getDestValue(value), nextMapper.destination)
    }

    private fun getSourceValue(value: Long, type: String = "location"): Long {
        val nextMapper: RangeMapper = mappers.firstOrNull { it.destination == type } ?: return value
        return getSourceValue(nextMapper.getSourceValue(value), nextMapper.source)
    }

    override fun partOne(): Long {
        return seeds.map { getDestValue(it) }.min()
    }

    override fun partTwo(): Long {
        var index = 0L
        while (!hasSeed(getSourceValue(index))) {
            index++
        }
        return index
    }

    private fun hasSeed(seed: Long): Boolean {
        return seeds.chunked(2) { it[0] to it[1] }.sortedBy { it.first }.firstOrNull {
            it.first <= seed && seed < it.first + it.second
        } != null
    }
}

fun main() {
    Day5().run()
}
