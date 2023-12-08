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

class ScratchcardsParser : LineParser<Card> {
    override fun parseLine(index: Int, line: String): Card {
        val (card, numbers) = line.split(": ")
        val (winning, yours) = numbers.split(" | ")
        return Card(
            card.substring(5).trim().toInt(),
            winning.toNumberList(),
            yours.toNumberList(),
        )
    }

}

class Scratchcards(inputType: InputType = InputType.FINAL) : AocRunner<Card, Int>( 4, ScratchcardsParser(), inputType) {

    override fun partOne(): Int {
        return lines.sumOf { it.getPoints() }
    }

    override fun partTwo(): Int {
        val numberOfCopies = mutableMapOf<Int, Int>()
        lines.forEach{ card -> numberOfCopies.addCardWinningCopies(card) }
        return numberOfCopies.values.sum() + lines.size
    }
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

fun main() {
    Scratchcards().run()
}
