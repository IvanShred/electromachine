<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        <div class="logo-container">
            <h1>ELECTROMACHINE</h1>
        </div>
        <h2>Предоставление доступа</h2>
    <#elseif section = "form">
        <form action="${url.loginAction}" method="post">
            <!-- Форма OAuth grant -->
            <button type="submit">Разрешить</button>
        </form>
    </#if>
</@layout.registrationLayout>