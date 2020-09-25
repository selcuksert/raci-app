import { showMessage } from "./common.js";

function initSubmitPasswordApi() {
    $('#submit-password').api({
        action: 'change password',
        method: 'PATCH',
        beforeSend: function (settings) {
            let passwd = $('#chpassword').val();

            if (!$('#change-password-form').form('is valid')) {
                // Cancel the request
                return false;
            }

            settings.data = JSON.stringify({
                newPassword: passwd
            });

            return settings;
        },
        beforeXHR(xhrObject) {
            xhrObject.setRequestHeader('Content-Type', 'application/json');
            xhrObject.setRequestHeader('Accept', 'application/json');
        },
        onSuccess: function (response) {
            showMessage('Success', response.message);
            $('#chpassword').val('');
            $('#chrepassword').val('');
        },
        onFailure: function (response) {
            showMessage('Error', response.message);
        }
    });
}

function initPasswordFormValidation() {
    $('#change-password-form').form({
        fields: {
            chpassword: {
                identifier: 'password',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a password.'
                    },
                    {
                        type: 'length[6]',
                        prompt: 'Your password must be at least 6 characters'
                    }
                ]
            },
            chrepassword: {
                identifier: 'repassword',
                rules: [
                    {
                        type: 'match[password]',
                        prompt: 'Passwords do not match.'
                    }
                ]
            }
        }
    });
}

export { initSubmitPasswordApi, initPasswordFormValidation };
