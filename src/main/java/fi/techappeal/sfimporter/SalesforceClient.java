package fi.techappeal.sfimporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SalesforceClient {
    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;

    private String accessToken;

    public SalesforceClient(
            @Value("${salesforce.client.id}") String clientId,
            @Value("${salesforce.client.secret}") String clientSecret,
            @Value("${salesforce.base.url}") String baseUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restClient = RestClient.create(baseUrl);
    }

    public void login() {
        Login salesforceLogin = restClient.post()
                .uri("/services/oauth2/token")
                .body("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .toEntity(Login.class).getBody();
        accessToken = salesforceLogin.access_token();
    }

    public String query() {
        return restClient.get()
                .uri("/services/data/v60.0/sobjects/Contact/updated/?start=2024-03-20T00:00:00Z&end=2024-03-24T00:00:00Z")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class).getBody();
    }
    private static record Login(
            String access_token,
            String signature,
            String instance_url,
            String id,
            String token_type,
            String issued_at,
            String scope,
            String refresh_token) { }
}
