package dap.feature_store.user_metadata.functions;

public class FieldBuilder {
    private String title;
    private String value;
    private Boolean isShort;

    public FieldBuilder title(String title) {
        this.title = title;
        return this;
    }

    public FieldBuilder value(String value) {
        this.value = value;
        return this;
    }

    public FieldBuilder isShort(Boolean isShort) {
        this.isShort = isShort;
        return this;
    }

    public SlackMessage.Field createField() {
        return new SlackMessage.Field(title, value, isShort);
    }
}