import { showMessage, clearTableHeader, clearTableBody, httpErrorHandler } from "./common.js";

var stakeholderDataObj = [];

function addTaskHeader(tableId, stakeholders) {

    let stakeholderCols = '';
    $.each(stakeholders, (index, stakeholder) => stakeholderCols += '<th>' + stakeholder + '</th>')

    let roles = document.querySelector('raci-app').roles;
    let delHeader = '';

    if (roles.includes("ADMIN") || roles.includes("USER")) {
        delHeader = '<th class="center aligned">Delete Task</th>';
    }

    $('#' + tableId + ' > thead').append(
        '<tr>' +
        '<th>#</th>' +
        delHeader +
        '<th>Task</th>' +
        '<th>Additional Info</th>' +
        stakeholderCols +
        '</tr>'
    );
}

function addTaskEntry(tableId, index, task) {
    let respCols = '';
    let respList = $.each(task.responsibilities,
        (index, value) => respCols += '<td data-label="' + value + '">' + value + '</td>');

    let roles = document.querySelector('raci-app').roles;
    let delColumn = '';

    if (roles.includes("ADMIN") || roles.includes("USER")) {
        delColumn = '<td data-label="Delete" class="center aligned"><a class="delete item"><input type="hidden" name="taskId" value="' + task.id + '"><i class="trash icon"></i></a></td>';
    }

    $('#' + tableId + ' > tbody:last-child').append(
        '<tr>' +
        '<td data-label="Index">' + (index + 1) + '</td>' +
        delColumn +
        '<td data-label="Task">' + task.taskDescription + '</td>' +
        '<td data-label="Additional Info">' + task.additionalInfo + '</td>' +
        respCols +
        '</tr>'
    );
}

function initTasksLoadApi() {
    $('#tasks')
        .api({
            action: 'get tasks',
            beforeSend: function (settings) {
                $('#task-loader').addClass("active");
                clearTableHeader('task-table');
                clearTableBody('task-table');

                return settings;
            },
            beforeXHR(xhrObject) {
                xhrObject.setRequestHeader('Content-Type', 'application/json');
                xhrObject.setRequestHeader('Accept', 'application/json');
            },
            onSuccess: function (response) {
                $('#task-loader').removeClass("active");
                // valid response and response.success = true
                addTaskHeader('task-table', response.stakeholders);

                $.each(response.tasks, (index, task) => addTaskEntry('task-table', index, task));

                initDeleteTaskApi();
            },
            onError: function (errorMessage, element, xhr) {
                httpErrorHandler(xhr);
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
        debug: false,
        verbose: false,
        apiSettings: {
            action: 'get stakeholders',
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
        action: 'task operation',
        method: 'POST',
        beforeSend: function (settings) {
            let taskDetailText = $('#task-description').val();
            let taskInfoText = $('#task-info').val();

            if (!$('#task-form').form('is valid')) {
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
            $('#task-form').form('reset');
            showMessage('Success', response.message);
        },
        onError: function (errorMessage, element, xhr) {
            httpErrorHandler(xhr);
        },
        onFailure: function (response) {
            // request failed, or valid response but response.success = false
            showMessage('Error', response.message);
        }
    });
}

function initDeleteTaskApi() {
    $(".delete.item").api({
        action: 'task operation',
        method: 'DELETE',
        beforeSend: function (settings) {
            let taskIdToDel = $(this).find('input[name="taskId"]').val();
            settings.data = JSON.stringify({
                taskId: taskIdToDel
            });

            return settings;
        },
        beforeXHR(xhrObject) {
            xhrObject.setRequestHeader('Content-Type', 'application/json');
            xhrObject.setRequestHeader('Accept', 'application/json');
        },
        onSuccess: function (response) {
            // valid response and response.success = true
            $('#tasks').api('query');
            showMessage('Success', response.message);
        },
        onError: function (errorMessage, element, xhr) {
            httpErrorHandler(xhr);
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

export default function initTaskModule() {
    initTasksLoadApi();
    initAddTaskApi();

    setTimeout(() => {
        initStakeholderDropDown('stakeholder-select-r');
        initStakeholderDropDown('stakeholder-select-a');
        initStakeholderDropDown('stakeholder-select-c');
        initStakeholderDropDown('stakeholder-select-i');
    }, 2000);

    initSubmitTaskFormValidation();
}