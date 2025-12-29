<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        <div class="logo-container">
            <h1>ELECTROMACHINE</h1>
        </div>
        <h2>Обновление профиля</h2>
    <#elseif section = "form">
        <form action="${url.loginAction}" method="post">
            <!-- Форма обновления профиля -->
            <button type="submit">Обновить</button>
        </form>
    </#if>
</@layout.registrationLayout>