package ru.shred.electromachine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "otp.keycloak")
public class KeycloakOtpProperties {
    /** Public Keycloak base URL accessible from browser/app, e.g. http://localhost:8080 */
    private String baseUrl;
    /** Realm name, e.g. electromachine */
    private String realm;
    /** OAuth2 client id to initiate auth */
    private String clientId;
    /** Redirect URI registered in Keycloak for the client */
    private String redirectUri;
    /** Response type for init call, default: code */
    private String responseType = "code";
    /** Scope for init call, default: openid */
    private String scope = "openid";
    /** Base REST path of the custom Email OTP provider in Keycloak, default: /email-otp */
    private String providerPath = "/email-otp";

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getRedirectUri() { return redirectUri; }
    public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }

    public String getResponseType() { return responseType; }
    public void setResponseType(String responseType) { this.responseType = responseType; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getProviderPath() { return providerPath; }
    public void setProviderPath(String providerPath) { this.providerPath = providerPath; }
}
