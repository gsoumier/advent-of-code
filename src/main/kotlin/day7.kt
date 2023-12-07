enum class HandType {
    FIVE {
        override fun match(value: String, jokerRule: Boolean) =
            value.maxCardNumber(jokerRule) == 5
    },
    FOUR {
        override fun match(value: String, jokerRule: Boolean) =
            value.maxCardNumber(jokerRule) == 4
    },
    FULL {
        override fun match(value: String, jokerRule: Boolean) =
            if(jokerRule && value.numberOfJokers() == 1)
                value.cardNumbers().filter { it.second == 2 }.size == 2
        else
            value.cardNumbers().map { it.second }.containsAll(listOf(2, 3))
    },
    THREE {
        override fun match(value: String, jokerRule: Boolean) =
            value.maxCardNumber(jokerRule) == 3
    },
    TWO_PAIRS {
        override fun match(value: String, jokerRule: Boolean) =
            value.cardNumbers().filter { it.second == 2 }.size == 2
    },
    ONE_PAIR {
        override fun match(value: String, jokerRule: Boolean) =
            value.maxCardNumber(jokerRule) == 2
    },
    HIGH {
        override fun match(value: String, jokerRule: Boolean) =
            true
    };

    abstract fun match(value: String, jokerRule: Boolean): Boolean

    fun String.cardNumbers(): List<Pair<Char, Int>> = map { it }.groupBy { it }.map { (k, v) -> k to v.size }

    fun String.maxCardNumber(jokerRule: Boolean) =
        if (jokerRule)
            cardNumbers().filter { it.first != 'J' }.maxOfOrNull { it.second }?.let { it + numberOfJokers() } ?: 5
        else
            cardNumbers().maxOfOrNull { it.second }


    fun String.numberOfJokers(): Int =
        (cardNumbers().find { it.first == 'J' }?.second ?: 0)
}

data class CamelCardsLine(
    val hand: CamelHand,
    val bit: Int
) : Comparable<CamelCardsLine> {
    override fun compareTo(other: CamelCardsLine): Int {
        return hand.compareTo(other.hand)
    }

}

data class CamelHand(val value: String) : Comparable<CamelHand> {
    fun getHandType(jokerRule: Boolean = false): HandType {
        return HandType.entries.first { it.match(value, jokerRule) }
    }

    fun String.power(jokerRule: Boolean): Int {
        return when (this) {
            "A" -> 14
            "K" -> 13
            "Q" -> 12
            "J" -> if(jokerRule) 0 else 11
            "T" -> 10
            else -> toInt()
        }
    }

    override fun compareTo(other: CamelHand): Int {
        val jokerRule = false
        return compareTo(other, jokerRule)
    }

    fun compareTo(other: CamelHand, jokerRule: Boolean): Int {
        (other.getHandType(jokerRule).ordinal - getHandType(jokerRule).ordinal)
            .takeIf { it != 0 }
            ?.let { return it }
        return value.zip(other.value).map { ("" + it.first).power(jokerRule ) - ("" + it.second).power(jokerRule) }
            .dropWhile { it == 0 }
            .first()
    }
}

object CamelCardsComparatorWithJokerRule : Comparator<CamelCardsLine> {
    override fun compare(o1: CamelCardsLine?, o2: CamelCardsLine?): Int {
        if(o1 == null || o2 == null)
            return 0
        return o1.hand.compareTo(o2.hand, true)
    }
}

class CamelCardsParser : LineParser<CamelCardsLine> {
    override fun parseLine(index: Int, line: String): CamelCardsLine {
        val split = line.split(" ")
        return CamelCardsLine(
            CamelHand(split[0]), split[1].toInt()
        )
    }
}

class CamelCards(inputType: InputType = InputType.FINAL) : AocRunner<CamelCardsLine, Int>(
    7,
    CamelCardsParser(),
    inputType
) {
    override fun partOne(): Int {
        return inputLines.sorted().mapIndexed { i, card -> (i + 1) * card.bit }.sum()
    }

    override fun partTwo(): Int {
        val cardsLines = inputLines.sortedWith(CamelCardsComparatorWithJokerRule)
        cardsLines.forEach { println(it) }
        return cardsLines.mapIndexed { i, card -> (i + 1) * card.bit }.sum()

    }

    override fun onEach(index: Int, line: CamelCardsLine) {
        super.onEach(index, line)
    }
}


fun main() {
    CamelCards().run()
}
