package wordcloud.image;

import java.util.Random;

/**
 * Created by kenny on 7/3/14.
 */
public class AngleGenerator {

    private static final Random RANDOM = new Random();

    private final int steps;

    private final double[] thetas;

    private int next = 0;

    public AngleGenerator() {
        steps = 3;
        thetas = calculateThetas(-90, 90);
    }

    public AngleGenerator(int degrees) {
        this.steps = 1;
        this.thetas = new double[] { degreesToRadians(degrees) };
    }

    public AngleGenerator(double fromDegrees, double toDegrees, int steps) {
        this.steps = steps;
        thetas = calculateThetas(fromDegrees, toDegrees);
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
        for(int i = 0; i < steps; i++) {
            thetas[i] = degreesToRadians(from + (i * stepSize));
        }
        return thetas;
    }

    private double degreesToRadians(final double degrees) {
        return Math.PI * degrees / 180.0;
    }

}
