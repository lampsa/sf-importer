package fi.techappeal.sfimporter;


record SalesforceLogin(String access_token, String signature, String instance_url, String id, String token_type, String issued_at, String scope, String refresh_token) {
}
