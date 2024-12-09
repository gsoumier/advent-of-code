package aoc2024

import AocRunner
import InputType
import StringLineParser

data class DiskPart(
    val size: Int,
    val fileId: Long? = null,
) {
    val isFreeSpace: Boolean
        get() = fileId == null
}


class Day9(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    9,
    StringLineParser,
    inputType
) {
    private val initial = lines.first().map { "$it".toInt() }.mapIndexed { idx, it ->
        DiskPart(it, idx.takeIf { it % 2 == 0 }?.let { it.toLong() / 2 })
    }

    override fun partOne(): Long {
        val fragmented = initial.toUnitParts().toMutableList()
        var emptyPartIndex = fragmented.indexOfFirst { it.isFreeSpace }
        while (emptyPartIndex > 0) {
            fragmented.cleanEndOfDisk()
            fragmented[emptyPartIndex] = fragmented.removeLast()
            emptyPartIndex = fragmented.indexOfFirst { it.isFreeSpace }
        }
        return fragmented.checksum()
    }

    override fun partTwo(): Long {
        val fragmented = initial.toMutableList()
        var currentId = fragmented.maxOf { it.fileId ?: 0 }
        while (currentId > 0) {
            val fileIndex = fragmented.indexOfLast { it.fileId == currentId }
            val file = fragmented[fileIndex]
            val spaceIndex = fragmented.indexOfFirst { it.fileId == null && it.size >= file.size }
            if (spaceIndex > -1 && spaceIndex < fileIndex) {
                val space = fragmented[spaceIndex]
                fragmented[fileIndex] = DiskPart(file.size, null)
                fragmented[spaceIndex] = file
                if (space.size > file.size) {
                    fragmented.add(spaceIndex + 1, DiskPart(space.size - file.size, null))
                }
            }
            currentId--
        }
        return fragmented.checksum()
    }

    private fun MutableList<DiskPart>.cleanEndOfDisk() {
        while (last().isFreeSpace) {
            removeLast()
        }
    }

    private fun List<DiskPart>.checksum() = toUnitParts()
        .mapIndexed { index, diskPart -> diskPart.fileId?.let { it * index } ?: 0 }
        .sum()

    private fun List<DiskPart>.toUnitParts() =
        flatMap { diskPart: DiskPart -> (0..<diskPart.size).map { diskPart.copy(size = 1) } }
}


fun main() {
    Day9().run()
}
