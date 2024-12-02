package aoc2023

import InputType
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day24Test {

    @Nested
    inner class Day24CrossTest {

        @Test
        fun `parallel should return null`() {
            p("18, 19, 22 @ -1, -1, -2").getFutureCrossWith(p("20, 25, 34 @ -2, -2, -4")) shouldBe null
        }

        @Test
        fun `cross ok in future`() {
            val futureCrossWith = p("19, 13, 30 @ -2, 1, -2").getFutureCrossWith(p("18, 19, 22 @ -1, -1, -2"))
            futureCrossWith shouldNotBe null
            futureCrossWith!!.first shouldBeIn 14.3..14.4
            futureCrossWith.second shouldBeIn 15.3..15.4
        }

        @Test
        fun `cross ok in past`() {
            val futureCrossWith = p("19, 13, 30 @ -2, 1, -2").getFutureCrossWith(p("20, 19, 15 @ 1, -5, -3"))
            futureCrossWith shouldBe null
        }

        private fun p(s: String) = Day24Parser.parseLine(s)!!

    }
    @Nested
    inner class Day24RunnerTest {
        private val runner = Day24(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.findCrossIn(7, 27) shouldBe 2
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 47
        }
    }

}
