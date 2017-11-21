$(function(){

    $('#teamForm #clearTeam').click(function (e) {
        e.preventDefault();
        $('#teamForm').fromObject({});
    });

    $('#teamForm #saveTeam').click(function (e) {
        e.preventDefault();
        if($('#teamForm').valid()){
            var name = $('#teamForm input[name="name"]').val();
            var userIds = $('#teamForm select[name="users[]"]').val();
            console.log(userIds);
            $.ajax({
                type: "post",
                url: 'TeamServlet',
                data: {
                    method: "create",
                    name: name,
                    userIds: userIds,
                },
                success: function(team){
                    console.log(team);
                    if(team != "error"){
                        $('#teamRow').tmpl(team).appendTo($('#teamSection').find('#tblTeams tbody'));
                    }
                    else {
                        console.log("Cannot add team!");
                    }
                }
            })
        }
    });

});