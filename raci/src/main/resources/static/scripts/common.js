function showMessage(header, message) {
    $('#modal-header').text(header);
    $('#modal-message').text(message);
    $('#message-modal').modal('show');
}

function clearTableHeader(tableId) {
    $('#' + tableId + ' thead').empty();
}
function clearTableBody(tableId) {
    $('#' + tableId + ' tbody').empty();
}

function checkValEmpty(txtInput) {
    return (txtInput === undefined || txtInput == "");
}

function httpErrorHandler(xhr) {
    if(xhr && xhr.status) {
        switch(xhr.status) {
            case 401:
                location.href = '/logout';
                break;
        }
    }
}

export { showMessage, clearTableBody, clearTableHeader, checkValEmpty, httpErrorHandler };