import java.io.InputStream
import kotlin.math.pow

data class Card(
    val cardNumber: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>,
) {
    fun getNbMatchingNumbers(): Int {
        val yourWinningNumbers = winningNumbers.intersect(numbers)
        return yourWinningNumbers.size
    }

    fun getPoints(): Int {
        return when (val nb = getNbMatchingNumbers()) {
            0 -> 0
            1 -> 1
            else -> 2.0.pow(nb - 1).toInt()
        }
    }
}

fun String.toDay4(): Card {
    val (card, numbers) = split(": ")
    val (winning, yours) = numbers.split(" | ")
    return Card(
        card.substring(5).trim().toInt(),
        winning.toNumberList(),
        yours.toNumberList(),
    )
}

private fun String.toNumberList() =
    split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }

fun calculateDay4Ex1(stream: InputStream): Int {
    return stream.bufferedReader().readLines().map { it.toDay4().getPoints() }.sum()
}

fun calculateDay4Ex2(stream: InputStream): Int {
    val numberOfCopies = mutableMapOf<Int, Int>()
    val originalCards = stream.bufferedReader().readLines().map { it.toDay4() }
    originalCards.forEach{ card -> numberOfCopies.addCardWinningCopies(card) }
    return numberOfCopies.values.sum() + originalCards.size
}

fun MutableMap<Int, Int>.addCardWinningCopies(card: Card) : MutableMap<Int, Int>{
    val nbMatchingNumbers = card.getNbMatchingNumbers()
    val nbOfCurrentCardCopies = getOrDefault(card.cardNumber, 0)
    (1..nbMatchingNumbers).forEach { winningCopyIndex ->
        compute(card.cardNumber + winningCopyIndex) { _, v ->
            (v ?: 0) + nbOfCurrentCardCopies + 1
        }
    }
    return this
}

fun main(args: Array<String>) {
    println("Ex 1 : " + calculateDay4Ex1(object {}.javaClass.getResourceAsStream("day4.txt")))
    println("Ex 2 : " + calculateDay4Ex2(object {}.javaClass.getResourceAsStream("day4.txt")))
}
