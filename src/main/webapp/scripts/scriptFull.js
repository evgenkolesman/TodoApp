 $(document).ready(function () {
    let showAll = false;
    buildTable(showAll);

    $('#add').click(function () {
        validateAndAdd();
        setTimeout(function () {
            buildTable(showAll);
        }, 10);
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
    $.post("/TodoApp/add", {
        description: $('#description').val()
    }).done(function (response) {
        console.log("Response Data: " + response);
    }).fail(function (err) {
        alert('Request Failed!');
        console.log("Request Failed: " + err);
    });
}

function buildTable(showAll) {
    $.getJSON("/TodoApp/index").done(function (response) {
        let rows = [];
        $.each(response, function (key, val) {
            if (showAll === false) {
                if (val.done === false) {
                    rows.push('<tr><td>' + val.id + '</td><td>' + val.description
                         + '</td><td>'+ timestampToDate(val.created) +'</td><td>' +
                        '<div class="form-check">' +
                    '<input class="form-check-input" type="checkbox" value="" id="flexSwitchCheckDefault" checked>' +
                    '</div>' +
                        '</td></tr>');
                }
            } else {
                if (val.done === false) {
                    rows.push('<tr><td>' + val.id + '</td><td>' + val.description
                        + '</td><td>'+ timestampToDate(val.created) +'</td><td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="flexSwitchCheckDefault" checked>' +
                        '</div>' +
                        '</td></tr>');
                } else {
                    rows.push('<tr><td>' + val.id + '</td><td>' + val.description
                        + '</td><td>'+ timestampToDate(val.created) +'</td><td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="flexSwitchCheckDefault" checked>' +
                        '</div>' +
                        '</td></tr>');
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
    $.post("TodoApp/update", {
        id: id
    }).done(function (response) {
        buildTable(showAll);
        console.log("Response Data: " + response);
    }).fail(function (err) {
        alert('Request Failed!');
        console.log("Request Failed: " + err);
    });
}

 function timestampToDate(ts) {
     var d = new Date();
     d.setTime(ts);
     return ('0' + d.getDate()).slice(-2) + '.' + ('0' + (d.getMonth() + 1)).slice(-2) + '.' + d.getFullYear();
 }