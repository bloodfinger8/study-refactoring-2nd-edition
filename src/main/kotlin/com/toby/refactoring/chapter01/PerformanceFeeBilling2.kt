package com.toby.refactoring.chapter01

import java.text.NumberFormat
import java.util.*



fun statement2(invoice: Invoice, plays: Map<String, Play>): String {
    var result = "청구내역 (고객명: ${invoice.customer})\n"

    for (perf in invoice.performances) {
        result += " ${playFor(perf, plays).name}: ${usd(amountFor(playFor(perf, plays), perf))} (${perf.audience}석)\n"
    }

    result += "총액: ${usd(totalAmount(invoice, plays))}\n"
    result += "적립 포인트: ${totalVolumeCredits(invoice, plays)}점"
    return result
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