tasksController = function() {

    function errorLogger(errorCode, errorMessage) {
        console.log(errorCode +':'+ errorMessage);
    }

    var taskPage;
    var initialised = false;

    /**
     * makes json call to server to get task list.
     * currently just testing this and writing return value out to console
     * 111917kl
     */
    function retrieveTasksServer() {
        $.ajax("TaskServlet", {
            "type": "get",
            dataType: "json"
            // "data": {
            //     "first": first,
            //     "last": last
            // }
        }).done(displayTasksServer.bind()); //need reference to the tasksController object
    }

    /**
     * 111917kl
     * callback for retrieveTasksServer
     * @param data
     */
    function displayTasksServer(data) { //this needs to be bound to the tasksController -- used bind in retrieveTasksServer 111917kl
        console.log(data);
        tasksController.loadServerTasks(data);
    }

    function taskCountChanged() {
        var count = $(taskPage).find( '#tblTasks tbody tr').length;
        $('footer').find('#taskCount').text(count);
    }

    function clearTask() {
        $(taskPage).find('form').fromObject({});
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

    return {
        init : function(page, callback) {
            if (initialised) {
                callback()
            } else {
                taskPage = page;
                storageEngine.init(function() {
                    storageEngine.initObjectStore('task', function() {
                        callback();
                    }, errorLogger)
                }, errorLogger);
                $(taskPage).find('[required="required"]').prev('label').append( '<span>*</span>').children( 'span').addClass('required');
                $(taskPage).find('tbody tr:even').addClass('even');

                $(taskPage).find('#btnAddTask').click(function(evt) {
                    evt.preventDefault();
                    $(taskPage).find('#taskCreation').removeClass('not');
                });

                /**	 * 11/19/17kl        */
                $(taskPage).find('#btnRetrieveTasks').click(function(evt) {
                    evt.preventDefault();
                    console.log('making ajax call');
                    retrieveTasksServer();
                });

                $(taskPage).find('#tblTasks tbody').on('click', 'tr', function(evt) {
                    $(evt.target).closest('td').siblings().andSelf().toggleClass('rowHighlight');
                });

                $(taskPage).find('#tblTasks tbody').on('click', '.deleteRow',
                    function(evt) {

                        var taskId = $(evt.target).data().taskId;
                        console.log(taskId);
                        if(confirm("Are you sure to delete?")){
                            $.ajax({
                                type: 'POST',
                                url: 'TaskServlet',
                                data: {
                                    method: 'delete',
                                    taskId: taskId,
                                },
                                success: function(response){
                                    if(response != "error"){
                                        $(evt.target).parents('tr').remove();
                                        taskCountChanged();
                                    }
                                    else {
                                        console.log(response);
                                    }
                                },
                                error: function (xhr, status, exception) {
                                    errorLogger;
                                }
                            });
                        }
                    }
                );

                $(taskPage).find('#tblTasks tbody').on('click', '.editRow',
                    function(evt) {
                        $(taskPage).find('#taskCreation').removeClass('not');
                        $.ajax({
                            type: "POST",
                            url: "TaskServlet",
                            data: {
                                method: "findById",
                                taskId: $(evt.target).data().taskId
                            },
                            success: function (response) {
                                if(response != "error"){
                                    $(taskPage).find('form').fromObject(response);
                                }
                                else {
                                    console.log(response);
                                }
                            }
                        });
                    }
                );

                $(taskPage).find('#clearTask').click(function(evt) {
                    evt.preventDefault();
                    clearTask();
                });

                $(taskPage).find('#tblTasks tbody').on('click', '.completeRow', function(evt) {

                    $.ajax({
                       type: "POST",
                       url: "TaskServlet",
                       data: {
                           taskId: $(evt.target).data().taskId,
                           method: 'complete',
                       },
                       success: function (response) {
                           if(response != "error"){
                               $(evt.target).parents('tr').remove();
                               $('#taskRow').tmpl(response).appendTo($(taskPage).find('#tblTasks tbody'));
                               taskCountChanged();
                               renderTable();
                           }
                           else {
                               console.log(response);
                           }

                       },
                       error: function(xhr, status, exception){
                           errorLogger;
                       }
                    });
                });

                $(taskPage).find('#saveTask').click(function(evt) {
                    var event = evt;
                    evt.preventDefault();
                    if ($(taskPage).find('form').valid()) {
                        var task = $(taskPage).find('form').toObject();
                        $.ajax({
                            type: 'POST',
                            url: 'TaskServlet',
                            data: {
                                method: 'create',
                                taskId: task.id,
                                userId: task.user,
                                category: task.category,
                                task: task.task,
                                requiredBy: task.requiredBy,
                                priority: task.priority,
                            },
                            success: function (response) {
                                if(response != "error"){

                                    // REMOVE EDIT ROW
                                    if(task.id){
                                        $('#task' + task.id).remove();
                                    }
                                    $('#taskRow').tmpl(response).appendTo($(taskPage).find('#tblTasks tbody'));
                                    taskCountChanged();
                                    renderTable();
                                    clearTask();
                                    $(taskPage).find('#taskCreation').addClass('not');
                                }
                                else {
                                    console.log(response);
                                }
                            }
                        });
                    }
                });
                initialised = true;
            }
        },
        /**
         * 111917kl
         * modification of the loadTasks method to load tasks retrieved from the server
         */
        loadServerTasks: function(tasks) {
            $(taskPage).find('#tblTasks tbody').empty();
            $.each(tasks, function (index, task) {
                if (!task.complete) {
                    task.complete = false;
                }
                $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                taskCountChanged();
                console.log('about to render table with server tasks');
                //renderTable(); --skip for now, this just sets style class for overdue tasks 111917kl
            });
        },
        loadTasks : function() {
            $(taskPage).find('#tblTasks tbody').empty();

            storageEngine.findAll('task', function(tasks) {
                tasks.sort(function(o1, o2) {
                    return Date.parse(o1.requiredBy).compareTo(Date.parse(o2.requiredBy));
                });
                $.each(tasks, function(index, task) {
                    if (!task.complete) {
                        task.complete = false;
                    }
                    $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                    taskCountChanged();
                    renderTable();
                });
            }, errorLogger);

        },
        loads: function () {
            $(taskPage).find('#tblTasks tbody').empty();
            $(taskPage).find('#userId').empty();

            $.ajax({
                type: 'POST',
                url: 'TaskServlet',
                data: {
                    method: 'init',
                },
                success: function(response){
                    if(response != "error"){
                        $.each(response.users, function (index, user) {
                            $('#taskUser').tmpl(user).appendTo($(taskPage).find('#userId'));
                        });

                        response.tasks.sort(function(o1, o2) {
                            return Date.parse(o1.requiredBy).compareTo(Date.parse(o2.requiredBy));
                        });
                        $.each(response.tasks, function(index, task) {
                            if (!task.complete) {
                                task.complete = false;
                            }
                            $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                            taskCountChanged();
                            renderTable();
                        });
                    }
                    else {
                        console.log(response);
                    }
                }
            })
        }
    }
}();
