package com.kennycason.kumo

import java.awt.Dimension
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.util.Random
import javax.imageio.ImageIO
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * @author joerg1985
 */
class SpiralTest {

    @Test
    fun maxRadius() {
        // draw the spiral as image?
        // red pixels -> only returned by the old implementation
        // blue pixels -> only returned by the new implementation
        // pink pixels -> returned by the old and the new implementation
        val debug = false

        val white = 0
        val red = 0xFFFF0000.toInt()
        val blue = 0xFF0000FF.toInt()
        val pink = red or blue

        // we seed to get the same numbers on each run
        val random = Random(42)

        for (i in 0 until 20) {
            val dimension = Dimension(
                100 + random.nextInt(900),
                100 + random.nextInt(900)
            )

            for (j in 0 until 20) {
                val start = Point(
                    random.nextInt(dimension.width),
                    random.nextInt(dimension.height)
                )

                val original = originalSpiral(
                    dimension, start, dimension.width
                )
                val optimized = originalSpiral(
                    dimension, start, WordCloud.computeRadius(dimension, start)
                )

                val img = BufferedImage(
                    dimension.width, dimension.height, BufferedImage.TYPE_4BYTE_ABGR
                )

                original.forEach { p -> img.setRGB(p.x, p.y, red) }
                optimized.forEach { p ->
                    if (img.getRGB(p.x, p.y) != 0) {
                        img.setRGB(p.x, p.y, pink)
                    } else {
                        img.setRGB(p.x, p.y, blue)
                    }
                }

                var next = false

                for (y in 0 until dimension.height) {
                    if (next) break
                    for (x in 0 until dimension.width) {
                        if (next) break
                        val rgb = img.getRGB(x, y)

                        if (rgb == red) {
                            ImageIO.write(img, "png", File("output/failed_spiral_test.png"))
                            throw AssertionError("Optimized spiral missed an original point")
                        } else if (debug && rgb != white && rgb != pink) {
                            ImageIO.write(img, "png", File("output/debug_spiral_test_${System.currentTimeMillis()}.png"))
                            next = true
                        }
                    }
                }
            }
        }
    }

    @Test
    fun noIdenticalPoints() {
        // we seed to get the same numbers on each run
        val random = Random(42)

        for (i in 0 until 20) {
            val dimension = Dimension(
                100 + random.nextInt(900),
                100 + random.nextInt(900)
            )

            for (j in 0 until 20) {
                val start = Point(
                    random.nextInt(dimension.width),
                    random.nextInt(dimension.height)
                )

                val points = optimizedSpiral(dimension, start)

                expectThat(points).get("number of unique points") { distinct().size }.isEqualTo(points.size)
            }
        }
    }

    private fun originalSpiral(dimension: Dimension, start: Point, maxRadius: Int): List<Point> {
        val points = mutableListOf<Point>()
        val position = Point()

        var r = 0
        while (r < maxRadius) {
            for (x in -r..r) {
                if (start.x + x < 0) { r += 2; continue }
                if (start.x + x >= dimension.width) { continue }

                position.x = start.x + x

                // try positive root
                val y1 = Math.sqrt((r * r - x * x).toDouble()).toInt()
                if (start.y + y1 >= 0 && start.y + y1 < dimension.height) {
                    position.y = start.y + y1
                    points.add(Point(position))
                }
                // try negative root
                val y2 = -y1
                if (start.y + y2 >= 0 && start.y + y2 < dimension.height) {
                    position.y = start.y + y2
                    points.add(Point(position))
                }
            }
            r += 2
        }

        return points
    }

    private fun optimizedSpiral(dimension: Dimension, start: Point): List<Point> {
        val maxRadius = WordCloud.computeRadius(dimension, start)

        val points = mutableListOf<Point>()
        val position = Point()

        var r = 0
        while (r < maxRadius) {
            for (x in Math.max(-start.x, -r)..Math.min(r, dimension.width - start.x - 1)) {
                position.x = start.x + x

                val offset = Math.sqrt((r * r - x * x).toDouble()).toInt()

                // try positive root
                position.y = start.y + offset
                if (position.y >= 0 && position.y < dimension.height) {
                    points.add(Point(position))
                }

                // try negative root (if offset != 0)
                position.y = start.y - offset
                if (offset != 0 && position.y >= 0 && position.y < dimension.height) {
                    points.add(Point(position))
                }
            }
            r += 2
        }

        return points
    }
}
