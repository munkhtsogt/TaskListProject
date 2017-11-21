var priority = 0;
var user = 0;
function sort(attribute, elem){
    $(elem).find('span').remove();

    if(attribute == 'priority'){
        priority++;
        if(priority % 2 == 0){
            // ASCENDING
            $(elem).append($('<span>', { text: '▲' }));
        }
        else {
            // DESC
            $(elem).append($('<span>', { text: '▼' }));
        }
    }
    if(attribute == "user"){
        user++;
        if(user % 2 == 0){
            // ASCENDING
            $(elem).append($('<span>', { text: '▲' }));
        }
        else {
            // DESC
            $(elem).append($('<span>', { text: '▼' }));
        }
    }

    $.ajax({
        type: "POST",
        url: 'TaskServlet',
        data: {
            method: "loadTasks",
            attribute: attribute,
            priority: priority,
            user: user,
        },
        success: function(tasks){
            if(tasks != "error"){
                $(taskPage).find('#tblTasks tbody').empty();
                $.each(tasks, function(index, task) {
                    $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                    renderTable();
                });
            }
            else {
                console.log(tasks);
            }
        }
    });
}

function renderTable() {
    $.each($(taskPage).find('#tblTasks tbody tr'), function(idx, row) {
        var due = Date.parse($(row).find('[datetime]').text());
        if (due.compareTo(Date.today()) < 0) {
            $(row).addClass("overdue");
        } else if (due.compareTo((2).days().fromNow()) <= 0) {
            $(row).addClass("warning");
        }
    });
}

$(function(){

    $('table th[data-sortable="true"]').hover(function(){
        $(this).css({ cursor: 'pointer' });
    })
})