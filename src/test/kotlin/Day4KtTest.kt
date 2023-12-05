import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.InputStream

class Day4KtTest {

    val scratchcards = Scratchcards(InputType.SAMPLE);
    val card1 = Card(1, listOf(41, 48, 83, 86, 17), listOf(83, 86, 6, 31, 17, 9, 48, 53))

    @Test
    fun toDay4() {
        ScratchcardsParser().parseLine(1, "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53") shouldBe card1
    }

    @Test
    fun getPoints() {
        card1.getPoints() shouldBe 8
    }


    @Test
    fun addCardWinningCopies() {
        mutableMapOf(2 to 1, 3 to 2, 4 to 1, 5 to 4).addCardWinningCopies(mockk<Card> {
            every { cardNumber } returns 3
            every { getNbMatchingNumbers() } returns 4
        }) shouldBe mutableMapOf(2 to 1, 3 to 2, 4 to 4, 5 to 7, 6 to 3, 7 to 3)

    }

    @Test
    fun sampleTestEx1() {
        scratchcards.partOne() shouldBe 13
    }

    @Test
    fun sampleTestEx2() {
        scratchcards.partTwo() shouldBe 30
    }
}
