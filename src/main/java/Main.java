import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;

public class Main {
    final static String[] SYMBOLS = {"a", "b", "c"};

    final static int CAPACITY = 100;
    final static int LENGTH = 100_000;
    final static int COUNT = 10_000;
    static int[] resultsCount = new int[SYMBOLS.length];
    static String[] resultsText = new String[SYMBOLS.length];

    public static void main(String[] args) throws InterruptedException {
        final HashMap<String, ArrayBlockingQueue<String>> queues = new HashMap<>();
        Arrays.stream(SYMBOLS).forEach(x -> queues.put(x, new ArrayBlockingQueue<>(CAPACITY)));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(SYMBOLS.length + 1);
        executorService.execute(new QueueFiller(queues, countDownLatch));

        for (int i = 0; i < SYMBOLS.length; i++) {
            executorService.execute(new SymbolCounter(i, SYMBOLS[i], queues));
        }
        countDownLatch.await();
        executorService.shutdownNow();
        for (int i = 0; i < SYMBOLS.length; i++) {
            System.out.println("Самое максимальное количество символов \""
                    + SYMBOLS[i] + "\": " + resultsCount[i] + " в строке \"" + resultsText[i] +"\"" );
        }
    }
}