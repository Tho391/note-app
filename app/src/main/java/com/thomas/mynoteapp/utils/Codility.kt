package com.thomas.mynoteapp.utils

fun main() {
    val a = listOf(2, 3, 1, 5).toIntArray()

    println(solution(a))
}

fun solution(A: IntArray): Int {
    // write your code in Kotlin
    val map = mutableMapOf<Int, Int>()
    for (i in A) map[i] = 1
    for (i in 1..A.size + 1) if (map[i] == null) return i
    return 0
}