package aoc2024

import AocRunner
import InputType
import StringLineParser


class Day23(inputType: InputType = InputType.FINAL) : AocRunner<String, String>(
    23,
    StringLineParser,
    inputType
) {
    private val links: Set<Set<String>> = lines.map { it.split("-").toSet() }.toSet()
    private val computerLinks: Map<String, Set<String>> = links.flatten().associateWith { it.findLinked() }

    private fun String.findLinked() = links.filter { this in it }.mapNotNull { it.find { it != this } }.toSet()

    override fun partOne() = links
        .findConnectedComputers()
        .count { it.any { c -> c.startsWith("t") } }
        .toString()

    override fun partTwo(): String {
        var lans: Set<Set<String>> = links
        while (lans.size > 1) {
            lans = lans.findConnectedComputers()
        }
        return lans.first().sorted().joinToString(",")
    }

    private fun Set<Set<String>>.findConnectedComputers() = flatMap { lan ->
        computerLinks.keys
            .filter { computerLinks[it]!!.containsAll(lan) }
            .map { lan + it }
    }.toSet()

}


fun main() {
    Day23().run()
}
