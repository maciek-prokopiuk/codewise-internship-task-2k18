package pl.codewise.internship;

public interface MessageQueue {

    void add(Message message);

    Snapshot snapshot();

    long numberOfErrorMessages();
}
