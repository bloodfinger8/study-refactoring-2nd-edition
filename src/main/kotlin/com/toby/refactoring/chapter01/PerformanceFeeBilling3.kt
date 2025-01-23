package com.toby.refactoring.chapter01

import java.text.NumberFormat
import java.util.*



fun statement3(invoice: Invoice, plays: Map<String, Play>) {
    val statementData = StatementData3(
        invoice.customer,
        invoice.performances.stream()
            .map {
                Performance3(
                    playFor3(it, plays),
                    it.audience,
                    amountFor3(playFor3(it, plays), Performance3(playFor3(it, plays), it.audience)),
                    volumeCreditsFor3(Performance3(playFor3(it, plays), it.audience), plays)
                )
            }
            .toList()
    )
    renderPlainText3(statementData, plays)
}

data class StatementData3(
    var customer: String,
    var performances: List<Performance3>
) {
}

data class Performance3(
    var play: Play,
    var audience: Int,
    val amount: Int = 0,
    val volumeCredits: Int = 0
) {
    constructor() : this(Play("",""), 0)
}


private fun playFor3(aPerformance: Performance, plays: Map<String, Play>): Play =
    plays[aPerformance.playID] ?: throw IllegalArgumentException("해당 ID의 공연 정보를 찾을 수 없습니다: ${aPerformance.playID}")


private fun renderPlainText3(data: StatementData3, plays: Map<String, Play>) {
    println("청구내역 (고객명: ${data.customer})")

    for (perf in data.performances) {
        println(" ${perf.play.name}: ${usd3(perf.amount)} (${perf.audience}석)")
    }

    println("총액: ${usd3(totalAmount3(data, plays))}")
    println("적립 포인트: ${totalVolumeCredits3(data, plays)}점")
}


private fun totalAmount3(data: StatementData3, plays: Map<String, Play>):Int {
    var result = 0
    for (perf in data.performances) {
        result += perf.amount
    }
    return result
}

private fun totalVolumeCredits3(data: StatementData3, plays: Map<String, Play>): Int {
    var result = 0
    for (perf in data.performances) {
        result += perf.volumeCredits
    }
    return result
}

private fun usd3(it: Int): String? = NumberFormat.getCurrencyInstance(Locale.US).apply {
    minimumFractionDigits = 2
}.format(it/100)

private fun volumeCreditsFor3(perf: Performance3, plays: Map<String, Play>): Int {
    var result = 0
    result += Math.max(perf.audience - 30, 0)
    if (perf.play.type == "comedy") {
        result += (perf.audience / 5)
    }
    return result
}

private fun amountFor3(play: Play, aPerformance: Performance3): Int {
    return when (play.type) {
            "tragedy" -> {
                var amount = 40000
                if (aPerformance.audience > 30) {
                    amount += 1000 * (aPerformance.audience - 30)
                }
                amount
            }
            "comedy" -> {
                var amount = 30000
                if (aPerformance.audience > 20) {
                    amount += 10000 + 500 * (aPerformance.audience - 20)
                }
                amount += 300 * aPerformance.audience
                amount
            }
            else -> {
                throw IllegalArgumentException("알 수 없는 장르: ${play.type}")
            }
        }
}






fun main() {
    val invoice = Invoice(
        customer = "BigCo",
        performances = listOf(
            Performance("hamlet", 55),
            Performance("as-like", 35),
            Performance("othello", 40)
        )
    )

    val plays = mapOf(
        "hamlet" to Play("Hamlet", "tragedy"),
        "as-like" to Play("As You Like It", "comedy"),
        "othello" to Play("Othello", "tragedy")
    )

    statement2(invoice, plays)
}