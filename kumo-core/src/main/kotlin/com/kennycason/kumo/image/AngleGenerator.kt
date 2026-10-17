package com.kennycason.kumo.image

import java.util.Random

class AngleGenerator {

    private val steps: Int
    private val thetas: DoubleArray
    private var next: Int = 0

    constructor() {
        steps = 3
        thetas = calculateThetas(-90.0, 90.0)
    }

    constructor(degrees: Int) {
        steps = 1
        thetas = doubleArrayOf(Math.toRadians(degrees.toDouble()))
    }

    constructor(fromDegrees: Double, toDegrees: Double, steps: Int) {
        this.steps = steps
        thetas = calculateThetas(fromDegrees, toDegrees)
    }

    constructor(degrees: DoubleArray) {
        thetas = degrees
        steps = degrees.size
    }

    fun next(): Double = thetas[next++ % steps]

    fun randomNext(): Double = thetas[RANDOM.nextInt(steps)]

    private fun calculateThetas(to: Double, from: Double): DoubleArray {
        val stepSize = (to - from) / (steps - 1)
        return DoubleArray(steps) { i ->
            Math.toRadians(from + (i * stepSize))
        }
    }

    companion object {
        private val RANDOM = Random()
    }
}
