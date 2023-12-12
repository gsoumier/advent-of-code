import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class DayXXXTest {

    @Nested
    inner class DayXXXRunnerTest {
        private val runner = DayXXX(InputType.SAMPLE)

        @Test
        fun partOne() {
            runner.partOne() shouldBe 51
        }

        @Test
        fun partTwo() {
            runner.partTwo() shouldBe 99
        }
    }

}
