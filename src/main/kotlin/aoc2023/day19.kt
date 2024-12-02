package aoc2023

import AocRunner
import InputType
import StringLineParser
import splitWhen

data class Part(
    val notes: Map<Char, Int>,
) {

}

data class Workflow(
    val name: String,
    val rules: List<WorkflowRule>,
    val default : String
) {
    fun getDest(part: Part): String{
       return  rules.firstOrNull{ it.isValid(part) }?.dest ?: default
    }
}

data class WorkflowRule (
    val cat : Char,
    val lessThan: Boolean,
    val value: Int,
    val dest: String,
) {
    fun isValid(part: Part): Boolean {
        val diff = part.notes[cat]!! - value
        return if(lessThan)  diff < 0 else diff > 0
    }
}

data class Input(val workflows : Map<String, Workflow>, val parts : List<Part>)


class Day19(inputType: InputType = InputType.FINAL) : AocRunner<String, Long>(
    19, // FIXME : bien penser à changer
    StringLineParser,
    inputType
) {

    val input = parseInput(lines)

    private fun parseInput(lines: List<String>): Input {
        val (workflowStr, partStr) = lines.splitWhen { it.isEmpty() }
        return Input( workflowStr.map {
            val (name, rulesStr) = it.dropLast(1).split("{")
            val rules = rulesStr.split(",")
            Workflow(name, rules.dropLast(1).map {
                val (cond, dest) = it.split(":")
                WorkflowRule(cond[0], cond[1] == '<', cond.drop(2).toInt(), dest)
            }, rules.last())
        }.associateBy { it.name }
        , partStr.map { Part(it.drop(1).dropLast(1).split(",").map { it[0] to it.drop(2).toInt() }.toMap()) })
    }


    override fun partOne(): Long {
        return input.parts.filter { isAccepted(it, "in") }.sumOf { it.notes.values.sum().toLong() }
    }

    data class PartRanges(val map: Map<Char, IntRange> = mapOf(
        'x' to 1..4000,
        'm' to 1..4000,
        'a' to 1..4000,
        's' to 1..4000,
    )){
        fun filter(rule: WorkflowRule): Pair<PartRanges?, PartRanges?> {
            val initialRange = map[rule.cat]!!
            val v = rule.value
            return initialRange.separateValidAndInvalidRanges(rule, v).let {
                it.first?.toPartRanges(rule.cat) to it.second?.toPartRanges(rule.cat)
            }
        }

        private fun IntRange.separateValidAndInvalidRanges(
            rule: WorkflowRule,
            v: Int
        ) = if (rule.lessThan) {
            when {
                last < v -> this to null
                first > v -> null to this
                else -> first..<v to v..last
            }
        } else {
            when {
                first > v -> this to null
                last < v -> null to this
                else -> v + 1..last to first..v
            }
        }

           fun splitAllLimits(c: Char, limits : List<Int>): List<PartRanges> {
               val intRange = map[c]!!
               return limits.filter { it in intRange }.zipWithNext().mapIndexed { index, pair ->  pair.first.let { if(index>0) it+1 else it }..pair.second }.map { it.toPartRanges(c) }
           }

        private fun IntRange.toPartRanges(cat: Char): PartRanges {
            return PartRanges(map.mapValues { e -> if(e.key == cat) this else e.value })
        }
    }

    private fun getAcceptedRanges(inputRange: PartRanges, workflowName: String = "in"): List<PartRanges> {
        val workflow = input.workflows[workflowName]!!
        val (validPartRanges, remaining) = workflow.rules.fold<WorkflowRule, Pair<List<PartRanges>, PartRanges?>>(emptyList<PartRanges>() to inputRange) { acc, rule ->
            val partRange = acc.second ?: return acc.first
            val (valid, notValid) = partRange.filter(rule)
            valid?.let {
                acc.first + it.partRangesForDest(rule.dest)
            }?.let { it to notValid } ?: acc
        }
        return validPartRanges + (remaining?.let { it.partRangesForDest(workflow.default)} ?: emptyList())

    }

    private fun PartRanges.partRangesForDest(dest: String) = when (dest) {
        "A" -> listOf(this)
        "R" -> emptyList()
        else -> getAcceptedRanges(this, dest)
    }

    private fun isAccepted(it: Part, workflowName: String): Boolean {
        val workflow = input.workflows[workflowName]!!
        val dest = workflow.getDest(it)
        return when(dest){
            "A" -> true
            "R" -> false
            else -> isAccepted(it, dest)
        }
    }

    override fun partTwo(): Long {
        val acceptedRanges: Set<PartRanges> = getAcceptedRanges(PartRanges()).toSet()
//            .splitAllLimits('x')
//            .splitAllLimits('m')
//            .splitAllLimits('a')
//            .splitAllLimits('s')
        return acceptedRanges.sumOf { it.map.values.map { it.last - it.first + 1 }.fold(1L) { acc, i -> acc * i } }
    }

    private fun Set<PartRanges>.splitAllLimits(c: Char): Set<PartRanges> {
        val sortedLimits = flatMap { it.map[c]!!.let { listOf(it.first, it.last) } }.toSet().sorted()
        return flatMap { range->  range.splitAllLimits(c, sortedLimits)}.toSet()
    }

}


fun main() {
    Day19().run()
}
