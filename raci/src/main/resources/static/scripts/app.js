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

function clearTable(tableId) {
  $('#' + tableId + ' tbody').empty();
}

$(document).ready(function () {
  $('.menu .item').tab();

  /* Define API endpoints once globally */
  $.fn.api.settings.api = {
    'get cncf data': '/cncf/data',
    'get task list': 'http://ec2-34-245-89-225.eu-west-1.compute.amazonaws.com:8080/tasks?page=0&size=70'
  };

  $('#cncf')
    .api({
      action: 'get cncf data',
      beforeSend: function (settings) {
        $('#cncf-loader').addClass("active");
        clearTable('cncf-table');

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
        console.error(response);
      }
    });

  $('#tasks')
    .api({
      action: 'get task list',
      beforeSend: function (settings) {
        $('#task-loader').addClass("active");
        clearTable('task-table');

        return settings;
      },
      onSuccess: function (response) {
        $('#task-loader').removeClass("active");
        // valid response and response.success = true
        $.each(response._embedded.tasks, (index, value) => addTaskEntry('task-table', value));
      },
      onFailure: function (response) {
        $('#task-loader').removeClass("active");
        // request failed, or valid response but response.success = false
        console.error(response);
      }
    });
});
