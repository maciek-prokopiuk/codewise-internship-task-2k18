package pl.codewise.internship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
}
