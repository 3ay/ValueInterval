import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        List<Future> futureList = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(texts.length);
        for (String text : texts) {
            Future<Integer> future = threadPool.submit(new StringProcessor(text));
            futureList.add(future);
        }
        int maxInterval = -1;
        for (Future future : futureList) {
            Integer interval = (Integer) future.get();
            if (interval > maxInterval) {
                maxInterval = interval;
            }
        }
        threadPool.shutdown();
        System.out.println("Максимальный интервал значений среди всех строк: " + maxInterval);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}