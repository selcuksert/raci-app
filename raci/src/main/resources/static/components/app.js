import { initTasksLoadApi, initStakeholderDropDown, initAddTaskApi, initSubmitTaskFormValidation } from "../scripts/tasks.js";
import { initSubmitStakeholderApi, initStakeholderFormValidation } from "../scripts/stakeholder.js";
import { initRoleDropDown, initSubmitUserApi, initUserFormValidation } from "../scripts/user.js"
import { initSubmitPasswordApi, initPasswordFormValidation, initPassChangeModal } from "../scripts/changepassword.js";
import { initCncfDataLoadApi } from "../scripts/cncf.js";

(async () => {
    const pageList = [
        '/pages/menu.html',
        '/pages/message.html',
        '/pages/add-task.html',
        '/pages/add-stakeholder.html',
        '/pages/task-list.html',
        '/pages/cncf.html',
        '/pages/add-user.html',
        '/pages/change-password.html'
    ]

    // Load and glue HTML content of pageList in given order
    const pageHtmlText = await Promise.all(pageList.map(async (pageToLoad) => {
        let pageHtml = await fetch(pageToLoad);
        let pageText = await pageHtml.text();

        return pageText;
    }));

    class RaciApp extends HTMLElement {
        constructor() {
            super();
            /* Define API endpoints once globally */
            $.fn.api.settings.api = {
                'cncf-data': '/cncf/data',
                'task': '/task',
                'add stakeholder': '/admin/stakeholder',
                'get stakeholders': '/stakeholder',
                'add user': '/admin/user',
                'change password': '/password',
                'logout': '/logout'
            };
        }
        connectedCallback() {
            this.innerHTML = pageHtmlText.join("\n");

            $('.menu .item').tab();
            $('#message-modal').modal();

            initTasksLoadApi();

            initAddTaskApi();
            initSubmitTaskFormValidation();

            initStakeholderDropDown('stakeholder-select-r');
            initStakeholderDropDown('stakeholder-select-a');
            initStakeholderDropDown('stakeholder-select-c');
            initStakeholderDropDown('stakeholder-select-i');

            initRoleDropDown();
            initSubmitUserApi();
            initUserFormValidation();

            initSubmitPasswordApi();
            initPasswordFormValidation();
            initPassChangeModal();

            initSubmitStakeholderApi();
            initStakeholderFormValidation();

            initCncfDataLoadApi();
        }
    }
    customElements.define('raci-app', RaciApp);
})();
