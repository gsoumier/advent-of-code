import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day4KtTest {

    val card1 = Card(1, listOf(41, 48, 83, 86, 17), listOf(83, 86, 6, 31, 17, 9, 48, 53))

    @Test
    fun toDay4() {
        assertEquals(
            card1,
            "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53".toDay4()
        )
    }

    @Test
    fun getPoints() {
        assertEquals(8, card1.getPoints())
    }


    @Test
    fun addCardWinningCopies() {
        assertEquals(
            mutableMapOf(2 to 1, 3 to 2, 4 to 4, 5 to 7, 6 to 3, 7 to 3),
            mutableMapOf(2 to 1, 3 to 2, 4 to 1, 5 to 4).addCardWinningCopies(mockk<Card>{
                every { cardNumber } returns 3
                every { getNbMatchingNumbers() } returns 4
            })
        )
    }

    @Test
    fun sampleTestEx1() {
        assertEquals(
            13,
            calculateDay4Ex1(object {}.javaClass.getResourceAsStream("day4.sample.txt"))
        )
    }

    @Test
    fun sampleTestEx2() {
        assertEquals(
            30,
            calculateDay4Ex2(object {}.javaClass.getResourceAsStream("day4.sample.txt"))
        )
    }
}
