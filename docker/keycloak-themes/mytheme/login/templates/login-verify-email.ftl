<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        <div class="logo-container">
            <h1>ELECTROMACHINE</h1>
        </div>
        <h2>Подтверждение email</h2>
    <#elseif section = "form">
        <form action="${url.loginAction}" method="post">
            <!-- Форма подтверждения email -->
            <button type="submit">Подтвердить</button>
        </form>
    </#if>
</@layout.registrationLayout>