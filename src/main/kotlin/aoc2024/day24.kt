package aoc2024

import AocRunner
import InputType
import StringLineParser
import splitWhen
import java.lang.Math.pow

typealias WireName = String

enum class Operation {
    AND {
        override fun apply(a: Boolean, b: Boolean) = a && b
    },
    OR {
        override fun apply(a: Boolean, b: Boolean) = a || b
    },
    XOR {
        override fun apply(a: Boolean, b: Boolean) = a xor b
    };

    abstract fun apply(a: Boolean, b: Boolean) : Boolean
}

data class Wire(val name: WireName, val value: Boolean)
data class Gate(val input1: WireName, val operation: Operation, val input2: WireName, val result: WireName)

class Day24(inputType: InputType = InputType.FINAL) : AocRunner<String, String>(
    24,
    StringLineParser,
    inputType
) {
    val initialWires: Map<WireName, Wire>
    val gates: List<Gate>

    init {
        val (wireLines, opLines) = lines.splitWhen()
        initialWires = wireLines
            .map { it.split(": ").let { Wire(it[0], it[1] == "1") } }
            .associateBy { it.name }
        gates = opLines
            .map {
                it.split(" -> ").let { (a, res) ->
                    a.split(" ").let { (w1, op, w2) -> Gate(w1, Operation.valueOf(op), w2, res) }
                }
            }
    }


    override fun partOne(): String {
        val wires = initialWires.toMutableMap()
        calculate(wires)
        return wires.binaryValues("z").toDecimal().toString()
    }

    private fun List<Wire>.toDecimal() = mapIndexed { index, wire -> if (wire.value) pow(2.0, index.toDouble()).toLong() else 0 }
        .sum()

    private fun MutableMap<WireName, Wire>.binaryValues(prefix: String) = values
        .filter { it.name.startsWith(prefix) }
        .sortedBy { it.name }

    private fun calculate(initialWires: MutableMap<WireName, Wire>) {
        val queue = ArrayDeque<Gate>()
        val (gates1, gates2) = gates.partition { it.input1.first() in setOf('x', 'y') }
        queue.addAll(gates1)
        queue.addAll(gates2)
        while (queue.isNotEmpty()) {
            val gate = queue.removeFirst()
            val w1 = initialWires[gate.input1]?.value
            val w2 = initialWires[gate.input2]?.value
            if (w1 == null || w2 == null) {
                queue.add(gate)
            } else {
                Wire(gate.result, gate.operation.apply(w1, w2)).apply { initialWires[name] = this }
            }
        }
    }

    override fun partTwo(): String {
        var r : WireName = gates.findOpOnIndex(Operation.AND, "00")

        val errors = mutableListOf<Pair<WireName, WireName>>()

        gates.groupBy { it.operation }.forEach { o, u ->
            println()
            println("Op $o : ${u.size}")
            val (l1, l2) = u.partition { it.input1.first() in setOf('x', 'y') }
            l1.sortedBy { it.input1.drop(1) }.forEach { println(it) }
            println()
            l2.sortedBy { it.input1.drop(1) }.forEach { println(it) }
            println()
        }

        (1..44).forEach {
            val index = it.toString().padStart(2, '0')
            r = r.correct(errors)
            var n = gates.findOpOnIndex(Operation.XOR, index).correct(errors)
            var a1 = gates.findOpOnIndex(Operation.AND, index).correct(errors)
            var z = gates.findOpOn(Operation.XOR, r, n)?.correct(errors)
            var a2 = gates.findOpOn(Operation.AND, r, n)?.correct(errors)
            if(z == null || a2 == null){
                n = gates.findOpOn(Operation.XOR, r)?.takeIf { gates.findOpOn(Operation.AND, r) == it }
                    ?.also { errors.add(n to it) } ?: n
                r = gates.findOpOn(Operation.XOR, n)?.takeIf { gates.findOpOn(Operation.AND, n) == it }
                    ?.also { errors.add(r to it) } ?: r
            }
            z = gates.findOpOn(Operation.XOR, r, n)!!
            a2 = gates.findOpOn(Operation.AND, r, n)!!
            if(z != "z$index")
                errors.add(z to "z$index")
            a1 = a1.correct(errors)
            a2 = a2.correct(errors)
            val newR = gates.findOpOn(Operation.OR, a1, a2)?.correct(errors)
            if(newR == null){
                a1 = gates.findOpOn(Operation.OR, a2)
                    ?.also { errors.add(a2!! to it) } ?: a1
                a2 = gates.findOpOn(Operation.OR, a1)
                    ?.also { errors.add(a1 to it) } ?: a2
            }
            r = gates.findOpOn(Operation.OR, a1, a2)!!
        }
        return errors.filter { it.first != it.second }.flatMap { it.toList() }.sorted().joinToString(",")
    }

    private fun List<Gate>.findOpOnIndex(operation: Operation, suffix: String) = find { it.operation == operation && it.input1.endsWith(
        suffix
    ) }!!.result

    private fun List<Gate>.findOpOn(operation: Operation, a: String, b: String) = find { it.operation == operation
            && setOf(it.input1, it.input2) == setOf(a, b) }?.result

    private fun List<Gate>.findOpOn(operation: Operation, a: String) = find { it.operation == operation
            && it.input1 == a }?.input2 ?: find { it.operation == operation
            && it.input2 == a }?.input1

}

private fun WireName.correct(errors: List<Pair<WireName, WireName>>): WireName {
    return errors.find { it.first == this }?.second ?: errors.find { it.second == this }?.first ?: this
}


fun main() {
    Day24().run()
}
