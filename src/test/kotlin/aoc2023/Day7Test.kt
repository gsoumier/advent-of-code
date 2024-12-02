package aoc2023

import InputType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class CamelCardsTest {

    private val sampleCamelCards = CamelCardsLine(CamelHand("KK677"), 28)

    @Nested
    inner class CamelCardsParserTest {
        private val parser = CamelCardsParser()

       @Test
       fun shouldParse(){
           parser.parseLine("KK677 28") shouldBe sampleCamelCards
       }
    }


    @Nested
    inner class CamelCardsLineTest {
        @Test
        fun testHandType() {
            Assertions.assertAll(
                Executable { CamelHand("K867Q").getHandType() shouldBe HandType.HIGH },
                Executable { CamelHand("KK677").getHandType() shouldBe HandType.TWO_PAIRS },
                Executable { CamelHand("KK687").getHandType() shouldBe HandType.ONE_PAIR },
                Executable { CamelHand("KK6K7").getHandType() shouldBe HandType.THREE },
                Executable { CamelHand("KKKK7").getHandType() shouldBe HandType.FOUR },
                Executable { CamelHand("KKKKK").getHandType() shouldBe HandType.FIVE },
                Executable { CamelHand("KK777").getHandType() shouldBe HandType.FULL },
            )
        }

        @Test
        fun testHandTypePart2() {
            Assertions.assertAll(
                Executable { CamelHand("K867Q").getHandType(true) shouldBe HandType.HIGH },
                Executable { CamelHand("KJ687").getHandType(true) shouldBe HandType.ONE_PAIR },
                Executable { CamelHand("KJ6J7").getHandType(true) shouldBe HandType.THREE },
                Executable { CamelHand("K6677").getHandType(true) shouldBe HandType.TWO_PAIRS },
                Executable { CamelHand("KK6J7").getHandType(true) shouldBe HandType.THREE },
                Executable { CamelHand("KKJ77").getHandType(true) shouldBe HandType.FULL },
                Executable { CamelHand("KKJJ7").getHandType(true) shouldBe HandType.FOUR },
                Executable { CamelHand("KKJJJ").getHandType(true) shouldBe HandType.FIVE },
                Executable { CamelHand("KJJJJ").getHandType(true) shouldBe HandType.FIVE },
                Executable { CamelHand("JJJJJ").getHandType(true) shouldBe HandType.FIVE },
            )
        }

        @Test
        fun testOrderDifferentType(){
            (CamelHand("K867Q") < CamelHand("KKKK7")) shouldBe true
        }

        @Test
        fun testOrderSameType(){
            (CamelHand("Q867K") < CamelHand("A867Q")) shouldBe true
            (CamelHand("9867K") < CamelHand("A867Q")) shouldBe true
            (CamelHand("J867K") < CamelHand("286QQ")) shouldBe true
            (CamelHand("JJ678") < CamelHand("JJ87Q")) shouldBe true
        }
    }

    @Nested
    inner class CamelCardsRunnerTest {
        private val runner = CamelCards(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 6440
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 5905
        }
    }

}
