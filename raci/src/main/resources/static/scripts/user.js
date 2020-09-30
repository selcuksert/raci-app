import { showMessage } from "./common.js";

function initRoleDropDown() {
    $('.ui.dropdown')
        .dropdown();
}

function initSubmitUserApi() {
    $('#submit-user').api({
        action: 'add user',
        method: 'POST',
        beforeSend: function (settings) {
            let uname = $('#username').val();
            let passwd = $('#password').val();
            let roleText = $("input[name='role']").val();

            if (!$('#user-form').form('is valid')) {
                // Cancel the request
                return false;
            }

            settings.data = JSON.stringify({
                username: uname,
                password: passwd,
                role: roleText
            });

            return settings;
        },
        beforeXHR(xhrObject) {
            xhrObject.setRequestHeader('Content-Type', 'application/json');
            xhrObject.setRequestHeader('Accept', 'application/json');
        },
        onSuccess: function (response) {
            showMessage('Success', response.message);
            $('#username').val('');
            $('#password').val('');
            $('#repassword').val('');
        },
        onFailure: function (response) {
            showMessage('Error', response.message);
        }
    });
}

function initUserFormValidation() {
    $('#user-form').form({
        fields: {
            username: {
                identifier: 'username',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a username.'
                    }
                ]
            },
            password: {
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
            repassword: {
                identifier: 'repassword',
                rules: [
                    {
                        type: 'match[password]',
                        prompt: 'Passwords do not match.'
                    }
                ]
            },
            role: {
                identifier: 'role',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please select a role.'
                    }
                ]
            }
        }
    });
}

export default function initUserModule() {
    initRoleDropDown();
    initSubmitUserApi();
    initUserFormValidation();
}