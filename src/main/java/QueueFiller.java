import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class QueueFiller implements Runnable {
    private HashMap<String, ArrayBlockingQueue<String>> queues;
    private final CountDownLatch countDownLatch;

    public QueueFiller(HashMap queues, CountDownLatch countDownLatch) {
        this.queues = queues;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        for (int i = 0; i < Main.COUNT; i++) {
            for (String c : queues.keySet()) {
                String text = StringGenerator.generateText(String.join("", Main.SYMBOLS), Main.LENGTH);
                try {
                    /*ArrayBlockingQueue tempQueue = queues.get(c);
                    tempQueue.put(text);*/
                    queues.get(c).put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        countDownLatch.countDown();
    }
}
