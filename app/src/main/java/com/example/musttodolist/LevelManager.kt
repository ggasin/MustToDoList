package com.example.musttodolist

class LevelManager {
    var level: Int = 1
        private set
    var currentGauge: Int = 0
        private set
    val maxGauge: Int = 100

    fun increaseGauge(amount: Int) {
        currentGauge += amount
    }

    fun levelUp() {
        level++
        currentGauge = 0
        println("Level up! You are now at level $level.")
    }
}