package pl.kamilbaranowski.chatappserver.model;

import java.util.Date;

public class Message {
    private String sender;
    private String receiver;
    private String messageContent;
    private Long timestamp;

    public Message(String sender, String receiver, String messageContent, Long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
