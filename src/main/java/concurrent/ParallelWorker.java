package concurrent;

import java.util.List;
import java.util.concurrent.*;

public class ParallelWorker<T extends ParallelTask> {
    public List<T> getAsParallel(List<T> elements) {
        ExecutorService worker = Executors.newFixedThreadPool(4);
        elements.forEach(element -> {
            Runnable task = element::doWork;
            worker.submit(task);
        });
        worker.shutdown();
        try {
            worker.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return elements;
    }
}
