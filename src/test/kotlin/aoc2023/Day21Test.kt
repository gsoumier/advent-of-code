package aoc2023

import CharPoint
import Coord
import InputType
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day21Test {

    @Nested
    inner class RepeatableMapTest {
        private val runner = Day21(InputType.SAMPLE)

        @Test
        fun testRepeatableMap() {
            runner.charMap.get(Coord(8, 3), true) shouldBe CharPoint(Coord(8, 3), '#')
            runner.charMap.get(Coord(30, 3), true) shouldBe CharPoint(Coord(30, 3), '#')
            runner.charMap.get(Coord(-2, -2), true) shouldBe CharPoint(Coord(-2, -2), '#')
        }

        @Test
        fun testRepeatableMapCoord() {
            runner.charMap.mapCoord(Coord(8, 3)) shouldBe Coord(0, 0)
            runner.charMap.mapCoord(Coord(30, 3)) shouldBe Coord(2, 0)
            runner.charMap.mapCoord(Coord(-12, -12)) shouldBe Coord(-2, -2)
            runner.charMap.mapCoord(Coord(-2, -2)) shouldBe Coord(-1, -1)
        }


    }
    @Nested
    inner class Day21RunnerTest {
        private val runner = Day21(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.initialState.positionsAfter(6).plots shouldBe 16
        }

        @Test
        fun partTwo_method1() {
            runner.calculatePart2Method1(1000) shouldBe 668697
        }

        @Test
        fun partTwo_method2() {
            runner.calculatePart2Method2(1000) shouldNotBe 668697
        }

        @Test
        fun partTwo_real_method1() {
            Day21().calculatePart2Method1(26501365, 1) shouldBe 584211423220706L
        }

        @Test
        fun partTwo_real_method2() {
            Day21().calculatePart2Method2(26501365) shouldBe 584211423220706L
        }

    }

}
