package com.kennycason.kumo

import com.kennycason.kumo.exception.KumoException
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * A LayeredWordCloud which can process each layer in its own Thread, thus
 * minimizing processing time.
 */
class ParallelLayeredWordCloud(
    layers: Int,
    dimension: Dimension,
    collisionMode: CollisionMode
) : LayeredWordCloud(layers, dimension, collisionMode) {

    private val executorFutures = mutableListOf<Future<*>>()
    private val executorService = Executors.newFixedThreadPool(layers)

    /**
     * Constructs the wordcloud specified by layer using the given word frequencies.
     * This is a non-blocking call.
     */
    override fun build(layer: Int, wordFrequencies: List<WordFrequency>) {
        val completionFuture = executorService.submit {
            LOGGER.info("Starting to build WordCloud Layer {} in new Thread", layer)
            super.build(layer, wordFrequencies)
        }
        executorFutures.add(completionFuture)
    }

    /**
     * Writes the wordcloud to an image file.
     * This is a blocking call.
     */
    override fun writeToFile(outputFileName: String) {
        writeToFile(outputFileName, blockThread = true, shutdownExecutor = true)
    }

    /**
     * Writes the wordcloud to an image file.
     * Terminates the executor afterwards.
     */
    fun writeToFile(outputFileName: String, blockThread: Boolean) {
        writeToFile(outputFileName, blockThread, shutdownExecutor = true)
    }

    /**
     * Writes the wordcloud to an image file.
     *
     * @param outputFileName some file like "test.png"
     * @param blockThread should the current thread be blocked
     * @param shutdownExecutor should the executor be shutdown afterwards
     */
    fun writeToFile(outputFileName: String, blockThread: Boolean, shutdownExecutor: Boolean) {
        if (blockThread) {
            waitForFuturesToBlockCurrentThread()
        }
        super.writeToFile(outputFileName)

        if (shutdownExecutor) {
            shutdown()
        }
    }

    private fun waitForFuturesToBlockCurrentThread() {
        LOGGER.info("Awaiting Termination of Executors")
        for (i in executorFutures.indices) {
            val future = executorFutures[i]
            try {
                LOGGER.info("Performing get on Future:${i + 1}/${executorFutures.size}")
                future.get()
            } catch (e: InterruptedException) {
                throw KumoException("Error while waiting for Future of Layer $i", e)
            } catch (e: ExecutionException) {
                throw KumoException("Error while waiting for Future of Layer $i", e)
            }
        }
        executorFutures.clear()
        LOGGER.info("Termination Complete, Processing to File now")
    }

    /**
     * Signals the ExecutorService to shutdown and await Termination.
     * This is a blocking call.
     */
    fun shutdown() {
        executorService.shutdown()
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            throw KumoException("Executor awaitTermination was interrupted, consider manual termination", e)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ParallelLayeredWordCloud::class.java)
    }
}
