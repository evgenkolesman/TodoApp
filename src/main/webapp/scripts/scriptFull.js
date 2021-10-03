$(document).ready(function () {
    let showAll = false;
    buildTable(showAll);
    hello();
    getCategory();

    $('#add').click(function () {
        validateAndAdd();
        setTimeout(function () {
            buildTable(showAll);
        }, 1000);
    });

    $('#flexSwitchCheckDefault').click(function () {
        showAll = !showAll;
        buildTable(showAll);
    });

    // $('#checkBut').click(function () {
    //     showAll = !showAll;
    //     buildTable(showAll);
    // })

    function hello() {
        $.getJSON("/TodoApp/greet.do"
        ).done(function (response) {
            $('#hello').text('Hello, ' + response.name + ' ! ');
            console.log("Response Data: " + response);
        }).fail(function (err) {
            alert('Request Failed!');
            console.log("Request Failed: " + err);
        });
    }
});

function getCategory() {
    let categories = [];
    categories.push('<option selected>Choose</option>');
    $.getJSON("/TodoApp/categories"
    ).done(function (response) {
        $.each(response, function (key, val) {
            categories.push('<option value="' + val.id + '">' + val.name + '</option>');
        });
        $('#category').html(categories);
    }).fail(function (err) {
        alert('Get category Failed!');
        console.log("Get category error: " + err);
    });
}

function validateAndAdd() {
    if ($('#description').val() == '' || $('#category').val() == 'Choose') {
        $('#description').val() == '' ? alert('Enter description') : alert('Enter category');
        return false;
    }
    addTask();
    return false;
}

function addTask() {
    $.post("/TodoApp/add", $('form').serialize()
    ).done(function (response) {
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
            // let checkBut = val.done;
            if (showAll === false) {
                if (val.done == false) {
                    rows.push('<tr>' +
                        '<td>' + val.id + '</td>' +
                        '<td>' + val.description + '</td>' +
                        '<td>' + timestampToDate(val.created) + '</td>' +
                        '<td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="' + val.id + '">' +
                        '</div>' +
                        '</td>' +
                        '<td>' + val.user.name + //'</td></tr>');
                        '<td>' + print(val.categories) + '</td></tr>');
                }
            } else {
                if (val.done == false) {
                    rows.push('<tr>' +
                        '<td>' + val.id + '</td>' +
                        '<td>' + val.description +
                        '</td>' +
                        '<td>' + timestampToDate(val.created) + '</td>' +
                        '<td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="' + val.id + '">' +
                        '</div>' +
                        '</td>' +
                        '<td>' + val.user.name + //'</td></tr>');
                        '<td>' + print(val.categories) + '</td></tr>');
                } else {
                    rows.push('<tr>' +
                        '<td>' + val.id + '</td>' +
                        '<td>' + val.description +
                        '</td>' +
                        '<td>' + timestampToDate(val.created) + '</td>' +
                        '<td>' +
                        '<div class="form-check">' +
                        '<input class="form-check-input" type="checkbox" value="" id="' + val.id + '" checked>' +
                        '</div>' +
                        '</td>' +
                        '<td>' + val.user.name + //'</td></tr>');
                        '<td>' + print(val.categories) + '</td></tr>');
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

function print(category) {
    let result = [];
    category.forEach(c => result.push(c.name))
    return result;
}

function update(id, showAll) {
    $.post("/TodoApp/update", {
        id: id
    }).done(function (response) {
        let value = showAll;
        alert(typeof value); // boolean

        value = String(value); // теперь value это строка "true"
        alert(typeof value);

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