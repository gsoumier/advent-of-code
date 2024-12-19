package aoc2024

import AocRunner
import CharMap
import CharPoint
import Coord
import InputType
import LineParser
import Vector
import toNumberList

data class Robot(
    val id: Int,
    val initial: Coord,
    val move: Vector,
) {

}

class Day14Parser : LineParser<Robot> {
    override fun parseLine(index: Int, line: String): Robot {
        val (pos, dir) = line.split(" ")
        return Robot(
            index,
            pos.drop(2).toNumberList(",").let { (x, y) -> Coord(x, y) },
            Vector(
                dir.drop(2).toNumberList(",").let { (x, y) -> Coord(x, y) },
            )
        )
    }
}

class Day14(inputType: InputType = InputType.FINAL) : AocRunner<Robot, Long>(
    14,
    Day14Parser(),
    inputType
) {

    val wide = if(inputType == InputType.FINAL) 101 else 11
    val height = if(inputType == InputType.FINAL) 103 else 7
    val map = CharMap(wide, height)

    override fun partOne(): Long {
        val robots100 = lines.map { robot -> move(robot, 100).second }
        return safetyFactors(robots100).map { it.count() }.reduce { acc, i -> acc * i }.toLong()
    }

    private fun safetyFactors(robots100: List<Coord>): List<List<Coord>> = robots100
        .filter { it.x != wide / 2 && it.y != height / 2 }
        .partition { it.x < wide / 2 }.toList().flatMap {
                it.partition { it.y < height / 2 }.toList()
            }

    fun move(robot: Robot, times: Int) = robot.id to map.inInitialMap(robot.initial.to(robot.move, times))

    override fun partTwo(): Long {
        var bestSymetricalScore = 0
        var iter = 0L
        (0..10000).forEach {
            val robots = lines.map { robot -> move(robot, it).second }
            val symetricalScore = symetricalScore(robots)
            if (symetricalScore> bestSymetricalScore) {
                bestSymetricalScore = symetricalScore
                iter = it.toLong()
                printRobots(robots)
                println(it)
            }
        }
        return iter
    }

    private fun symetricalScore(robots: List<Coord>): Int {
        return robots.count { robots.contains(map.ySym(it)) }
    }

    private fun printRobots(robots: List<Coord>) {
        CharMap(map.charPoints.map { if(robots.contains(it.coord)) CharPoint(it.coord, '#') else it }).print()
    }

}


fun main() {
    Day14().run()
}
