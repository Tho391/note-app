package com.thomas.mynoteapp.utils

import java.math.BigInteger

fun main() {
    val a = listOf(2, 3, 1, 5, 6, 7, 8, 9, 10).toIntArray()

    println(solution(a))
}

fun solution(A: IntArray): Int {
    // write your code in Kotlin
    var sumA: BigInteger = 0.toBigInteger()
    for (i in A) sumA += i.toBigInteger()

    val n: BigInteger = A.size.toBigInteger().inc()
    val n1 = n.inc()

    val sum: BigInteger = n * n1 / 2.toBigInteger()
    return (sum - sumA).toInt()
}