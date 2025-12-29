package ru.shred.electromachine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.shred.electromachine.config.KeycloakOtpProperties;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OtpProxyService {

    private static final Logger log = LoggerFactory.getLogger(OtpProxyService.class);

    private final KeycloakOtpProperties props;
    private final RestTemplate restTemplate = new RestTemplate();

    public OtpProxyService(KeycloakOtpProperties props) {
        this.props = props;
    }

    public SessionInfo initAuthSession() {
        try {
            String authUrl = props.getBaseUrl()
                    + "/realms/" + url(props.getRealm())
                    + "/protocol/openid-connect/auth?client_id=" + url(props.getClientId())
                    + "&redirect_uri=" + url(props.getRedirectUri())
                    + "&response_type=" + url(props.getResponseType())
                    + "&scope=" + url(props.getScope());

            log.debug("[OTP] Init KC session: {}", authUrl);

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            HttpClient client = HttpClient.newBuilder()
                    // управляем редиректами вручную, чтобы ловить Location и tab_id
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .cookieHandler(cookieManager)
                    .build();

            URI current = URI.create(authUrl);
            String tabId = null;
            int maxHops = 10;
            HttpResponse<String> last = null;

            for (int hop = 0; hop < maxHops; hop++) {
                HttpRequest req = HttpRequest.newBuilder().uri(current).GET().build();
                last = client.send(req, HttpResponse.BodyHandlers.ofString());
                int status = last.statusCode();
                // сначала пытаемся найти tab_id в Location
                String location = last.headers().firstValue("location").orElse(null);
                if (location != null) {
                    if (tabId == null) tabId = extractTabIdFromUrl(location).orElse(null);
                }
                if (status / 100 == 3 && location != null) {
                    current = URI.create(location);
                    continue;
                }
                // конечный ответ (обычно 200 страница логина) — пробуем достать tab_id из URL и/или HTML
                if (tabId == null) tabId = extractTabIdFromUrl(current.toString()).orElse(null);
                if (tabId == null && last.body() != null) {
                    tabId = extractTabIdFromHtml(last.body()).orElse(null);
                }
                break;
            }

            // Извлекаем AUTH_SESSION_ID (и возможное LEGACY имя)
            String sessionId = null;
            Map<String, List<HttpCookie>> cookieMap = cookieManager.getCookieStore().getCookies().stream()
                    .collect(HashMap::new, (m, c) -> m.computeIfAbsent(c.getName(), k -> new java.util.ArrayList<>()).add(c), Map::putAll);
            List<HttpCookie> authCookies = cookieMap.get("AUTH_SESSION_ID");
            if (authCookies == null || authCookies.isEmpty()) {
                authCookies = cookieMap.get("AUTH_SESSION_ID_LEGACY");
            }
            if (authCookies != null && !authCookies.isEmpty()) {
                sessionId = authCookies.get(0).getValue();
            }

            // Пытаемся извлечь loginActionUrl со страницы логина (если есть HTML тела)
            String loginActionUrl = null;
            if (last != null && last.body() != null) {
                loginActionUrl = extractLoginActionFromHtml(last.body(), props.getBaseUrl());
            }

            log.debug("[OTP] Extracted sessionId={}, tabId={}, loginActionUrl={}", sessionId, tabId, loginActionUrl);

            if (sessionId == null || tabId == null) {
                String bodySample = (last != null && last.body() != null) ? last.body().substring(0, Math.min(300, last.body().length())) : "<no-body>";
                throw new IllegalStateException("Unable to initialize Keycloak auth session: sessionId=" + sessionId + ", tabId=" + tabId + 
                        ". Check KC URL and flow. Body sample: " + bodySample);
            }

            return new SessionInfo(sessionId, tabId, loginActionUrl);
        } catch (Exception e) {
            throw new RuntimeException("OTP init failed: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> sendOtp(String sessionId, String tabId, String email, Boolean forceCreate) {
        if (sessionId == null || tabId == null) {
            throw new IllegalArgumentException("Keycloak session is not initialized (sessionId/tabId is null)");
        }
        String providerPath = normalizePath(props.getProviderPath());
        String url = props.getBaseUrl() + "/realms/" + props.getRealm()
                + providerPath + "/" + sessionId + "/" + tabId + "/send";
        log.debug("[OTP] SEND url={} email={}", url, email);
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        if (forceCreate != null) body.put("forceCreate", forceCreate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(url, new HttpEntity<>(body, headers), Map.class);
    }

    public VerifyResponse verifyOtp(String sessionId, String tabId, String email, String code) {
        if (sessionId == null || tabId == null) {
            throw new IllegalArgumentException("Keycloak session is not initialized (sessionId/tabId is null)");
        }
        String providerPath = normalizePath(props.getProviderPath());
        String url = props.getBaseUrl() + "/realms/" + props.getRealm()
                + providerPath + "/" + sessionId + "/" + tabId + "/verify";
        log.debug("[OTP] VERIFY url={} email={} code={}***", url, email, (code != null && code.length() > 0 ? code.charAt(0) : '?'));
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> resp = restTemplate.postForObject(url, new HttpEntity<>(body, headers), Map.class);
        boolean verified = false;
        String error = null;
        if (resp != null) {
            Object v = resp.get("verified");
            if (v instanceof Boolean b) verified = b;
            Object e = resp.get("error");
            if (e instanceof String s) error = s;
        }
        return new VerifyResponse(verified, error);
    }

    private static String url(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }

    public record SessionInfo(String sessionId, String tabId, String loginActionUrl) {}

    public record VerifyResponse(boolean verified, String error) {}

    private static Optional<String> extractTabIdFromUrl(String url) {
        try {
            URI u = URI.create(url);
            String q = u.getQuery();
            if (q == null) return Optional.empty();
            for (String p : q.split("&")) {
                int i = p.indexOf('=');
                if (i > 0) {
                    String k = p.substring(0, i);
                    String v = p.substring(i + 1);
                    if ("tab_id".equals(k)) return Optional.of(v);
                }
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    private static Optional<String> extractTabIdFromHtml(String html) {
        // Ищем tab_id в form action или ссылках
        try {
            Pattern p = Pattern.compile("tab_id=([A-Za-z0-9\\-_.]+)");
            Matcher m = p.matcher(html);
            if (m.find()) {
                return Optional.ofNullable(m.group(1));
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    /**
     * Извлекает абсолютный URL login-actions/authenticate из HTML формы Keycloak (kc-form-login).
     */
    private static String extractLoginActionFromHtml(String html, String baseUrl) {
        try {
            // Ищем форму с id="kc-form-login" и её action
            Pattern formPattern = Pattern.compile("<form[^>]*id=\\\"kc-form-login\\\"[^>]*action=\\\"([^\\\"]+)\\\"", Pattern.CASE_INSENSITIVE);
            Matcher fm = formPattern.matcher(html);
            if (fm.find()) {
                String action = fm.group(1);
                // может быть относительным путём
                if (action.startsWith("http://") || action.startsWith("https://")) {
                    return action;
                }
                // гарантируем ведущий слэш
                String path = action.startsWith("/") ? action : "/" + action;
                // baseUrl уже без завершающего слэша
                return (baseUrl != null ? baseUrl.replaceAll("/$", "") : "") + path;
            }
            // Альтернатива: искать explicit login-actions/authenticate ссылку
            Pattern alt = Pattern.compile("action=\\\"([^\\\"]*login-actions/authenticate[^\\\"]*)\\\"", Pattern.CASE_INSENSITIVE);
            Matcher am = alt.matcher(html);
            if (am.find()) {
                String action = am.group(1);
                if (action.startsWith("http://") || action.startsWith("https://")) {
                    return action;
                }
                String path = action.startsWith("/") ? action : "/" + action;
                return (baseUrl != null ? baseUrl.replaceAll("/$", "") : "") + path;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static String normalizePath(String path) {
        if (path == null || path.isBlank()) return "/email-otp";
        if (!path.startsWith("/")) return "/" + path;
        return path;
    }
}
