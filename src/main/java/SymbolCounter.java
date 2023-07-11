import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class SymbolCounter implements Runnable {
    private int index;
    private String symbol;
    private HashMap<String, BlockingQueue<String>> queues;
    public SymbolCounter(int index, String symbol, HashMap queues) {
        this.index = index;
        this.symbol = symbol;
        this.queues = queues;
    }
    @Override
    public void run() {
        for (int i = 0; i < Main.LENGTH; i++) {
            AtomicInteger currentResult = new AtomicInteger(0);
            try {
                String sample = queues.get(symbol).take();
                Stream<Character> charStream = sample.chars().mapToObj(c -> (char) c);
                charStream.filter(c -> symbol.equals(c.toString())).forEach(c -> currentResult.getAndIncrement());
                if (currentResult.get() > Main.resultsCount[index]) {
                    Main.resultsCount[index] = currentResult.get();
                    Main.resultsText[index] = sample;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
