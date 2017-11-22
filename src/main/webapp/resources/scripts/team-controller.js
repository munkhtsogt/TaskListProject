$(function(){

    $(document).ajaxStart(function(){
        $('#loading').css({ visibility: 'visible' });
    }).ajaxStop(function(){
        $('#loading').css({ visibility: 'hidden' });
    });

    $('#teamForm #clearTeam').click(function (e) {
        e.preventDefault();
        $('#teamForm').fromObject({});
    });

    $('#teamForm #saveTeam').click(function (e) {
        e.preventDefault();
        if($('#teamForm').valid()){
            var name = $('#teamForm input[name="name"]').val();
            var teamId = $('#teamForm input[name="id"]').val();
            var userIds = $('#teamForm select[name="users[]"]').val();
            console.log(userIds);
            $.ajax({
                type: "post",
                url: 'TeamServlet',
                data: {
                    method: "create",
                    teamId: teamId,
                    name: name,
                    userIds: userIds,
                },
                success: function(team){
                    if(team != "error"){

                        if(team.id){
                            $('#team' + team.id).remove();
                        }

                        var tr = $('<tr>', { id: 'team' + team.id });
                        var td1 = $('<td>', { text: team.name });
                        var spans = "";
                        $.each(team.users, function(i, v){
                            var span = '<span>' + v.username + '</span>';
                            spans += span + ", ";
                        })
                        var td2 = $('<td>', { html: spans });

                        var actions = '<nav>' +
                                          '<a href="#" class="editTeam" data-team-id="' + team.id + '">Edit</a>' +
                                          '<a href="#" class="deleteTeam" data-team-id="' + team.id + '">Delete</a>' +
                                          '<a href="#" class="filterTeam" data-team-id="' + team.id + '">Filter</a>' +
                                       '</nav>';

                        var td3 = $('<td>', { html: actions });
                        tr.append(td1);
                        tr.append(td2);
                        tr.append(td3);
                        $('#tblTeams tbody').append(tr);
                        $('#teamForm').fromObject({});
                    }
                    else {
                        console.log("Cannot add team!");
                    }
                }
            })
        }
    });

    $(document).on('click', '#tblTeams .editTeam', function(){
        var self = $(this);
        self.parent().parent().parent().find('td').addClass('rowHighlight');
        $.ajax({
            type: "POST",
            url: 'TeamServlet',
            data: {
                method: 'findById',
                teamId: self.attr('data-team-id'),
            },
            success:function(team){
                if(team != "error"){
                    $('#teamForm input[name="id"]').val(team.id);
                    $('#teamForm input[name="name"]').val(team.name);

                    var userIds = [];
                    $.each(team.users, function(i,v){
                        userIds.push(v.id);
                    })

                    $('#teamForm select[name="users[]"]').val(userIds);
                }
                else {
                    console.log("Error on updating team!")
                }
            }
        })
    });

    $(document).on('click', '#tblTeams .deleteTeam', function(){
        var self = $(this);
        if(confirm("Are you sure to delete?")){
            $.ajax({
                type: 'POST',
                url: 'TeamServlet',
                data: {
                    method: 'delete',
                    teamId: self.attr('data-team-id')
                },
                success: function(response){
                    if(response != "error"){
                        self.parents('tr').remove();
                    }
                    else {
                        console.log("Error on deleting team!")
                    }
                }
            })
        }
    });

    $(document).on('click', '#tblTeams .filterTeam', function(){
        var self = $(this);
        self.parent().parent().parent().find('td').addClass('rowHighlight');
        $.ajax({
            type: 'POST',
            url: 'TaskServlet',
            data: {
                method: 'filterByTeam',
                teamId: self.attr('data-team-id')
            },
            success: function(tasks){
                console.log(tasks);
                $('#tblTasks tbody').empty();
                $.each(tasks, function(index, task) {
                    $('#taskRow').tmpl(task).appendTo($('#taskPage').find('#tblTasks tbody'));
                    var count = $('#taskPage').find( '#tblTasks tbody tr').length;
                    $('footer').find('#taskCount').text(count);
                    renderTable();
                });
            }
        })
    });
});