package wordcloud;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * A LayeredWordCloud which can process each layer in its own Thread, thus
 * minimizing processing time.
 * 
 * @author &#64;wolfposd
 */
public class ParallelLayeredWordCloud extends LayeredWordCloud {

    private static final Logger LOGGER = Logger.getLogger(ParallelLayeredWordCloud.class);

    private List<Future<?>> executorFutures = new ArrayList<>();

    private ExecutorService executorservice;

    public ParallelLayeredWordCloud(int layers, int width, int height, CollisionMode collisionMode) {
        super(layers, width, height, collisionMode);

        executorservice = Executors.newFixedThreadPool(layers);
    }

    @Override
    public void build(final int layer, final List<WordFrequency> wordFrequencies) {
        Future<?> f = executorservice.submit(new Runnable() {
            public void run() {
                LOGGER.info("Starting to build WordCloud Layer " + layer + " in new Thread");
                ParallelLayeredWordCloud.super.build(layer, wordFrequencies);
            }
        });

        executorFutures.add(f);
    }

    @Override
    public void writeToFile(String outputFileName) {
        // we're not shutting down the executor as this would render it
        // non-functional afterwards. this way it can still be used
        LOGGER.info("Awaiting Termination of Executors");
        for (int i = 0; i < executorFutures.size(); i++) {
            Future<?> f = executorFutures.get(i);
            try {
                // cycle through all futures, invoking get() will block current
                // task until the future can return a result
                LOGGER.info("Performing get on Future:" + (i + 1) + "/" + executorFutures.size());
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorFutures.clear();
        LOGGER.info("Termination Complete, Processing to File now");

        super.writeToFile(outputFileName);
    }

    /**
     * Signals the ExecutorService to shutdown and await Termination. This is a blocking call.
     */
    public void shutdown() {
        executorservice.shutdown();
        try {
            executorservice.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOGGER.error("Executor awaitTermination was interrupted, consider manual termination", e);
        }
    }

}
