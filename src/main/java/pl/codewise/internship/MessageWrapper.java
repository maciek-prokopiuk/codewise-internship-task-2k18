package pl.codewise.internship;

import java.time.LocalDateTime;

public final class MessageWrapper{
    private Message message;
    private LocalDateTime timestamp;

    protected MessageWrapper(Message message){
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    protected Message getMessage(){
        return message;
    }

    protected LocalDateTime getTimestamp(){
        return timestamp;
    }
}