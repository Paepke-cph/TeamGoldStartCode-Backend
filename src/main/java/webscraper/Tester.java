package webscraper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Tester {


    public static List<TagCounter> runSequental() {
        List<TagCounter> urls = new ArrayList();
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://politiken.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));
        for (TagCounter tc : urls) {
            tc.doWork();
        }
        return urls;
    }

    public static List<TagCounter> runParallel() throws InterruptedException {
        List<TagCounter> urls = new ArrayList();
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://politiken.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));

        ExecutorService worker = Executors.newFixedThreadPool(4);
        urls.forEach(e -> {
            Runnable task = () -> {
              e.doWork();
            };
            worker.submit(task);
        });
        worker.shutdown();
        worker.awaitTermination(15, TimeUnit.SECONDS);
        return urls;
    }

    public static void main(String[] args) throws InterruptedException {
        long timeSequental;
        long start = System.nanoTime();

        List<TagCounter> fetchedData = new Tester().runSequental();
        long end = System.nanoTime();
        timeSequental = end - start;
        System.out.println("Time Sequential: " + ((timeSequental) / 1_000_000) + " ms.");

        for (TagCounter tc : fetchedData) {
            System.out.println("Title: " + tc.getTitle());
            System.out.println("Div's: " + tc.getDivCount());
            System.out.println("Body's: " + tc.getBodyCount());
            System.out.println("----------------------------------");
        }

        long timeParallel;
        start = System.nanoTime();
        Tester.runParallel();
        end = System.nanoTime();
        timeParallel = end - start;
        System.out.println("Time Parallel: "+((timeParallel)/100_000_000)+" ms.");
        System.out.println("Paralle was "+timeSequental/timeParallel + " times faster");
    }
}
