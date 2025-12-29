<#-- login-page-expired.ftl -->
<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <#if section = "header">
        <div class="logo-container">
            <h1>ELECTROMACHINE</h1>
        </div>
        <h2>Выберите способ входа</h2>
    <#elseif section = "form">
        <#-- Просто редиректим на нашу кастомную страницу -->
        <script>
            window.location.href = '${url.loginUrl}';
        </script>
        
        <div class="text-center" style="padding: 40px;">
            <p>Перенаправление на страницу выбора метода...</p>
            <a href="${url.loginUrl}">Нажмите здесь, если не произошло автоматического перенаправления</a>
        </div>
    </#if>
</@layout.registrationLayout>