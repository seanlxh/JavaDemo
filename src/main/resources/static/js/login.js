var username ;
var password ;
$(document).ready(function() {
$("#ajaxLogin").click(function abc() {
    username = $("#username").val();
    password = $("#password").val();
    $.post("/ajaxLogin?username="+username+"&password="+password, {
    }, function(result) {
        if (result.status == 200) {
            location.href = "/index/index";
        }
        else if (result.status == 201){
            location.href = "/index/userIndex";
        }
        else {
            $("#erro").html(result.message);
        }
    });
});
});