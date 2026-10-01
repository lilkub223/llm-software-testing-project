package edu.depaul.se331.chatbot.controller;

import edu.depaul.se331.chatbot.model.ChatMessage;
import edu.depaul.se331.chatbot.model.ChatRequest;
import edu.depaul.se331.chatbot.model.ChatResponse;
import edu.depaul.se331.chatbot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.depaul.se331.chatbot.model.FeedbackRequest;
import edu.depaul.se331.chatbot.service.FeedbackService;

import java.util.List;
import java.util.Map;

/**
 * ChatController — HTTP layer for the chatbot.
 *
 * Endpoints:
 *   POST   /api/chat          — send a message, get a reply
 *   DELETE /api/chat/history  — clear the conversation history
 *   GET    /api/chat/history  — (optional) inspect conversation history
 *
 * EXTEND: Add new endpoints here to expose additional features,
 * e.g. POST /api/summarize, POST /api/translate, etc.
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")   // allows the HTML page to call this API
public class ChatController {

    private final ChatService chatService;
    private final FeedbackService feedbackService;

    public ChatController(ChatService chatService, FeedbackService feedbackService) {
        this.chatService = chatService;
        this.feedbackService = feedbackService;
    }

    // ── Endpoints ─────────────────────────────────────────────

    /**
     * Send a message to the chatbot.
     *
     * Request body : { "message": "your text here" }
     * Response body: { "reply": "assistant reply here" }
     */
    
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest()
                .body(new ChatResponse("Please provide a non-empty message."));
    }

    String trimmedMessage = request.getMessage().trim();

    if (trimmedMessage.length() > 4000) {
        return ResponseEntity.badRequest()
                .body(new ChatResponse("Message cannot exceed 4000 characters."));
    }

    String reply = chatService.chat(trimmedMessage, request.getMode());
    return ResponseEntity.ok(new ChatResponse(reply));
}

@PostMapping("/summary")
public ResponseEntity<ChatResponse> summarize(@RequestBody ChatRequest request) {
    if (request.getMessage() == null || request.getMessage().isBlank()) {
        return ResponseEntity.badRequest()
                .body(new ChatResponse("Please provide text to summarize."));
    }

    String textToSummarize = request.getMessage().trim();

    if (textToSummarize.length() > 4000) {
        return ResponseEntity.badRequest()
                .body(new ChatResponse("Text cannot exceed 4000 characters."));
    }

    String summaryPrompt = "Summarize the following text clearly and concisely:\n\n"
            + textToSummarize;

    String reply = chatService.chat(summaryPrompt, "summary");

    return ResponseEntity.ok(new ChatResponse(reply));
}
    
    
    
    

    /**
     * Clear the in-memory conversation history.
     *
     * Useful when you want to start a completely new topic
     * without the model seeing previous turns.
     */
    @DeleteMapping("/history")
    public ResponseEntity<Map<String, String>> clearHistory() {
        chatService.resetHistory();
        return ResponseEntity.ok(Map.of("status", "Conversation history cleared."));
    }

    /**
     * Inspect the current conversation history (for debugging).
     *
     * EXTEND: Remove or restrict this endpoint before deploying
     * to a production environment.
     */
    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getHistory() {
        return ResponseEntity.ok(chatService.getHistory());
    }
    /**
 * Submit feedback about a chatbot response.
 *
 * This supports LLM evaluation by allowing users/testers to record
 * whether a response was helpful and optionally explain why.
 */
@PostMapping("/feedback")
public ResponseEntity<Map<String, String>> submitFeedback(@RequestBody FeedbackRequest request) {
    if (request.getHelpful() == null) {
        return ResponseEntity.badRequest()
                .body(Map.of("status", "Feedback must include whether the response was helpful."));
    }

    feedbackService.addFeedback(request.getHelpful(), request.getComment());

    return ResponseEntity.ok(Map.of("status", "Feedback was recorded."));
}

/**
 * Return a summary of chatbot response feedback.
 */
@GetMapping("/feedback/summary")
public ResponseEntity<Map<String, Object>> getFeedbackSummary() {
    return ResponseEntity.ok(feedbackService.getSummary());
}
   

    
}
