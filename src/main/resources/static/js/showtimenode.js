
var flag2 = true;
$(function () {
    $("#add").click(function () {
        if (flag2){
            document.getElementById("newconnection").style.display= "block";
            document.getElementById("add").innerHTML= "pack";
            flag2 = false;
        }else {
            document.getElementById("newconnection").style.display= "none";
            document.getElementById("add").innerHTML= "Add New Connection";
            flag2 = true;
        }
    })
});
