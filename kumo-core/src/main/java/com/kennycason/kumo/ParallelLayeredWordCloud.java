package com.kennycason.kumo;

import com.kennycason.kumo.exception.KumoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * A LayeredWordCloud which can process each layer in its own Thread, thus
 * minimizing processing time.
 * 
 * @author &#64;wolfposd
 */
public class ParallelLayeredWordCloud extends LayeredWordCloud {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelLayeredWordCloud.class);

    private final List<Future<?>> executorFutures = new ArrayList<>();

    private final ExecutorService executorservice;

    public ParallelLayeredWordCloud(final int layers, final Dimension dimension, final CollisionMode collisionMode) {
        super(layers, dimension, collisionMode);
        executorservice = Executors.newFixedThreadPool(layers);
    }

    /**
     * constructs the wordcloud specified by layer using the given
     * wordfrequencies.<br>
     * This is a non-blocking call.
     * 
     * @param layer
     *            Wordcloud Layer
     * @param wordFrequencies
     *            the WordFrequencies to use as input
     */
    @Override
    public void build(final int layer, final List<WordFrequency> wordFrequencies) {
        final Future<?> completionFuture = executorservice.submit(() -> {
            LOGGER.info("Starting to build WordCloud Layer {} in new Thread", layer);
            super.build(layer, wordFrequencies);
        });

        executorFutures.add(completionFuture);
    }

    /**
     * Writes the wordcloud to an imagefile.<br>
     * This is a blocking call.
     * 
     * @param outputFileName
     *            some file like "test.png"
     */
    @Override
    public void writeToFile(final String outputFileName) {
        writeToFile(outputFileName, true, true);
    }

    /**
     * Writes the wordcloud to an imagefile.<br>
     * Terminates the executor afterwards. See
     * {@link #writeToFile(String, boolean, boolean)} for a non-terminating call
     * 
     * @param outputFileName
     *            some file like "test.png"
     * @param blockThread
     *            should the current thread be blocked
     */
    public void writeToFile(final String outputFileName, final boolean blockThread) {
        this.writeToFile(outputFileName, blockThread, true);
    }

    /**
     * Writes the wordcloud to an imagefile.
     * 
     * @param outputFileName
     *            some file like "test.png"
     * @param blockThread
     *            should the current thread be blocked
     * @param shutdownExecutor
     *            should the executor be shutdown afterwards. if
     *            <code>false</code> this PLWC can still be used to build other
     *            layers. if <code>true</code> this will become a blocking
     *            Thread no matter what was specified in blockThread.
     */
    public void writeToFile(final String outputFileName, final boolean blockThread, final boolean shutdownExecutor) {
        if (blockThread) {
            waitForFuturesToBlockCurrentThread();
        }
        super.writeToFile(outputFileName);

        if (shutdownExecutor) {
            this.shutdown();
        }
    }

    private void waitForFuturesToBlockCurrentThread() {
        // we're not shutting down the executor as this would render it
        // non-functional afterwards. this way it can still be used if we
        // plan on building another layer on top of the previous ones
        LOGGER.info("Awaiting Termination of Executors");
        for (int i = 0; i < executorFutures.size(); i++) {
            final Future<?> future = executorFutures.get(i);
            try {
                // cycle through all futures, invoking get() will block
                // current task until the future can return a result
                LOGGER.info("Performing get on Future:" + (i + 1) + "/" + executorFutures.size());
                future.get();

            } catch (InterruptedException | ExecutionException e) {
                throw new KumoException("Error while waiting for Future of Layer " + i, e);
            }
        }
        executorFutures.clear();
        LOGGER.info("Termination Complete, Processing to File now");
    }

    /**
     * Signals the ExecutorService to shutdown and await Termination.<br>
     * This is a blocking call.
     */
    public void shutdown() {
        executorservice.shutdown();
        try {
            executorservice.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new KumoException("Executor awaitTermination was interrupted, consider manual termination", e);
        }
    }

}
