package dap.feature_store.user_metadata.functions;

import java.util.List;

public class SlackMessage {
    private String username;
    private List<Attachment> attachments;

    public SlackMessage(String username, List<Attachment> attachments) {
        this.username = username;
        this.attachments = attachments;
    }

    static class Attachment {
        private String color;
        private List<Field> fields;

        public Attachment(String color, List<Field> fields) {
            this.color = color;
            this.fields = fields;
        }
    }

    static class Field {
        private String title;
        private String value;
        private Boolean isShort;

        public Field(String title, String value, Boolean isShort) {
            this.title = title;
            this.value = value;
            this.isShort = isShort;
        }
    }
}
