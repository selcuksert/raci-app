function initLoginErrorMessage() {
    let loginUrl = window.location.href;
    let errorPattern = /\\?error/g;

    $('#login-error .close')
        .on('click', function () {
            $(this)
                .closest('.message')
                .transition('fade')
                ;
        });

    if (errorPattern.test(loginUrl)) {
        $('#login-error').removeClass('hidden');
    }
}

function initLoginSuccessMessage() {
    let loginUrl = window.location.href;
    let logoutPattern = /\\?logout/g;

    $('#login-success .close')
        .on('click', function () {
            $(this)
                .closest('.message')
                .transition('fade')
                ;
        });

    if (logoutPattern.test(loginUrl)) {
        $('#login-success').removeClass('hidden');
    }
}

function initLoginForm() {
    initLoginSuccessMessage();
    initLoginErrorMessage();

    $('.ui.form')
        .form({
            fields: {
                username: {
                    identifier: 'username',
                    rules: [
                        {
                            type: 'empty',
                            prompt: 'Please enter your username'
                        }
                    ]
                },
                password: {
                    identifier: 'password',
                    rules: [
                        {
                            type: 'empty',
                            prompt: 'Please enter your password'
                        },
                        {
                            type: 'length[6]',
                            prompt: 'Your password must be at least 6 characters'
                        }
                    ]
                }
            }
        });
}

$(document)
    .ready(function () {
        initLoginForm();
    });
