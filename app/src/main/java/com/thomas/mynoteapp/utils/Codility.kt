package com.thomas.mynoteapp.utils

fun main() {
    println(solution(10, 40, 30))
}

fun solution(X: Int, Y: Int, D: Int): Int {
    // write your code in Kotlin
    val len = Y - X

    return if (len % D == 0) len / D else len / D + 1
}