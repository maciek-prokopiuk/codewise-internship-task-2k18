package pl.codewise.internship;

import org.junit.Assert;
import org.junit.Test;

public class MessageQueueTest {

    private void fillMessengerDeque(MessageQueueComponent messenger){
        for(int i = 0; i < 150; i++) {
            messenger.add(new Message("Test123", 0));
        }
    }

    @Test
    public void testAddingMessage(){
        MessageQueueComponent messenger = new MessageQueueComponent();
        fillMessengerDeque(messenger);

        Assert.assertEquals(100, messenger.snapshot().getSnapshot().size());
    }

    @Test
    public void testGetNumberOfErrors(){
        MessageQueueComponent messenger = new MessageQueueComponent();
        fillMessengerDeque(messenger);

        messenger.add(new Message("321tseT", 34));
        Assert.assertEquals(1, messenger.numberOfErrorMessages());
    }
}

