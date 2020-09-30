import { showMessage } from "./common.js";

function initSubmitStakeholderApi() {
    $('#submit-stakeholder').api({
        action: 'add stakeholder',
        method: 'POST',
        beforeSend: function (settings) {
            let sname = $('#stakeholder-name').val();

            if (!$('#stakeholder-form').form('is valid')) {
                // Cancel the request
                return false;
            }

            settings.data = JSON.stringify({
                name: sname
            });

            return settings;
        },
        beforeXHR(xhrObject) {
            xhrObject.setRequestHeader('Content-Type', 'application/json');
            xhrObject.setRequestHeader('Accept', 'application/json');
        },
        onSuccess: function (response) {
            showMessage('Success', 'Stakeholder added: ' + response.name);
            $('#stakeholder-name').val('');
        },
        onFailure: function (response) {
            showMessage('Error', response.message);
        }
    });
}

function initStakeholderFormValidation() {
    $('#stakeholder-form').form({
        fields: {
            name: {
                identifier: 'name',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a name.'
                    }
                ]
            }
        }
    });
}

export default function initStakeholderModule() {
    initSubmitStakeholderApi();
    initStakeholderFormValidation();
}