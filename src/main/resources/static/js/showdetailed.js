var a;
var flag1 = true;
var flag2 = true;
$(function () {
    $("#show").click(function () {
        if (flag1){
            document.getElementById("rawtext").style.display= "block";
            document.getElementById("show").innerHTML= "pack";
            flag1 = false;
        }else {
            document.getElementById("rawtext").style.display= "none";
            document.getElementById("show").innerHTML= "show";
            flag1 = true;
        }
    });
    $("#add").click(function () {
        if (flag2){
            document.getElementById("newtimenode").style.display= "block";
            document.getElementById("add").innerHTML= "pack";
            flag2 = false;
        }else {
            document.getElementById("newtimenode").style.display= "none";
            document.getElementById("add").innerHTML= "Add New Timenode";
            flag2 = true;
        }
    })
});



window.onload = function() {
    $.ajax({
        type: "post",
        url: "/rawtext.do",
        data: {objectId:$('#objectId').val()},
        dataType: "json",
        success: function (result) {
            a = result[0];
            document.getElementById("rawtext").innerText= a;
        }
    });
};
