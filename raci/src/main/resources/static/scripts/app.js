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

function showMessage(header, message) {
  $('#modal-header').text(header);
  $('#modal-message').text(message);
  $('#message-modal').modal('show');
}

$(document).ready(function () {
  $('.menu .item').tab();

  $('#message-modal').modal();

  /* Define API endpoints once globally */
  $.fn.api.settings.api = {
    'get cncf data': '/cncf/data',
    'get task list': 'http://ec2-34-245-89-225.eu-west-1.compute.amazonaws.com:8080/tasks?page=0&size=70',
    'add stakeholder': '/stakeholder'
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
        showMessage('Error', JSON.stringify(response));
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
        showMessage('Error', JSON.stringify(response));
      }
    });

  $('#submit-stakeholder').api({
    action: 'add stakeholder',
    method: 'POST',
    beforeSend: function (settings) {
      let sname = $('#stakeholder-name').val();

      if (sname === undefined || sname == "") {
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
    },
    onFailure: function (response) {
      showMessage('Error', response.message);
    }
  });

  $('#stakeholder-form').form({
    fields: {
      name: {
        identifier: 'name',
        rules: [
          {
            type: 'empty',
            prompt: 'Please enter a name'
          }
        ]
      }
    }
  });
});
