import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day20Test {

    @Nested
    inner class Day20RunnerTest {
        private val runner = Day20(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 11687500
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 1
        }
    }

}
