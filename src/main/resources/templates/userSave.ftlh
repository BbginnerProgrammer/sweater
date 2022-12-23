<#import "parts/common.ftlh" as c>

<@c.page>
    User editor

    <form action="/user/${user.id}/save" method="post">
        <input type="text" name="username" value="${user.username}">
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("cheked", "")}>${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</@c.page>