package aoc2024

import Coord
import Direction
import Direction.*
import InputType
import Vector
import com.github.shiguruikai.combinatoricskt.permutations
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day21Test {

    @Nested
    inner class Day21RunnerTest {
        private val runner = Day21(InputType.SAMPLE)


        @Test
        fun testPermutation() {
            listOf(S, E, S).permutations().toList().toSet() shouldContainAll setOf(
                listOf(S, S, E),
                listOf(S, E, S),
                listOf(E, S, S),
            )
        }


        @Test
        fun testDecompose() {
            Vector(Coord(3, 1)).decompose() shouldContainAll listOf(E, E, E, S)
            Vector(Coord(-2, 0)).decompose() shouldContainAll listOf(W, W)
        }


        @Test
        fun testFindBestCombination() {
            runner.findBestCombination("029A", runner.numericMap, runner.numericCombinations) shouldContainAll setOf(
                "<A^A>^^AvvvA",
                "<A^A^>^AvvvA",
                "<A^A^^>AvvvA"
            )
        }

        @Test
        fun testFindFullBestCombination() {
            runner.findFullBestCombination("029A") shouldBe "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
        }
        @Test
        fun testFindFullBestCombinationPart2() {
            runner.findFullBestCombination("029A", 25) shouldBe "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
        }

        @Test
        fun partOne() {
            runner.partOne() shouldBe 126384
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 0
        }
    }

}
