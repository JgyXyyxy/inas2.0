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
function verifytime() {
    var ts = document.getElementById("inputTime").value;
    var r = new RegExp("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
    var bool = r.test(ts);
    if (bool){
        return true;
    }else {
        alert("时间格式错误(YYYY-MM-DD),例：2018-09-01");
        return false;
    }
}


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
