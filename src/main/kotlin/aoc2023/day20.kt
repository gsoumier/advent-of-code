package aoc2023

import AocRunner
import InputType
import LineParser
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

enum class Pulse {
    LOW, HIGHT
}

sealed class Day20Line {
    abstract val name: String
    abstract val destModules: List<String>

    var lowCount = mutableMapOf<Int,List<String>>()
    var highCount = mutableMapOf<Int,List<String>>()

    fun handlePulse(receivedPulse: Pulse, fromModule: String, nbCount: Int): Pulse?{
        if(receivedPulse== Pulse.LOW)
            lowCount.compute(nbCount){ _, old -> old?.plus(fromModule) ?: listOf(fromModule) }
        else
            highCount.compute(nbCount){ _, old -> old?.plus(fromModule) ?: listOf(fromModule) }
        return pulseToSend(receivedPulse, fromModule)
    }
    abstract fun pulseToSend(receivedPulse: Pulse, fromModule: String): Pulse?

    open fun init(lines: List<Day20Line>) {
        lowCount.clear()
        highCount.clear()
    }
}

private const val BROADCASTER = "broadcaster"

data class BroadcasterModule(override val destModules: List<String>) : Day20Line() {
    override val name: String
        get() = BROADCASTER

    override fun pulseToSend(receivedPulse: Pulse, fromModule: String): Pulse {
        return receivedPulse
    }
}

data class TestingModule(override val name: String) : Day20Line() {
    override val destModules: List<String>
        get() = emptyList()

    var lowPulseCount = 0
    var highPulseCount = 0

    override fun pulseToSend(receivedPulse: Pulse, fromModule: String): Pulse? {
        if(receivedPulse == Pulse.LOW) lowPulseCount++ else highPulseCount++
        return null
    }

    fun resetCount(){
        lowPulseCount = 0
        highPulseCount = 0
    }

    fun hasSingleLowPulse() : Boolean {
        return lowPulseCount == 1 // && highPulseCount == 0
    }
}

data class FlipFlopModule(override val name: String, override val destModules: List<String>) : Day20Line() {
    var on = false
    override fun init(lines: List<Day20Line>) {
        super.init(lines)
        on = false
    }

    override fun pulseToSend(receivedPulse: Pulse, fromModule: String): Pulse? {
        if (receivedPulse == Pulse.HIGHT)
            return null
        on = !on
        return if (on) Pulse.HIGHT else Pulse.LOW
    }
}

data class ConjunctionModule(override val name: String, override val destModules: List<String>) : Day20Line() {
    val lastReceived = mutableMapOf<String, Pulse>()

    override fun init(lines: List<Day20Line>) {
        super.init(lines)
        lines.filter { name in it.destModules }.forEach { lastReceived[it.name] = Pulse.LOW }
    }

    override fun pulseToSend(receivedPulse: Pulse, fromModule: String): Pulse {
        lastReceived[fromModule] = receivedPulse
        return Pulse.LOW.takeIf { lastReceived.values.all { it == Pulse.HIGHT } } ?: Pulse.HIGHT
    }
}


class Day20Parser : LineParser<Day20Line> {
    override fun parseLine(index: Int, line: String): Day20Line {
        val (typeAndName, dest) = line.split(" -> ")
        val destModules = dest.split(", ")
        if (typeAndName == BROADCASTER)
            return BroadcasterModule(destModules)
        val name = typeAndName.drop(1)
        if (typeAndName.first() == '%')
            return FlipFlopModule(name, destModules)
        return ConjunctionModule(name, destModules)
    }
}

data class PulsePropagation(val pulse: Pulse, val from: String, val to: String)

class Day20(inputType: InputType = InputType.FINAL) : AocRunner<Day20Line, Long>(
    20,
    Day20Parser(),
    inputType
) {

    val rxModule = TestingModule("rx")

    val modules = (lines + rxModule).associateBy { it.name }

    private fun propagatePulse(count: Int): Pair<Int, Int> {
        val queue: Queue<PulsePropagation> = ArrayBlockingQueue(1000)
        var lowCount = 0
        var highCount = 0
        queue.add(PulsePropagation(Pulse.LOW, "button", BROADCASTER))
        while (queue.isNotEmpty()) {
            val (pulse, from, to) = queue.poll()
            if (pulse == Pulse.LOW) lowCount++ else highCount++
            val module = modules[to] ?: TestingModule(to)
            module.handlePulse(pulse, from, count)?.apply {
                module.destModules.map { PulsePropagation(this, module.name, it) }.forEach {
                    queue.add(it)
                }
            }
        }
        return lowCount to highCount
    }

    fun init() {
        lines.forEach { it.init(lines) }
    }

    override fun partOne(): Long {
        init()
        return (1..1000).map { propagatePulse(it) }
            .reduce { acc, pair -> acc.first + pair.first to acc.second + pair.second }
            .let { it.first.toLong() * it.second }
    }

    override fun partTwo(): Long {
        init()

        val originConjunction = modules.values.find { rxModule.name in it.destModules } ?: error("No conjunction module")
        val originsOfOrigin = modules.values.filter { originConjunction.name in it.destModules }

        var count = 1
        while (!originConjunction.highCount.flatMap { it.value }.containsAll(originsOfOrigin.map { it.name })){
            propagatePulse(count)
            count++
        }
        return originsOfOrigin
            .map { oo -> originConjunction.highCount.filter { it.value.contains(oo.name) }.minOf { it.key } }
            .fold(1L) { acc, i -> acc * i }

    }
}


fun main() {
    Day20().run()
}
