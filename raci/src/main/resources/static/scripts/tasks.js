import { showMessage, clearTable, checkValEmpty } from "./common.js";

var stakeholderDataObj = [];

function addTaskEntry(tableId, responseItem) {
    $('#' + tableId + ' > tbody:last-child').append(
        '<tr>' +
        '<td data-label="id">' + responseItem.id + '</td>' +
        '<td data-label="Category">' + responseItem.category + '</td>' +
        '<td data-label="Subcategory">' + responseItem.subcategory + '</td>' +
        '<td data-label="Task">' + responseItem.definition + '</td>' +
        '<td data-label="Solution">' + responseItem.solution + '</td>' +
        '</tr>'
    );
}

function initTasksLoadApi() {
    $('#tasks')
        .api({
            action: 'task',
            beforeSend: function (settings) {
                $('#task-loader').addClass("active");
                clearTable('task-table');

                return settings;
            },
            beforeXHR(xhrObject) {
                xhrObject.setRequestHeader('Content-Type', 'application/json');
                xhrObject.setRequestHeader('Accept', 'application/json');
            },
            onSuccess: function (response) {
                $('#task-loader').removeClass("active");
                // valid response and response.success = true
                $.each(response._embedded.tasks, (index, value) => addTaskEntry('task-table', value));
            },
            onFailure: function (response) {
                $('#task-loader').removeClass("active");
                // request failed, or valid response but response.success = false
                showMessage('Error', response.message);
            }
        });
}

function initStakeholderDropDown(id) {
    $('#' + id).dropdown({
        apiSettings: {
            action: 'stakeholder',
            on: 'change',
            cache: false,
            beforeXHR(xhrObject) {
                xhrObject.setRequestHeader('Content-Type', 'application/json');
                xhrObject.setRequestHeader('Accept', 'application/json');
            },
            onResponse: function (response) {
                let suiResponse = {
                    "success": true,
                    "results": []
                };

                // Modify API response to match with Semantic-UI requirements
                $.each(response, (index, value) => suiResponse["results"].push({
                    "name": value.name,
                    "value": value.name,
                    "text": value.name
                }));

                return suiResponse;
            },
            onFailure: function (response) {
                showMessage('Error', response.message);
            }
        },
        filterRemoteData: true,
        onAdd(value, text, $choice) {
            let respData = $choice.parent().parent().attr('data-resp');
            addResponsibilities(value, respData);
        },
        onRemove(value, text, $choice) {
            let respData = $choice.parent().parent().attr('data-resp');
            removeResponsibilities(value, respData);
        }
    });
}

function addResponsibilities(stakeholder, respData) {

    let stkData = stakeholderDataObj.find(stk => stk.name === stakeholder);

    if (!stkData) {
        stkData = {};
        stkData["name"] = stakeholder;
        stkData["responsibilityNames"] = [];
        stkData.responsibilityNames.push(respData);
        stakeholderDataObj.push(stkData);
    }
    else {
        if (!stkData.responsibilityNames.find(value => value === respData)) {
            stkData.responsibilityNames.push(respData);
        }
    }

}

function removeResponsibilities(stakeholder, respData) {

    let stkData = stakeholderDataObj.find(stk => stk.name === stakeholder);

    if (stkData) {
        let responsibilityNamesAfterRemove =
            stkData.responsibilityNames.filter((value) => value !== respData);

        if (responsibilityNamesAfterRemove.length === 0) {
            stakeholderDataObj = stakeholderDataObj.filter(stk => stk.name !== stakeholder);
        }
        else {
            stkData.responsibilityNames = responsibilityNamesAfterRemove;
        }
    }

}

function initAddTaskApi() {
    $('#submit-task').api({
        action: 'task',
        method: 'POST',
        beforeSend: function (settings) {
            let taskDetailText = $('#task-description').val();
            if (checkValEmpty(taskDetailText)) {
                // Cancel the request
                return false;
            }

            let taskInfoText = $('#task-info').val();

            let respSelectText = $("input[name='responsible']").val();
            if (checkValEmpty(respSelectText)) {
                // Cancel the request
                return false;
            }

            let accSelectText = $("input[name='accountable']").val();
            if (checkValEmpty(accSelectText)) {
                // Cancel the request
                return false;
            }

            let conSelectText = $("input[name='consulted']").val();
            if (checkValEmpty(conSelectText)) {
                // Cancel the request
                return false;
            }

            let infSelectText = $("input[name='informed']").val();
            if (checkValEmpty(infSelectText)) {
                // Cancel the request
                return false;
            }

            settings.data = JSON.stringify({
                taskDetail: taskDetailText,
                additionalInfo: taskInfoText,
                stakeholderData: stakeholderDataObj
            });

            return settings;
        },
        beforeXHR(xhrObject) {
            xhrObject.setRequestHeader('Content-Type', 'application/json');
            xhrObject.setRequestHeader('Accept', 'application/json');
        },
        onSuccess: function (response) {
            // valid response and response.success = true
            showMessage('Success', response.message);
        },
        onFailure: function (response) {
            // request failed, or valid response but response.success = false
            showMessage('Error', response.message);
        }
    });
}

function initSubmitTaskFormValidation() {
    $('#task-form').form({
        fields: {
            description: {
                identifier: 'description',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter task description.'
                    }
                ]
            },
            responsible: {
                identifier: 'responsible',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a responsible.'
                    }
                ]
            },
            accountable: {
                identifier: 'accountable',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a accountable.'
                    }
                ]
            },
            consulted: {
                identifier: 'consulted',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a consulted.'
                    }
                ]
            },
            informed: {
                identifier: 'informed',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a informed.'
                    }
                ]
            }
        }
    });
}

export { initTasksLoadApi, initAddTaskApi, initStakeholderDropDown, initSubmitTaskFormValidation };