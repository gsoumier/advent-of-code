package aoc2024

import Coord
import InputType
import Vector
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day14Test {

    @Nested
    inner class Day14RunnerTest {
        private val runner = Day14(InputType.SAMPLE)

        @Test
        fun testMove() {
            val robot = Robot(0,Coord(2, 4), Vector(Coord(2, -3) ))
            runner.move(robot, 1).second shouldBe Coord(4, 1)
            runner.move(robot, 2).second shouldBe Coord(6, 5)
            runner.move(robot, 5).second shouldBe Coord(1, 3)
            runner.move(robot, 100).second shouldBe Coord(4, 5)


        }
        @Test
        fun testMoveRobots() {
            runner.move(runner.lines[0], 100).second shouldBe Coord(3, 5)
            runner.move(runner.lines[0], 100).second shouldBe Coord(3, 5)
        }


        @Test
        fun partOne() {
            runner.partOne() shouldBe 12
        }

    }

}
