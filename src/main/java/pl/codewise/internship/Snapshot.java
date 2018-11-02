package pl.codewise.internship;

import java.util.List;

public class Snapshot {

    private final List<Message> snapshot;

    public Snapshot(List<Message> snapshot) {
        this.snapshot = snapshot;
    }

    public List<Message> getSnapshot() {
        return snapshot;
    }
}

