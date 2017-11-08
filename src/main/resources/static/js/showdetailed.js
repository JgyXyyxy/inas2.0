var a;
$(function () {
    $("#show").click(function () {
        $.ajax({
            type: "post",
            url: "/rawtext.do",
            data: {objectId:$('#objectId').val()},
            dataType: "json",
            success: function (result) {
                a = result[0];
                document.getElementById("rawtext").style.display="block";
                document.getElementById("rawtext").innerText= a;
            }
        });
    });
});


