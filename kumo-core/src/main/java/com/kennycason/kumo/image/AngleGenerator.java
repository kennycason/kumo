package com.kennycason.kumo.image;

import java.util.Random;

/**
 * Created by kenny on 7/3/14.
 */
public class AngleGenerator {
    private static final Random RANDOM = new Random();

    private final int steps;

    private final double[] thetas;

    private int next;

    public AngleGenerator() {
        steps = 3;
        thetas = calculateThetas(-90, 90);
    }

    public AngleGenerator(final int degrees) {
        this.steps = 1;
        this.thetas = new double[] { Math.toRadians(degrees) };
    }

    public AngleGenerator(final double fromDegrees, final double toDegrees, final int steps) {
        this.steps = steps;
        thetas = calculateThetas(fromDegrees, toDegrees);
    }
    
    public AngleGenerator(final double[] degrees) {
        thetas = degrees;
        steps = degrees.length;
    }

    public double next() {
        return thetas[next++ % steps];
    }

    public double randomNext() {
        return thetas[RANDOM.nextInt(steps)];
    }

    private double[] calculateThetas(final double to, final double from) {
        final double stepSize = (to - from) / (steps - 1);
        final double[] thetas = new double[steps];
        for (int i = 0; i < steps; i++) {
            thetas[i] = Math.toRadians(from + (i * stepSize));
        }
        return thetas;
    }

}
