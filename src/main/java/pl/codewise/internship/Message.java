package pl.codewise.internship;

public class Message {

    private final String userAgent;
    private final int errorCode;

    public Message(String userAgent, int errorCode) {
        this.userAgent = userAgent;
        this.errorCode = errorCode;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
