package edu.depaul.se331.chatbot.model;
public class FeedbackRequest {
    private Boolean helpful;
    private String comment;
    public FeedbackRequest() {
    }
    public Boolean getHelpful() {
        return helpful;
    }

    public void setHelpful(Boolean helpful) {
        this.helpful = helpful;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

  
}
