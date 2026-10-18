package com.kennycason.kumo.image

import java.util.Random

class AngleGenerator {

    private val steps: Int
    private val thetas: DoubleArray
    private var next: Int = 0
    private val random = Random()

    constructor() {
        steps = 3
        thetas = calculateThetas(-90.0, 90.0)
    }

    constructor(degrees: Int) {
        steps = 1
        thetas = doubleArrayOf(Math.toRadians(degrees.toDouble()))
    }

    constructor(fromDegrees: Double, toDegrees: Double, steps: Int) {
        require(steps > 0) { "At least one angle is required" }
        require(fromDegrees.isFinite() && toDegrees.isFinite()) { "Angles must be finite" }
        this.steps = steps
        thetas = calculateThetas(fromDegrees, toDegrees)
    }

    constructor(degrees: DoubleArray) {
        require(degrees.isNotEmpty() && degrees.all { it.isFinite() }) { "At least one finite angle is required" }
        thetas = degrees.copyOf()
        steps = degrees.size
    }

    fun next(): Double = thetas[next++ % steps]

    fun randomNext(random: Random = this.random): Double = thetas[random.nextInt(steps)]

    private fun calculateThetas(from: Double, to: Double): DoubleArray {
        if (steps == 1) return doubleArrayOf(Math.toRadians(from))
        val stepSize = (to - from) / (steps - 1)
        return DoubleArray(steps) { i ->
            Math.toRadians(from + (i * stepSize))
        }
    }
}
