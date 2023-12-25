import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day25Test {

    @Nested
    inner class Day25RunnerTest {
        private val runner = Day25(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 54
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 2023
        }
    }

}
