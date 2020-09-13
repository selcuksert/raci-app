import { initTasksLoadApi, initStakeholderDropDown, initAddTaskApi, initSubmitTaskFormValidation } from "../scripts/tasks.js";
import { initSubmitStakeholderApi, initStakeholderFormValidation } from "../scripts/stakeholder.js";
import { initCncfDataLoadApi } from "../scripts/cncf.js";

(async () => {
    const pageList = [
        '/pages/menu.html',
        '/pages/message.html',
        '/pages/add-task.html',
        '/pages/add-stakeholder.html',
        '/pages/task-list.html',
        '/pages/cncf.html'
    ]

    var pageHtmlText = "";

    // Load and unify HTML content of pageList
    await Promise.all(pageList.map(async (pageToLoad) => {
        let pageHtml = await fetch(pageToLoad);
        let pageText = await pageHtml.text();

        pageHtmlText += pageText;
    }));

    class RaciApp extends HTMLElement {
        constructor() {
            super();
            /* Define API endpoints once globally */
            $.fn.api.settings.api = {
                'cncf-data': '/cncf/data',
                'task': '/task',
                'stakeholder': '/stakeholder'
            };
        }
        connectedCallback() {
            this.innerHTML = pageHtmlText;

            $('.menu .item').tab();
            $('#message-modal').modal();

            initTasksLoadApi();

            initAddTaskApi();
            initSubmitTaskFormValidation();

            initStakeholderDropDown('stakeholder-select-r');
            initStakeholderDropDown('stakeholder-select-a');
            initStakeholderDropDown('stakeholder-select-c');
            initStakeholderDropDown('stakeholder-select-i');

            initSubmitStakeholderApi();
            initStakeholderFormValidation();

            initCncfDataLoadApi();
        }
    }
    customElements.define('raci-app', RaciApp);
})();