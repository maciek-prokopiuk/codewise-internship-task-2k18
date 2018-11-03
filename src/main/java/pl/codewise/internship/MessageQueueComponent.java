package pl.codewise.internship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public final class MessageQueueComponent implements MessageQueue {
    private final MessageBuffer messageBuffer = new MessageBuffer();

    @Override
    public void add(Message message) {
        messageBuffer.write(new MessageWrapper(message));
    }

    @Override
    public Snapshot snapshot() {
        return new Snapshot(new ArrayList<>(getMessagesWithinLastFiveMinutes()));
    }

    @Override
    public long numberOfErrorMessages() {
        return getMessagesWithinLastFiveMinutes().stream()
                .filter(n -> isErrorCode(n.getErrorCode()))
                .count();
    }

    private List<Message> getMessagesWithinLastFiveMinutes(){
        return messageBuffer.getWrappedMessages().stream()
                .filter(n -> isNotOlderThanFiveMinutes(n.getTimestamp()))
                .map(MessageWrapper::getMessage)
                .collect(Collectors.toList());
    }

    private boolean isNotOlderThanFiveMinutes(LocalDateTime then){
        return LocalDateTime.now().isBefore(then.plusMinutes(5));
    }

    private boolean isErrorCode(int errorCode){
        return errorCode >= 400 && errorCode < 600;
    }

    private final class MessageBuffer{
        private final MessageWrapper[] ringBuffer;
        private int writePosition = 0;
        private ReadWriteLock lock = new ReentrantReadWriteLock();

        private MessageBuffer(){
            this.ringBuffer = new MessageWrapper[100];
        }

        private void write(MessageWrapper messageWrapper){
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

        private List<MessageWrapper> getWrappedMessages(){
            lock.readLock().lock();
            try {
                return Arrays.stream(ringBuffer).filter(Objects::nonNull).collect(Collectors.toList());
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    private final class MessageWrapper{
        private Message message;
        private LocalDateTime timestamp;

        private MessageWrapper(Message message){
            this.message = message;
            this.timestamp = LocalDateTime.now();
        }

        private Message getMessage(){
            return message;
        }

        private LocalDateTime getTimestamp(){
            return timestamp;
        }
    }
}
