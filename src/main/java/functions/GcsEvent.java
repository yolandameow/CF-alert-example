package dap.feature_store.user_metadata.functions;

import java.util.Date;

public class GcsEvent {
    /**
     *   Cloud Functions uses GSON to populate this object.
     *   Field types/names are specified by Cloud Functions
     *   Changing them may break your code.
     */
    private String bucket;
    private String name;
    private String metageneration;
    private Date timeCreated;
    private Date updated;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetageneration() {
        return metageneration;
    }

    public void setMetageneration(String metageneration) {
        this.metageneration = metageneration;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}