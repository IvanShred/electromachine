<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password'); section>
    <#if section == "header">
        ${msg("loginTitle")}
    <#elseif section == "form">

        <div class="kc-auth-container">

            <h2 class="kc-auth-title">
                ${msg("chooseAuthMethod")}
            </h2>

            <!-- ===================== -->
            <!-- ЛОГИН + ПАРОЛЬ -->
            <!-- ===================== -->
            <div class="kc-auth-method">
                <h3>${msg("passwordAuthTitle")}</h3>

                <form id="kc-form-login"
                      action="${url.loginAction}"
                      method="post">

                    <div class="kc-form-group">
                        <label for="username">${msg("username")}</label>
                        <input id="username"
                               name="username"
                               type="text"
                               autocomplete="username"
                               required />
                    </div>

                    <div class="kc-form-group">
                        <label for="password">${msg("password")}</label>
                        <input id="password"
                               name="password"
                               type="password"
                               autocomplete="current-password"
                               required />
                    </div>

                    <button type="submit" class="kc-button-primary">
                        ${msg("doLogIn")}
                    </button>
                </form>
            </div>

            <hr class="kc-divider"/>

            <!-- ===================== -->
            <!-- EMAIL OTP -->
            <!-- ===================== -->
            <div class="kc-auth-method">
                <h3>${msg("emailOtpTitle")}</h3>
                <p>${msg("emailOtpDescription")}</p>

                <form action="${url.loginAction}" method="post">
                    <!-- ВАЖНО: выбор execution во Flow -->
                    <input type="hidden"
                           name="authenticationExecution"
                           value="email-otp" />

                    <button type="submit" class="kc-button-secondary">
                        ${msg("emailOtpButton")}
                    </button>
                </form>
            </div>

            <hr class="kc-divider"/>

            <!-- ===================== -->
            <!-- MAGIC LINK -->
            <!-- ===================== -->
            <div class="kc-auth-method">
                <h3>${msg("magicLinkTitle")}</h3>
                <p>${msg("magicLinkDescription")}</p>

                <form action="${url.loginAction}" method="post">
                    <input type="hidden"
                           name="authenticationExecution"
                           value="magic-link" />

                    <button type="submit" class="kc-button-secondary">
                        ${msg("magicLinkButton")}
                    </button>
                </form>
            </div>

        </div>

    </#if>
</@layout.registrationLayout>
