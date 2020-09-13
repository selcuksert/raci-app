function showMessage(header, message) {
    $('#modal-header').text(header);
    $('#modal-message').text(message);
    $('#message-modal').modal('show');
}

function clearTable(tableId) {
    $('#' + tableId + ' tbody').empty();
}

function checkValEmpty(txtInput) {
    return (txtInput === undefined || txtInput == "");
}

export { showMessage, clearTable, checkValEmpty };