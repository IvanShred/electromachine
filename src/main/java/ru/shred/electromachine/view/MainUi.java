package ru.shred.electromachine.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;

@Route("")
public class MainUi extends VerticalLayout {

    public MainUi() {
        Button buttonAzur = new Button("Испытания Азур", VaadinIcon.CHECK.create());
        buttonAzur.getStyle().set("border-radius", "12px").set("background-color", "blue").set("color", "white");
        buttonAzur.addClickListener(event -> UI.getCurrent().getPage().setLocation("azur"));

        Button buttonAll = new Button("Кнопка для всех", VaadinIcon.CHECK.create());
        buttonAll.getStyle().set("border-radius", "12px").set("background-color", "blue").set("color", "white");

        Button buttonAdmin = new Button("Кнопка для админа", VaadinIcon.CLOSE_CIRCLE.create());
        buttonAdmin.getStyle().set("border-radius", "12px").set("background-color", "red").set("color", "white");

        add(buttonAll);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_ADMIN")) {
            add(buttonAzur, buttonAdmin);
        }

        // Получаем аутентифицированного пользователя
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof OidcUser) {
            OidcUser user = (OidcUser) authentication.getPrincipal();

            add(new H1("Welcome, " + user.getPreferredUsername()));
            add(new H1("Email: " + user.getEmail()));

            // Кнопка выхода
            Button logoutButton = new Button("Logout", e -> {
                UI.getCurrent().getPage().setLocation("/electromachine/logout");
            });
            add(logoutButton);
        } else {
            add(new H1("Please login"));
        }

        setAlignItems(Alignment.CENTER);
    }
}
