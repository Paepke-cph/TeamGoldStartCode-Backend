package webscraper;

import concurrent.ParallelWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WebScraper {
    public static List<TagCounter> runGeneric() {
        List<TagCounter> urls = new ArrayList();
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://politiken.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));

        ParallelWorker<TagCounter> pw = new ParallelWorker<>();
        return pw.getAsParallel(urls);
    }

    public static List<TagCounter> runParallel() throws InterruptedException {
        List<TagCounter> urls = new ArrayList();
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://politiken.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));

        ExecutorService worker = Executors.newFixedThreadPool(4);
        urls.forEach(tagCounter -> {
            Runnable task = () -> {
              tagCounter.doWork();
            };
            worker.submit(task);
        });
        worker.shutdown();
        worker.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        return urls;
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        List<TagCounter> list1 = WebScraper.runParallel();
        long half = System.nanoTime();
        List<TagCounter> list2 = WebScraper.runGeneric();
        long end = System.nanoTime();

        long firstRun = TimeUnit.NANOSECONDS.toMillis(half - start);
        long lastRun = TimeUnit.NANOSECONDS.toMillis(end - half);
        System.out.println("Base Parallel: " + firstRun + "ms");
        System.out.println("Generic Parallel: " + lastRun + "ms");
        System.out.println("How come the Generic version is 4x times faster than the regular parallel function?????");
    }
}
