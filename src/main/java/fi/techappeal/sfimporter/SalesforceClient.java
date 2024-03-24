package fi.techappeal.sfimporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Date;
import java.util.List;

@Service
public class SalesforceClient {
    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;
    private final String contactPath;

    public SalesforceClient(
            @Value("${salesforce.client.id}") String clientId,
            @Value("${salesforce.client.secret}") String clientSecret,
            @Value("${salesforce.base.url}") String baseUrl,
            @Value("${salesforce.api.contact.path}") String contactPath) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restClient = RestClient.create(baseUrl);
        this.contactPath = contactPath;
    }

    public String login() {
        Login salesforceLogin = restClient.post()
                .uri("/services/oauth2/token")
                .body("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .toEntity(Login.class).getBody();
        return salesforceLogin.access_token();
    }

    public ChangedObjects getChangedContacts(String accessToken) {
        return restClient.get()
                .uri(contactPath+"/updated/?start=2024-03-20T00:00:00Z&end=2024-03-24T00:00:00Z")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(ChangedObjects.class).getBody();
    }

    public String getContact(String accessToken, String id) {
        return restClient.get()
                .uri(contactPath + "/" + id)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class).getBody();
    }

    public record ChangedObjects(
            List<String> ids,
            Date latestDateCovered
    ) { }
    private record Login(
            String access_token,
            String signature,
            String instance_url,
            String id,
            String token_type,
            String issued_at,
            String scope,
            String refresh_token) { }
}
