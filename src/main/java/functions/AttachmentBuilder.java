package dap.feature_store.user_metadata.functions;

import java.util.List;

public class AttachmentBuilder {
    private String color;
    private List<SlackMessage.Field> fields;

    public AttachmentBuilder color(String color) {
        this.color = color;
        return this;
    }

    public AttachmentBuilder fields(List<SlackMessage.Field> fields) {
        this.fields = fields;
        return this;
    }

    public SlackMessage.Attachment createAttachment() {
        return new SlackMessage.Attachment(color, fields);
    }
}