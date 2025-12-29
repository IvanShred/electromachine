<#-- login-config-totp.ftl -->
<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <#if section = "header">
        <div class="logo-container">
            <h1>ELECTROMACHINE</h1>
        </div>
        <h2>Выберите способ входа</h2>
        
        <#if message??>
            <div class="alert alert-${message.type}">
                <#if message.type = 'error'>Действие истекло. Пожалуйста, продолжите вход сейчас.
                <#else>${message.summary}</#if>
            </div>
        </#if>
    <#elseif section = "form">
        <#-- Вместо стандартной страницы TOTP показываем наш селектор -->
        <div class="auth-methods-grid">
            <div class="auth-method-card" onclick="selectMethod('password')">
                <div class="auth-method-icon">🔐</div>
                <div class="auth-method-content">
                    <h3 class="auth-method-title">Логин и пароль</h3>
                    <p class="auth-method-desc">
                        Войти с помощью имени пользователя и пароля
                    </p>
                </div>
            </div>
            
            <div class="auth-method-card" onclick="selectMethod('magiclink')">
                <div class="auth-method-icon">✨</div>
                <div class="auth-method-content">
                    <h3 class="auth-method-title">Magic link</h3>
                    <p class="auth-method-desc">
                        Получить magic link на email для входа без пароля
                    </p>
                </div>
            </div>
            
            <div class="auth-method-card" onclick="selectMethod('username')">
                <div class="auth-method-icon">👤</div>
                <div class="auth-method-content">
                    <h3 class="auth-method-title">Имя пользователя</h3>
                    <p class="auth-method-desc">
                        Начать вход с указания имени пользователя
                    </p>
                </div>
            </div>
        </div>
        
        <#-- Формы (скрытые) -->
        <div id="forms-container" style="display: none;">
            <!-- Вставьте сюда формы из login-selector.ftl -->
        </div>
        
        <script>
            function selectMethod(method) {
                // Перенаправляем на нашу кастомную страницу с параметром
                window.location.href = '${url.loginUrl}?auth_method=' + method;
            }
            
            // Если есть параметр в URL, сразу выбираем метод
            const urlParams = new URLSearchParams(window.location.search);
            const method = urlParams.get('auth_method');
            if (method) {
                selectMethod(method);
            }
        </script>
    </#if>
</@layout.registrationLayout>