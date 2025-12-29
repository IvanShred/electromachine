<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "form">
        <div class="form-group">
            <label for="password" class="control-label">${msg("password")}</label>
            <input type="password" id="password" name="password" 
                   class="form-control" 
                   autocomplete="current-password"
                   placeholder="${msg("password")}">
        </div>
    </#if>
</@layout.registrationLayout>