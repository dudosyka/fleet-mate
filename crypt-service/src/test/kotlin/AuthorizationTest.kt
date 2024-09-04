import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AuthorizationTest: StringSpec({
    "test" {
        "hello".length shouldBe 3
    }
    "test2" {
        "hello".length shouldBe 5
    }
})