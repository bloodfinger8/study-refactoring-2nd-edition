package com.toby.refactoring.chapter01

import java.text.NumberFormat
import java.util.*



data class Performance(
    val playID: String,
    val audience: Int
)

data class Invoice(
    val customer: String,
    val performances: List<Performance>
)

data class Play(
    val name: String,
    val type: String
)

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    var totalAmount = 0
    var volumeCredits = 0

    var result = "청구내역 (고객명: ${invoice.customer})\n"

    // 통화(USD) 포맷을 처리해줄 함수
    val format: (Number) -> String = {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 2
        }.format(it)
    }

    for (perf in invoice.performances) {
        val play = plays[perf.playID] ?: throw IllegalArgumentException("해당 ID의 공연 정보를 찾을 수 없습니다: ${perf.playID}")

        val thisAmount =
            when (play.type) {
                "tragedy" -> {
                    var amount = 40000
                    if (perf.audience > 30) {
                        amount += 1000 * (perf.audience - 30)
                    }
                    amount
                }
                "comedy" -> {
                    var amount = 30000
                    if (perf.audience > 20) {
                        amount += 10000 + 500 * (perf.audience - 20)
                    }
                    amount += 300 * perf.audience
                    amount
                }
                else -> {
                    throw IllegalArgumentException("알 수 없는 장르: ${play.type}")
                }
            }
        volumeCredits += Math.max(perf.audience - 30, 0)
        if (play.type == "comedy") volumeCredits += (perf.audience / 5)

        result += " ${play.name}: ${format(thisAmount / 100.0)} (${perf.audience}석)\n"

        totalAmount += thisAmount
    }

    // 전체 청구 요약 정보
    result += "총액: ${format(totalAmount / 100.0)}\n"
    result += "적립 포인트: ${volumeCredits}점"

    return result
}