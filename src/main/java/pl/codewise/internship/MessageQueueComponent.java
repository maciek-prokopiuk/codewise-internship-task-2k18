package pl.codewise.internship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public final class MessageQueueComponent implements MessageQueue {
    private final LinkedBlockingDeque<MessageWrapper> mostRecentMessages = new LinkedBlockingDeque<>(100);

    @Override
    public synchronized void add(Message message) {
        if(mostRecentMessages.remainingCapacity() > 0){
            mostRecentMessages.offerFirst(new MessageWrapper(message));
        } else {
            mostRecentMessages.removeLast();
            mostRecentMessages.offerFirst(new MessageWrapper(message));
        }
    }

    @Override
    public Snapshot snapshot() {
        return new Snapshot(new ArrayList<>(getMessagesWithinLastFiveMinutes()));
    }

    @Override
    public long numberOfErrorMessages() {
        return getMessagesWithinLastFiveMinutes().stream()
                .filter(n -> n.getErrorCode() != 0)
                .count();
    }

    private List<Message> getMessagesWithinLastFiveMinutes(){
        return mostRecentMessages.stream()
                .filter(n -> isNotOlderThanFiveMinutes(n.getTimestamp()))
                .map(MessageWrapper::getMessage)
                .collect(Collectors.toList());
    }

    private boolean isNotOlderThanFiveMinutes(LocalDateTime then){
        return LocalDateTime.now().isBefore(then.plusMinutes(5));
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
