package pl.codewise.internship;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public final class MessageBuffer{
    private final MessageWrapper[] ringBuffer;
    private int writePosition = 0;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    protected MessageBuffer(){
        this.ringBuffer = new MessageWrapper[100];
    }

    protected void write(MessageWrapper messageWrapper){
        lock.writeLock().lock();
        try {
            ringBuffer[writePosition] = messageWrapper;
            incrementWritePosition();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void incrementWritePosition(){
        writePosition = (writePosition + 1) % ringBuffer.length;
    }

    protected List<MessageWrapper> getWrappedMessages(){
        lock.readLock().lock();
        try {
            return Arrays.stream(ringBuffer).filter(Objects::nonNull).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
}
