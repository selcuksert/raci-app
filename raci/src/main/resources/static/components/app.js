import initTaskModule from "../scripts/tasks.js";
import initStakeholderModule from "../scripts/stakeholder.js";
import initUserModule from "../scripts/user.js"
import initChangePasswordModule from "../scripts/changepassword.js";
import initCncfDataLoadApi from "../scripts/cncf.js";

(async () => {
    const pageList = [
        '/pages/view/menu-open.html',
        '/pages/admin/menu.html',
        '/pages/user/menu.html',
        '/pages/view/menu.html',
        '/pages/view/menu-close.html',
        '/pages/view/message.html',
        '/pages/view/task-list.html',
        '/pages/view/cncf.html',
        '/pages/view/change-password.html',
        '/pages/user/add-task.html',
        '/pages/admin/add-stakeholder.html',
        '/pages/admin/add-user.html'
    ]

    const roleResponse = await fetch('/roles');
    const roles = await roleResponse.json();

    // Load and glue HTML content of pageList in given order
    const pageHtmlText = await Promise.all(pageList.map(async (pageToLoad) => {
        let pageClass = pageToLoad.match(/\/pages\/(\w+)+/i)[1];

        if (roles.includes(pageClass.toUpperCase())) {
            let pageHtml = await fetch(pageToLoad);
            let pageText = await pageHtml.text();

            return pageText;
        }
    }));

    class RaciApp extends HTMLElement {
        constructor() {
            super();
            /* Define API endpoints once globally */
            $.fn.api.settings.api = {
                'cncf-data': '/cncf/data',
                'get tasks': '/tasks',
                'add task': '/user/task',
                'add stakeholder': '/admin/stakeholder',
                'get stakeholders': '/stakeholders',
                'add user': '/admin/user',
                'change password': '/password',
                'logout': '/logout'
            };
        }

        connectedCallback() {
            this.innerHTML = pageHtmlText.join("\n");

            $('.menu .item').tab();
            $('#message-modal').modal();

            initTaskModule();

            initStakeholderModule();

            initUserModule();

            initChangePasswordModule();

            initCncfDataLoadApi();
        }
    }
    customElements.define('raci-app', RaciApp);
})();
