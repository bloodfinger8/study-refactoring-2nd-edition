package com.toby.refactoring.chapter01

import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.Assertions.*

class PerformanceFeeBillingKtTest : FunSpec ({
    test("statement test") {
        val invoice = invoice()
        val plays = plays()

        val result1 = statement(invoice, plays)
        val result2 = statement2(invoice, plays)
        val result3 = statement3(invoice, plays)

        val expected = """
            |청구내역 (고객명: BigCo)
            | Hamlet: ${'$'}650.00 (55석)
            | As You Like It: ${'$'}580.00 (35석)
            | Othello: ${'$'}500.00 (40석)
            |총액: ${'$'}1,730.00
            |적립 포인트: 47점
            """.trimMargin()

        assertEquals(expected, result1)
        assertEquals(expected, result2)
        assertEquals(expected, result3)
    }
})

private fun plays(): Map<String, Play> {
    return mapOf(
        "hamlet" to Play("Hamlet", "tragedy"),
        "as-like" to Play("As You Like It", "comedy"),
        "othello" to Play("Othello", "tragedy")
    )
}

private fun invoice(): Invoice {
    return Invoice(
        "BigCo",
        listOf(
            Performance("hamlet", 55),
            Performance("as-like", 35),
            Performance("othello", 40)
        )
    )
}