package com.toby.refactoring.chapter01

import java.text.NumberFormat
import java.util.*



fun statement3(invoice: Invoice, plays: Map<String, Play>): String {
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
    return renderPlainText3(statementData, plays)
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


private fun renderPlainText3(data: StatementData3, plays: Map<String, Play>): String {
    var result = "청구내역 (고객명: ${data.customer})\n";

    for (perf in data.performances) {
        result += " ${perf.play.name}: ${usd3(perf.amount)} (${perf.audience}석)\n"
    }

    result += "총액: ${usd3(totalAmount3(data, plays))}\n"
    result += "적립 포인트: ${totalVolumeCredits3(data, plays)}점"
    return result
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