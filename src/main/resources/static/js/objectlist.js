
var flag2 = true;
$(function () {
    $("#add").click(function () {
        if (flag2){
            document.getElementById("newentity").style.display= "block";
            document.getElementById("add").innerHTML= "pack";
            flag2 = false;
        }else {
            document.getElementById("newentity").style.display= "none";
            document.getElementById("add").innerHTML= "Add New Entity";
            flag2 = true;
        }
    })
    $("#savenew").click(function () {
        $.ajax({
            type: "post",
            url: "/newentity.do",
            data: {name:$('#name').val(),description:$('#description').val()},
            success: function (result) {
                alert(result);
                document.location.reload();
            }
        });
    });
});
