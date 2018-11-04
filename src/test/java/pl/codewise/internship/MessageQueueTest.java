package pl.codewise.internship;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class MessageQueueTest {
    private static MessageQueueComponent messenger = new MessageQueueComponent();

    @BeforeClass
    public static void fillMessengerDeque() {
        for (int i = 0; i < 150; i++) {
            messenger.add(new Message("Test123", 0));
        }
    }

    @Test
    public void testAddingMessage() {
        Assert.assertEquals(100, messenger.snapshot().getSnapshot().size());
    }

    @Test
    public void testGetNumberOfErrors() {
        messenger.add(new Message("321tseT", 404));
        messenger.add(new Message("321tseT", 200));
        Assert.assertEquals(1, messenger.numberOfErrorMessages());
    }

    @Test
    public void testThreadAddMessage() {
        int threads = 10;
        MessageQueueComponent messageQueueComponentForThreads = new MessageQueueComponent();

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        IntStream.range(0, 99).forEach(n -> executorService.submit(() ->
                        messageQueueComponentForThreads.add(new Message("Threads", 200))));
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(99, messageQueueComponentForThreads.snapshot().getSnapshot().size());
    }
}

