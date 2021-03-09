# deploy the cloud function
cloud functions deploy ${cloud_function_name} \
--entry-point functions.ValidationErrorFunction \
--set-env-vars PROJECT_ID=${projectId} \
--runtime java11 \
--memory 512MB \
--service-account ${service_account} \
--trigger-resource ${bucket_name} \
--trigger-event google.storage.object.finalize