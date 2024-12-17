package aoc2024

import AocRunner
import InputType
import StringLineParser
import toNumberList
import kotlin.math.pow

data class Register(var a: Long, var b: Long = 0, var c: Long = 0) {

    fun performOp(opcode: Int, operand: Int, out: MutableList<Int>): Int? {
        when (opcode) {
            0 -> adv(operand)
            1 -> blx(operand)
            2 -> bst(operand)
            3 -> return jnz(operand)
            4 -> bxc()
            5 -> out.add(comboMod8(operand).toInt())
            6 -> bdv(operand)
            7 -> cdv(operand)
            else -> error("")
        }
        return null
    }

    fun adv(operand: Int) {
        a = a.dv(combo(operand))
    }

    fun bdv(operand: Int) {
        b = a.dv(combo(operand))
    }

    fun cdv(operand: Int) {
        c = a.dv(combo(operand))
    }

    fun blx(operand: Int) {
        b = b.xor(operand.toLong())
    }

    fun bst(operand: Int) {
        b = comboMod8(operand)
    }

    fun jnz(operand: Int): Int? {
        return operand.takeIf { a != 0L }
    }

    fun bxc() {
        b = b.xor(c)
    }

    fun comboMod8(operand: Int) = combo(operand) % 8

    fun combo(operand: Int): Long {
        return when (operand) {
            4 -> a
            5 -> b
            6 -> c
            7 -> error("7 should not be used")
            else -> operand.toLong()
        }
    }

    private fun Long.dv(operand: Long) = (this / 2.toDouble().pow(operand.toDouble())).toLong()
}

class Day17(inputType: InputType = InputType.FINAL) : AocRunner<String, String>(
    17,
    StringLineParser,
    inputType
) {

    private var regA: Long = lines.first().substringAfter(": ").toLong()
    private val program: List<Int> = lines.last().substringAfter(": ").toNumberList(",")

    override fun partOne(): String {
        return runProgram(Register(regA), program).joinToString(",")
    }

    private fun runProgram(register: Register, instructions: List<Int>): List<Int> {
        val out = mutableListOf<Int>()
        var pointer = 0
        while (pointer < instructions.size) {
            val opcode = instructions[pointer]
            val operand = instructions[pointer + 1]
            register.performOp(opcode, operand, out)?.let { pointer = it } ?: pointer.also { pointer += 2 }
        }
        return out
    }

    override fun partTwo(): String {
        return aPossibilities(listOf(0L), program.reversed()).min().toString()
    }

    private fun aPossibilities(listA: List<Long>, toPrint: List<Int>): List<Long> {
        if (toPrint.isEmpty()) {
            return listA
        }
        val newPossibilities = listA.map { it * 8 }.flatMap {
            (it..<it + 8).filter { a ->
                runProgram(Register(a), program.dropLast(2)).firstOrNull() == toPrint.first()
            }
        }
        return aPossibilities(newPossibilities, toPrint.drop(1))
    }
}

fun main() {
    Day17().run()
}
