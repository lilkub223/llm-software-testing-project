package edu.depaul.se331.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.depaul.se331.chatbot.model.ChatRequest;
import edu.depaul.se331.chatbot.model.FeedbackRequest;
import edu.depaul.se331.chatbot.service.ChatService;
import edu.depaul.se331.chatbot.service.FeedbackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @MockBean
    private FeedbackService feedbackService;

    @Test
    @DisplayName("POST /api/chat returns non-empty reply for valid message")
    void chatEndpointReturnsNonEmptyReply() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setMessage("Hello, chatbot!");
        request.setMode("default");

        when(chatService.chat("Hello, chatbot!", "default")).thenReturn("Hello there!");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(not(emptyOrNullString())));
    }

    @Test
    @DisplayName("POST /api/chat rejects messages longer than 4000 characters")
    void chatEndpointRejectsTooLongMessage() throws Exception {
        String longMessage = "x".repeat(4001);
        ChatRequest request = new ChatRequest();
        request.setMessage(longMessage);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reply").value(not(emptyOrNullString())));
    }

    @Test
    @DisplayName("POST /api/chat/feedback accepts helpful=true feedback")
    void feedbackEndpointAcceptsHelpfulTrue() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setHelpful(true);
        request.setComment("This answer was useful.");

        mockMvc.perform(post("/api/chat/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Feedback was recorded."));
    }

    @Test
    @DisplayName("POST /api/chat/feedback accepts helpful=false feedback")
    void feedbackEndpointAcceptsHelpfulFalse() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setHelpful(false);
        request.setComment("This answer was not what I needed.");

        mockMvc.perform(post("/api/chat/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Feedback was recorded."));
    }

    @Test
    @DisplayName("POST /api/chat returns non-empty replies for each supported chat mode")
    void chatEndpointSupportsAllModes() throws Exception {
        String[] modes = {"default", "java-tutor", "debug-helper", "scheduling-assistant"};

        for (String mode : modes) {
            ChatRequest request = new ChatRequest();
            request.setMessage("Tell me something useful.");
            request.setMode(mode);

            when(chatService.chat("Tell me something useful.", mode)).thenReturn("Reply for " + mode);

            mockMvc.perform(post("/api/chat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reply").value(not(emptyOrNullString())));
        }
    }

    @Test
    @DisplayName("POST /api/chat/summary returns a summary reply")
    void summaryEndpointReturnsNonEmptyReply() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setMessage("Please summarize this text.");

        when(chatService.chat(anyString(), eq("summary"))).thenReturn("A concise summary.");

        mockMvc.perform(post("/api/chat/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(not(emptyOrNullString())));
    }

    @Test
    @DisplayName("POST /api/chat rejects blank messages")
    void chatEndpointRejectsBlankMessage() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setMessage("   ");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reply").value(not(emptyOrNullString())));
    }

    @Test
    @DisplayName("POST /api/chat/summary rejects blank message")
    void summaryEndpointRejectsBlankMessage() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setMessage("   ");

        mockMvc.perform(post("/api/chat/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reply").value(not(emptyOrNullString())));
    }

    @Test
    @DisplayName("POST /api/chat/feedback rejects missing helpful flag")
    void feedbackEndpointRejectsMissingHelpful() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setComment("No helpful flag supplied.");

        mockMvc.perform(post("/api/chat/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Feedback must include whether the response was helpful."));
    }

    @Test
    @DisplayName("GET /api/chat/feedback/summary returns feedback statistics")
    void feedbackSummaryEndpointReturnsStatistics() throws Exception {
        when(feedbackService.getSummary()).thenReturn(Map.of(
                "totalFeedback", 2,
                "helpfulCount", 1,
                "unhelpfulCount", 1,
                "helpfulRate", 0.5
        ));

        mockMvc.perform(get("/api/chat/feedback/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFeedback").value(2))
                .andExpect(jsonPath("$.helpfulCount").value(1))
                .andExpect(jsonPath("$.unhelpfulCount").value(1))
                .andExpect(jsonPath("$.helpfulRate").value(0.5));
    }
}
