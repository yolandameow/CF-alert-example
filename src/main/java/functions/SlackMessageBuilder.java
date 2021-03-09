package dap.feature_store.user_metadata.functions;

import java.util.List;

public class SlackMessageBuilder {
    private String username;
    private List<SlackMessage.Attachment> attachments;

    public SlackMessageBuilder username(String username) {
        this.username = username;
        return this;
    }

    public SlackMessageBuilder attachments(List<SlackMessage.Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public SlackMessage createSlackMessage() {
        return new SlackMessage(username, attachments);
    }
}