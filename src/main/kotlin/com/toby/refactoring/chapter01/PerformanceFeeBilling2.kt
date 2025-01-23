package com.toby.refactoring.chapter01

import java.text.NumberFormat
import java.util.*



fun statement2(invoice: Invoice, plays: Map<String, Play>) {
    println("청구내역 (고객명: ${invoice.customer})")

    for (perf in invoice.performances) {
        println(" ${playFor(perf, plays).name}: ${usd(amountFor(playFor(perf, plays), perf))} (${perf.audience}석)")
    }

    println("총액: ${usd(totalAmount(invoice, plays))}")
    println("적립 포인트: ${totalVolumeCredits(invoice, plays)}점")
}

private fun totalAmount(invoice: Invoice, plays: Map<String, Play>):Int {
    var result = 0
    for (perf in invoice.performances) {
        result += amountFor(playFor(perf, plays), perf)
    }
    return result
}

private fun totalVolumeCredits(invoice: Invoice, plays: Map<String, Play>): Int {
    var result = 0
    for (perf in invoice.performances) {
        result += volumeCreditsFor(perf, plays)
    }
    return result
}

private fun usd(it: Int): String? = NumberFormat.getCurrencyInstance(Locale.US).apply {
    minimumFractionDigits = 2
}.format(it/100)

private fun volumeCreditsFor(perf: Performance, plays: Map<String, Play>): Int {
    var result = 0
    result += Math.max(perf.audience - 30, 0)
    if (playFor(perf, plays).type == "comedy") {
        result += (perf.audience / 5)
    }
    return result
}

private fun playFor(aPerformance: Performance, plays: Map<String, Play>): Play =
    plays[aPerformance.playID] ?: throw IllegalArgumentException("해당 ID의 공연 정보를 찾을 수 없습니다: ${aPerformance.playID}")


private fun amountFor(play: Play, aPerformance: Performance): Int {
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