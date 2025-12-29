package ru.shred.electromachine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shred.electromachine.config.KeycloakOtpProperties;
import ru.shred.electromachine.service.OtpProxyService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpProxyController {

    private final OtpProxyService service;
    private final KeycloakOtpProperties props;

    public OtpProxyController(OtpProxyService service, KeycloakOtpProperties props) {
        this.service = service;
        this.props = props;
    }

    @PostMapping("/init")
    public ResponseEntity<Map<String, String>> init() {
        var session = service.initAuthSession();
        Map<String, String> resp = new HashMap<>();
        resp.put("sessionId", session.sessionId());
        resp.put("tabId", session.tabId());
        if (session.loginActionUrl() != null) {
            resp.put("loginActionUrl", session.loginActionUrl());
        }
        return ResponseEntity.ok(resp);
    }

    public record SendRequest(String sessionId, String tabId, String email, Boolean forceCreate) {}

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(@RequestBody SendRequest body) {
        Map<String, Object> resp = service.sendOtp(body.sessionId(), body.tabId(), body.email(), body.forceCreate());
        return ResponseEntity.ok(resp);
    }

    public record VerifyRequest(String sessionId, String tabId, String email, String code) {}

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestBody VerifyRequest body) {
        var result = service.verifyOtp(body.sessionId(), body.tabId(), body.email(), body.code());
        Map<String, Object> resp = new HashMap<>();
        resp.put("verified", result.verified());
        if (result.error() != null) resp.put("error", result.error());
        if (result.verified()) {
            // Provide auth URL for the client to finish OAuth login in the same session
            String authUrl = props.getBaseUrl()
                    + "/realms/" + url(props.getRealm())
                    + "/protocol/openid-connect/auth?client_id=" + url(props.getClientId())
                    + "&redirect_uri=" + url(props.getRedirectUri())
                    + "&response_type=" + url(props.getResponseType())
                    + "&scope=" + url(props.getScope());
            resp.put("authUrl", authUrl);
        }
        return ResponseEntity.ok(resp);
    }

    private static String url(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
