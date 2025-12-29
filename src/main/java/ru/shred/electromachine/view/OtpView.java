package ru.shred.electromachine.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import ru.shred.electromachine.config.KeycloakOtpProperties;
import ru.shred.electromachine.service.OtpProxyService;

@Route("otp")
@AnonymousAllowed
public class OtpView extends VerticalLayout {

    private final OtpProxyService otpService;
    private final KeycloakOtpProperties props;

    private String sessionId;
    private String tabId;
    private String loginActionUrl; // специфический URL KC для продолжения аутентификации в той же сессии

    public OtpView(OtpProxyService otpService, KeycloakOtpProperties props) {
        this.otpService = otpService;
        this.props = props;

        setWidthFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(new H2("Вход по email OTP"));

        EmailField email = new EmailField("Email");
        email.setClearButtonVisible(true);
        email.setRequired(true);
        email.setWidth("360px");

        TextField code = new TextField("Код из письма");
        code.setMaxLength(6);
        code.setWidth("360px");

        Button send = new Button("Отправить код");
        send.addClickListener(e -> {
            try {
                send.setEnabled(false);
                if (sessionId == null || tabId == null) {
                    var s = otpService.initAuthSession();
                    sessionId = s.sessionId();
                    tabId = s.tabId();
                    loginActionUrl = s.loginActionUrl();
                }
                var resp = otpService.sendOtp(sessionId, tabId, email.getValue(), Boolean.TRUE);
                Object ok = resp != null ? resp.get("sent") : null;
                if (Boolean.TRUE.equals(ok)) {
                    Notification.show("Код отправлен на " + email.getValue(), 3000, Notification.Position.MIDDLE);
                } else {
                    Notification.show("Не удалось отправить код", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("Ошибка: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            } finally {
                send.setEnabled(true);
            }
        });

        Button verify = new Button("Подтвердить");
        verify.addClickListener(e -> {
            try {
                verify.setEnabled(false);
                if (sessionId == null || tabId == null) {
                    // Инициализируем сессию KC при первом использовании, если не отправляли код раньше
                    var s = otpService.initAuthSession();
                    sessionId = s.sessionId();
                    tabId = s.tabId();
                    loginActionUrl = s.loginActionUrl();
                }
                if (email.isEmpty()) {
                    Notification.show("Введите email", 2500, Notification.Position.MIDDLE);
                    return;
                }
                if (code.isEmpty()) {
                    Notification.show("Введите код из письма", 2500, Notification.Position.MIDDLE);
                    return;
                }
                var result = otpService.verifyOtp(sessionId, tabId, email.getValue(), code.getValue());
                if (result.verified()) {
                    Notification.show("Верно. Завершаем вход...", 2000, Notification.Position.MIDDLE);
                    // Если удалось извлечь loginActionUrl той же KC-сессии — отправляем POST без полей,
                    // чтобы продолжить тот же аутентификационный поток и завершить логин.
                    if (loginActionUrl != null && !loginActionUrl.isBlank()) {
                        submitEmptyPost(loginActionUrl);
                    } else {
                        // Фолбэк: обычный OIDC старт (может потребовать повторный ввод, если KC не увидит верификацию)
                        String authUrl = props.getBaseUrl()
                                + "/realms/" + url(props.getRealm())
                                + "/protocol/openid-connect/auth?client_id=" + url(props.getClientId())
                                + "&redirect_uri=" + url(props.getRedirectUri())
                                + "&response_type=" + url(props.getResponseType())
                                + "&scope=" + url(props.getScope());
                        UI.getCurrent().getPage().setLocation(authUrl);
                    }
                } else {
                    Notification.show("Неверный код" + (result.error() != null ? ": " + result.error() : ""), 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (ex.getCause() != null && (msg == null || msg.isBlank())) msg = ex.getCause().getMessage();
                Notification.show("Ошибка: " + (msg != null ? msg : ex.toString()), 4000, Notification.Position.MIDDLE);
            } finally {
                verify.setEnabled(true);
            }
        });

        Div spacer = new Div();
        spacer.setHeight("12px");

        add(email, send, spacer, code, verify);
    }

    private static String url(String v) {
        return java.net.URLEncoder.encode(v, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static void submitEmptyPost(String actionUrl) {
        // Создаём и сабмитим форму через JS, чтобы выполнить POST в тот же аутентификационный шаг KC
        UI.getCurrent().getPage().executeJs(
                "var f=document.createElement('form'); f.method='POST'; f.action=$0; document.body.appendChild(f); f.submit();",
                actionUrl
        );
    }
}
