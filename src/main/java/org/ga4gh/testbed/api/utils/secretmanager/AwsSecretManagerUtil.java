package org.ga4gh.testbed.api.utils.secretmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretResponse;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.UpdateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.UpdateSecretResponse;

import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;


public class AwsSecretManagerUtil {
    public static final String REGION = "us-east-1";

    public String createSecret(
            final String secretName,
            final String secretKey,
            final String secretValue,
            final String secretDescription){
        // creates a new AWS Secret Manager entry by name = secretName.
        // This entry will house a key value pair.
        SecretsManagerClient secretsClient= secretsClient();
        try {

            String secretKeyValue = String.format("{ \"%s\":\"%s\" }", secretKey, secretValue);
            CreateSecretRequest createSecretRequest = CreateSecretRequest.builder()
                    .name(secretName)
                    .description(secretDescription)
                    .secretString(secretKeyValue)
                    .build();
            CreateSecretResponse createSecretResponse = secretsClient.createSecret(createSecretRequest);
            secretsClient.close();
            return createSecretResponse.arn();
        } catch (Exception ex){
            secretsClient.close();
            throw ex;
        } finally {
            secretsClient.close();
        }
    }

    public HashMap<String, String> retrieveSecret(final String secretName) throws JsonProcessingException {
        // Retrieves an entry from AWS Secret Manager by name, secretName
        SecretsManagerClient secretsClient= secretsClient();
        try {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();
            GetSecretValueResponse getSecretValueResponse = null;
            getSecretValueResponse = secretsClient.getSecretValue(getSecretValueRequest);
            String secret = null;
            if (getSecretValueResponse.secretString() != null) {
                secret = getSecretValueResponse.secretString();
            } else if (getSecretValueResponse.secretBinary() != null) {
                secret = new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
            }
            secretsClient.close();
            // convert secret string to HashMap<String, String>
            final ObjectMapper objectMapper = new ObjectMapper();
            final HashMap<String, String> secretMap = objectMapper.readValue(secret, HashMap.class);
            return secretMap;
        } catch(Exception ex){
            secretsClient.close();
            throw ex;
        } finally {
            secretsClient.close();
        }
    }

    public String updateSecret(
            final String secretName,
            final String secretKey,
            final String secretValue,
            final String secretDescription) throws JsonProcessingException {
        // Updates an existing SM entry with an additional key vaule pair
        SecretsManagerClient secretsClient= secretsClient();
        try {

            // get existing secretMap
            HashMap<String, String> secretMap = retrieveSecret(secretName);

            // add the new key value pair to the secretMap
            secretMap.put(secretKey,secretValue);

            // convert the secretMap to a String and update the secret entry with this string
            String secretKeyValueString = secretMap.keySet().stream()
                    .map(key -> "\""+key+ "\":\"" + secretMap.get(key)+"\"")
                    .collect(Collectors.joining(", ", "{", "}"));
            UpdateSecretRequest updateSecretRequest = UpdateSecretRequest.builder()
                    .secretId(secretName)
                    .description(secretDescription)
                    .secretString(secretKeyValueString)
                    .build();
            UpdateSecretResponse updateSecretResponse = secretsClient.updateSecret(updateSecretRequest);
            secretsClient.close();
            return updateSecretResponse.arn();
        } catch (Exception ex){
            secretsClient.close();
            throw ex;
        } finally {
            secretsClient.close();
        }

    }

    private SecretsManagerClient secretsClient(){
        Region region = Region.of(REGION);
        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .build();
        return secretsClient;
    }

}