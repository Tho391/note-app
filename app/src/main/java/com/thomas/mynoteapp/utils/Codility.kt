package com.thomas.mynoteapp.utils

fun main() {
    val a = listOf(2, 3, 1, 5, 6, 7, 8, 9, 10).toIntArray()

    println(solution(a))
}

fun solution(A: IntArray): Int {
    // write your code in Kotlin

    var a = A.size + 1
    var b = 0
    for (i in A.indices) {
        a = a.xor(i + 1)
        b = b.xor(A[i])
    }
    return a.xor(b)
}