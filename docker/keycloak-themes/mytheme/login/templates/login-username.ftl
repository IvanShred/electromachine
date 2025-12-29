<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "form">
        <div class="form-group">
            <label for="username" class="control-label">
                <#if !realm.loginWithEmailAllowed>${msg("username")}
                <#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}
                <#else>${msg("email")}</#if>
            </label>
            <input type="text" id="username" name="username" 
                   class="form-control" 
                   autofocus autocomplete="username"
                   placeholder="<#if !realm.loginWithEmailAllowed>${msg("username")}
                   <#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}
                   <#else>${msg("email")}</#if>">
        </div>
    </#if>
</@layout.registrationLayout>