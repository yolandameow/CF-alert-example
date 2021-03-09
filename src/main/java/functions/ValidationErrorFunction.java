package dap.feature_store.user_metadata.functions;



import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidationErrorFunction implements BackgroundFunction<GcsEvent> {
    private static final Logger logger = Logger.getLogger(ValidationErrorFunction.class.getName());

    private final String PROJECT_ID_ENV = "PROJECT_ID";
    private final String SLACK_WEBHOOK_SECRET_NAME = "slack-alert-webhook";
    private final String SECRET_VERSION_LATEST = "latest";
    private final String SLACK_MESSAGE_COLOR = "#9733EE";
    private final String SLACK_MESSAGE_USERNAME = "alerts";
    private final String projectId;

    public ValidationErrorFunction() {
        this.projectId = System.getenv(PROJECT_ID_ENV);
        if (this.projectId == null) {
            String errMessage = String.format("Env var %s is not set", PROJECT_ID_ENV);
            logger.log(Level.SEVERE, errMessage);
        }
    }

    @Override
    public void accept(GcsEvent payload, Context context) throws Exception {

        if (context.eventType().equalsIgnoreCase(GcsEventType.FINALIZE.eventString)) {
            String bucketName = payload.getBucket();
            String fileName = payload.getName();

            Storage storage = StorageOptions.newBuilder().setProjectId(this.projectId).build().getService();
            Blob blob = storage.get(BlobId.of(bucketName, fileName));
            String fileContent = new String(blob.getContent());

            int error_count = fileContent.split("[\\r\\n]+").length;
            if (error_count > 0) {
                sendSlackMessage(error_count, bucketName, fileName);
            }
        }

    }

    private void sendSlackMessage(int error_count, String bucketName, String fileName) {

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(this.projectId, SLACK_WEBHOOK_SECRET_NAME, SECRET_VERSION_LATEST);
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            String webhookUrl = response.getPayload().getData().toStringUtf8();
            Slack slack = Slack.getInstance();
//            String jobName = fileName.substring(fileName.lastIndexOf("/") + 1);
            String jobName = bucketName+fileName;
            WebhookResponse webhookResponse = slack.send(webhookUrl, buildSlackData(jobName, bucketName, error_count, fileName));
            System.out.println("Getting webhook response "+webhookResponse.getCode()+" message:"+webhookResponse.getMessage());
            if (webhookResponse.getCode() != HttpStatus.SC_OK) {
                logger.log(Level.SEVERE, String.format("Getting response [%s] from slack webhook with message[%s]",
                        webhookResponse.getCode(), webhookResponse.getMessage()));

            }

        } catch (IOException e) {
            logger.log(Level.WARNING, "", e);
        }
    }

    public String buildSlackData(String jobName, String bucketName, int errorCount, String filePath) {

        SlackMessage.Field jobField = new FieldBuilder().title("Error records uploaded for job").value(jobName).isShort(false).createField();
        SlackMessage.Field errorCountField = new FieldBuilder().title("Number of errors").value(String.valueOf(errorCount)).isShort(false).createField();
        SlackMessage.Field fileField = new FieldBuilder().title("File").value(String.format("gs://%s/%s", bucketName, filePath)).isShort(false).createField();

        SlackMessage.Attachment attachment = new AttachmentBuilder().color(SLACK_MESSAGE_COLOR).fields(Arrays.asList(jobField, errorCountField, fileField)).createAttachment();

        SlackMessage slackMessage = new SlackMessageBuilder().attachments(Arrays.asList(attachment)).username(SLACK_MESSAGE_USERNAME).createSlackMessage();

        Gson gson = new Gson();
        return gson.toJson(slackMessage);


    }


}
