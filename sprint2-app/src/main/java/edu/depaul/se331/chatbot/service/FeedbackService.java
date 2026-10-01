package edu.depaul.se331.chatbot.service;

import edu.depaul.se331.chatbot.model.ChatFeedback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stores simple in-memory feedback about chatbot responses.
 *
 * This supports LLM evaluation by tracking whether users found
 * chatbot answers helpful or unhelpful.
 */
@Service
public class FeedbackService {

    private final List<ChatFeedback> feedbackList = new ArrayList<>();

    public ChatFeedback addFeedback(boolean helpful, String comment) {
        ChatFeedback feedback = new ChatFeedback(helpful, comment);
        feedbackList.add(feedback);
        return feedback;
    }

    public List<ChatFeedback> getAllFeedback() {
        return feedbackList;
    }

    public Map<String, Object> getSummary() {
        int helpfulCount = 0;
        int unhelpfulCount = 0;

        for (ChatFeedback feedback : feedbackList) {
            if (feedback.isHelpful()) {
                helpfulCount++;
            } else {
                unhelpfulCount++;
            }
        }

        double helpfulRate = feedbackList.isEmpty()
                ? 0.0
                : (double) helpfulCount / feedbackList.size();

        return Map.of(
                "totalFeedback", feedbackList.size(),
                "helpfulCount", helpfulCount,
                "unhelpfulCount", unhelpfulCount,
                "helpfulRate", helpfulRate
        );
    }
}