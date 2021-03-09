package dap.feature_store.user_metadata.functions;

public enum GcsEventType {

    FINALIZE("google.storage.object.finalize"),
    DELETE("google.storage.object.delete"),
    ARCHIVE("google.storage.object.archive"),
    METADATA_UPDATE("google.storage.object.metadataUpdate");

    public final String eventString;

    GcsEventType(String event) {
        this.eventString = event;
    }

}
