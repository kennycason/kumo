package com.kennycason.kumo

import com.kennycason.kumo.exception.KumoException
import java.awt.Dimension
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ParallelLayeredWordCloud(
    layers: Int,
    dimension: Dimension,
    collisionMode: CollisionMode
) : LayeredWordCloud(layers, dimension, collisionMode) {

    private val executorFutures = mutableListOf<Future<*>>()
    private val executorService = Executors.newFixedThreadPool(layers)

    override fun build(layer: Int, wordFrequencies: List<WordFrequency>) {
        val completionFuture = executorService.submit {
            logger.info { "Starting to build WordCloud Layer $layer in new Thread" }
            super.build(layer, wordFrequencies)
        }
        executorFutures.add(completionFuture)
    }

    override fun writeToFile(outputFileName: String) {
        writeToFile(outputFileName, blockThread = true, shutdownExecutor = true)
    }

    fun writeToFile(outputFileName: String, blockThread: Boolean, shutdownExecutor: Boolean = true) {
        if (blockThread) {
            waitForFuturesToBlockCurrentThread()
        }
        super.writeToFile(outputFileName)

        if (shutdownExecutor) {
            shutdown()
        }
    }

    private fun waitForFuturesToBlockCurrentThread() {
        logger.info { "Awaiting termination of executors" }
        for (i in executorFutures.indices) {
            val future = executorFutures[i]
            try {
                logger.info { "Performing get on Future: ${i + 1}/${executorFutures.size}" }
                future.get()
            } catch (e: InterruptedException) {
                throw KumoException("Error while waiting for Future of Layer $i", e)
            } catch (e: ExecutionException) {
                throw KumoException("Error while waiting for Future of Layer $i", e)
            }
        }
        executorFutures.clear()
        logger.info { "Termination complete, processing to file now" }
    }

    fun shutdown() {
        executorService.shutdown()
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            throw KumoException("Executor awaitTermination was interrupted, consider manual termination", e)
        }
    }
}
