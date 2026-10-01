package edu.depaul.se331.chatbot.model;
import java.time.LocalDateTime;

/**
 * Stores feedback submitted about chatbot responses.
 */
public class ChatFeedback {

    private final boolean helpful;
    private final String comment;
    private final LocalDateTime createdAt;

    public ChatFeedback(boolean helpful, String comment) {
        this.helpful = helpful;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isHelpful() {
        return helpful;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}