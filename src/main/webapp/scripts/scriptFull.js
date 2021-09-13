(document).ready(function () {
    let showAll = false;
    buildTable(showAll);

    $('#add').click(function () {
        validateAndAdd();
        setTimeout(function () {
            buildTable(showAll);
        }, 100);
    });

    $('#flexSwitchCheckDefault').click(function () {
        showAll = !showAll;
        buildTable(showAll);
    });
});

function validateAndAdd() {
    if ($('#description').val() === '') {
        alert('Enter ' + $('#description').attr('id'));
        return false;
    }
    addTask();
    return false;
}

function addTask() {
    $.get("http://localhost:8080/TodoApp/add", {
        description: $('#description').val()
    }).done(function (response) {
        console.log("Response Data: " + response);
    }).fail(function (err) {
        alert('Request Failed!');
        console.log("Request Failed: " + err);
    });
}

function buildTable(showAll) {
    $.get("http://localhost:8080/TodoApp/index"
    ).done(function (response) {
        let rows = [];
        $.each(response, function (key, val) {
            if (showAll === false) {
                if (val.done === false) {
                    rows.push('<tr><td>' + val.description + '</td><td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="' + val.id + '">' +
                        '</div></td></tr>');
                }
            } else {
                if (val.done === false) {
                    rows.push('<tr><td>' + val.description + '</td><td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="' + val.id + '">' +
                        '</div></td></tr>');
                } else {
                    rows.push('<tr><td>' + val.description + '</td><td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="' + val.id + '" checked>' +
                        '</div></td></tr>');
                }
            }
        });
        $('#table').html(rows);
        $('table').find('input').click(function () {
            update($(this).attr("id"), showAll);
        });
    }).fail(function (err) {
        alert('buildTable Failed!');
        console.log("Request Failed: " + err);
    });
}

function update(id, showAll) {
    $.get("http://localhost:8080/TodoApp/update", {
        id: id
    }).done(function (response) {
        buildTable(showAll);
        console.log("Response Data: " + response);
    }).fail(function (err) {
        alert('Request Failed!');
        console.log("Request Failed: " + err);
    });
}