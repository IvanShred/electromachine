package ru.shred.electromachine.view;

import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginUi extends VerticalLayout {

    private LoginOverlay login = new LoginOverlay();

    public LoginUi() {
        login.setAction("login"); //
        login.setOpened(true); //
        login.setTitle("Spring Secured Vaadin");
        login.setDescription("Login Overlay Example");
        getElement().appendChild(login.getElement());
    }
}
