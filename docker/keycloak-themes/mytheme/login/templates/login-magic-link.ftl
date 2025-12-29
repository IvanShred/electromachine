<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "form">
        <form id="kc-form-magiclink" action="${url.loginAction}" method="post">
            <input type="hidden" name="auth_method" value="magiclink">
            
            <div class="form-group">
                <label for="email">${msg("email")}</label>
                <input type="email" id="email" name="email" 
                       class="form-control" required
                       placeholder="example@domain.com">
            </div>
            
            <#if message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                <div class="alert alert-${message.type}">
                    ${message.summary}
                </div>
            </#if>
            
            <div class="form-group">
                <button type="submit" class="btn-primary">
                    ${msg("sendMagicLink")}
                </button>
            </div>
        </form>
        
        <div class="text-center">
            <a href="${url.loginUrl}">${msg("tryAnotherWay")}</a>
        </div>
    </#if>
</@layout.registrationLayout>