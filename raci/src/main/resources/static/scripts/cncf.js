import { showMessage, clearTableBody } from "./common.js";

function addCncfEntry(tableId, responseItem) {
    $('#' + tableId + ' > tbody:last-child').append(
        '<tr>' +
        '<td data-label="Name">' + responseItem.name + '</td>' +
        '<td data-label="Logo"><img src="https://landscape.cncf.io/' + responseItem.href + '"></td>' +
        '<td data-label="Category">' + responseItem.category + '</td>' +
        '<td data-label="Landscape">' + responseItem.landscape + '</td>' +
        '</tr>'
    );
}

export default function initCncfDataLoadApi() {
    $('#cncf')
        .api({
            action: 'cncf-data',
            beforeSend: function (settings) {
                $('#cncf-loader').addClass("active");
                clearTableBody('cncf-table');

                return settings;
            },
            onSuccess: function (response) {
                $('#cncf-loader').removeClass("active");
                // valid response and response.success = true
                $.each(response, (index, value) => addCncfEntry('cncf-table', value));
            },
            onFailure: function (response) {
                $('#cncf-loader').removeClass("active");
                // request failed, or valid response but response.success = false
                showMessage('Error', JSON.stringify(response));
            }
        });
}