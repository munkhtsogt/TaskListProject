$(function(){

    $('#teamForm #clearTeam').click(function (e) {
        e.preventDefault();
        $('#teamForm').fromObject({});
    });

    $('#teamForm #saveTeam').click(function (e) {
        e.preventDefault();
        if($('#teamForm').valid()){
            var name = $('#teamForm input[name="name"]').val();
            var users = $('#teamForm select[name="users[]"]').val();

            console.log(users);
        }
    });

});