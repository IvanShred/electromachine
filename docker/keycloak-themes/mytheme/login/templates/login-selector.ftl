<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        <div class="logo-container">
            <h1>ELECTROMACHINE</h1>
        </div>
        <h2 id="page-title">Выберите способ входа</h2>
        
        <#if message?? && message.type = 'error'>
            <div class="alert alert-error">
                Действие истекло. Пожалуйста, продолжите вход сейчас.
            </div>
        </#if>
    <#elseif section = "form">
        <div id="auth-selector" class="auth-methods-container">
            <div class="auth-methods-grid">
                <div class="auth-method-card" onclick="selectAuthMethod('password')" id="method-password">
                    <div class="auth-method-icon">🔐</div>
                    <div class="auth-method-content">
                        <h3 class="auth-method-title">Логин и пароль</h3>
                        <p class="auth-method-desc">
                            Войти с помощью имени пользователя и пароля
                        </p>
                    </div>
                </div>
                
                <div class="auth-method-card" onclick="selectAuthMethod('magiclink')" id="method-magiclink">
                    <div class="auth-method-icon">✨</div>
                    <div class="auth-method-content">
                        <h3 class="auth-method-title">Magic link</h3>
                        <p class="auth-method-desc">
                            Получить magic link на email для входа без пароля
                        </p>
                    </div>
                </div>
                
                <div class="auth-method-card" onclick="selectAuthMethod('username')" id="method-username">
                    <div class="auth-method-icon">👤</div>
                    <div class="auth-method-content">
                        <h3 class="auth-method-title">Имя пользователя</h3>
                        <p class="auth-method-desc">
                            Начать вход с указания имени пользователя
                        </p>
                    </div>
                </div>
            </div>
        </div>
        
        <#-- Формы (скрытые) -->
        <div id="forms-container" style="display: none;">
            <div id="password-form" style="display: none;">
                <form id="kc-form-login" action="${url.loginAction}" method="post">
                    <div class="form-group">
                        <label for="username">Имя пользователя или Email</label>
                        <input type="text" id="username" name="username" 
                               class="form-control" autofocus
                               placeholder="Введите имя пользователя или email">
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Пароль</label>
                        <input type="password" id="password" name="password" 
                               class="form-control"
                               placeholder="Введите пароль">
                    </div>
                    
                    <#if realm.rememberMe>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" id="rememberMe" name="rememberMe"> Запомнить меня
                            </label>
                        </div>
                    </#if>
                    
                    <div class="form-group">
                        <button type="submit" class="btn-primary">Войти</button>
                    </div>
                    
                    <div class="text-center">
                        <a href="#" onclick="showMethodSelector(); return false;" style="color: #4299e1; text-decoration: none;">
                            ← Выбрать другой способ
                        </a>
                    </div>
                </form>
            </div>
            
            <div id="magiclink-form" style="display: none;">
                <form id="kc-form-magiclink" action="${url.loginAction}" method="post">
                    <input type="hidden" name="auth_method" value="magiclink">
                    
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" 
                               class="form-control" required
                               placeholder="example@domain.com">
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn-primary">
                            Отправить Magic Link
                        </button>
                    </div>
                    
                    <div class="text-center">
                        <a href="#" onclick="showMethodSelector(); return false;" style="color: #4299e1; text-decoration: none;">
                            ← Выбрать другой способ
                        </a>
                    </div>
                </form>
            </div>
            
            <div id="username-form" style="display: none;">
                <form id="kc-form-username" action="${url.loginAction}" method="post">
                    <input type="hidden" name="auth_method" value="username_only">
                    
                    <div class="form-group">
                        <label for="username_only">Имя пользователя</label>
                        <input type="text" id="username_only" name="username" 
                               class="form-control" required
                               placeholder="Введите имя пользователя">
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn-primary">
                            Продолжить
                        </button>
                    </div>
                    
                    <div class="text-center">
                        <a href="#" onclick="showMethodSelector(); return false;" style="color: #4299e1; text-decoration: none;">
                            ← Выбрать другой способ
                        </a>
                    </div>
                </form>
            </div>
        </div>
        
        <#-- Социальные логины -->
        <#if social.providers??>
            <div class="social-login">
                <div class="social-title">Или войдите через</div>
                <div class="social-buttons">
                    <#list social.providers as p>
                        <a href="${p.loginUrl}" class="social-btn">
                            ${p.displayName}
                        </a>
                    </#list>
                </div>
            </div>
        </#if>
        
        <#-- Регистрация -->
        <#if realm.registrationAllowed && !registrationDisabled??>
            <div class="register-link">
                <p>Новый пользователь? <a href="${url.registrationUrl}">Зарегистрироваться</a></p>
            </div>
        </#if>
        
        <script>
            function selectAuthMethod(method) {
                // Меняем заголовок
                document.getElementById('page-title').textContent = getMethodTitle(method);
                
                // Скрываем выбор методов
                document.getElementById('auth-selector').style.display = 'none';
                
                // Показываем формы
                document.getElementById('forms-container').style.display = 'block';
                
                // Скрываем все формы, показываем выбранную
                document.getElementById('password-form').style.display = 'none';
                document.getElementById('magiclink-form').style.display = 'none';
                document.getElementById('username-form').style.display = 'none';
                
                document.getElementById(method + '-form').style.display = 'block';
                
                // Добавляем параметр в URL для перезагрузки страницы
                window.history.pushState({}, '', '?auth_method=' + method);
            }
            
            function showMethodSelector() {
                // Возвращаем заголовок
                document.getElementById('page-title').textContent = 'Выберите способ входа';
                
                // Показываем выбор методов
                document.getElementById('auth-selector').style.display = 'block';
                
                // Скрываем формы
                document.getElementById('forms-container').style.display = 'none';
                
                // Убираем параметр из URL
                window.history.pushState({}, '', window.location.pathname);
            }
            
            function getMethodTitle(method) {
                const titles = {
                    'password': 'Вход с логином и паролем',
                    'magiclink': 'Вход через Magic Link',
                    'username': 'Вход по имени пользователя'
                };
                return titles[method] || 'Вход';
            }
            
            // При загрузке страницы проверяем параметр
            window.onload = function() {
                const urlParams = new URLSearchParams(window.location.search);
                const method = urlParams.get('auth_method');
                
                if (method && ['password', 'magiclink', 'username'].includes(method)) {
                    selectAuthMethod(method);
                }
            };
        </script>
    </#if>
</@layout.registrationLayout>