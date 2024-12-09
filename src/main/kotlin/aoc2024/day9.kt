package aoc2024

import AocRunner
import InputType
import StringLineParser

sealed class BlockSpace {
    object Empty : BlockSpace()
    data class FilePart(val id: Long) : BlockSpace()
}

data class Space(
    val id: Long?,
    val size: Int,
)


class Day9(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    9,
    StringLineParser,
    inputType
) {
    val initial = lines.first().map { "$it".toInt() }

    override fun partOne(): Long {
        val representation = spaceRepresentation().representation()
        val reArranged = representation.dropLastWhile { it == BlockSpace.Empty }.toMutableList()
        var freeIndex = reArranged.indexOfFirst { it is BlockSpace.Empty }
        while (freeIndex > 0) {
            reArranged[freeIndex] = reArranged.removeLast()
            while (reArranged.last() == BlockSpace.Empty) {
                reArranged.removeLast()
            }
            freeIndex = reArranged.indexOfFirst { it is BlockSpace.Empty }
        }
        return calculate(reArranged)
    }

    override fun partTwo(): Long {


        val result = spaceRepresentation().toMutableList()
        var current = result.maxOf { it.id ?: 0 }
        while (current > 0) {
            val fileIndex = result.indexOfLast { it.id == current }
            val file = result[fileIndex]
            val spaceIndex = result.indexOfFirst { it.id == null && it.size >= file.size }
            if (spaceIndex > -1 && spaceIndex < fileIndex) {
                val space = result[spaceIndex]
                result[fileIndex] = Space(null, file.size)
                result[spaceIndex] = file
                if (space.size > file.size) {
                    result.add(spaceIndex + 1, Space(null, space.size - file.size))
                }
            }
            current--
        }


        return calculate(result.representation())
    }

    private fun calculate(reArranged: List<BlockSpace>) = reArranged
        .mapIndexed { index, blockSpace -> index * ((blockSpace as? BlockSpace.FilePart)?.id ?: 0) }
        .sum()

    private fun List<Space>.representation(): List<BlockSpace> {
        return flatMap { space ->
            (0..<space.size).map {
                (if (space.id != null) {
                    BlockSpace.FilePart(space.id)
                } else BlockSpace.Empty)
            }
        }
    }

    private fun spaceRepresentation(): List<Space> {
        var id = 0L
        var isFile = true
        return (initial.map {
            (if (isFile) {
                Space(id, it).also { id++ }
            } else Space(null, it)).also { isFile = !isFile }
        })
    }


}


fun main() {
    Day9().run()
}
