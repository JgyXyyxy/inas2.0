$(function () {
    $("#save").click(function () {
        $.ajax({
            type: "post",
            url: "/saveraw.do",
            data: {textarea1: $('#textarea1').val(),objectId:$('#objectId').val()},
            success: function (result) {
                alert(result);
            },
            error:function(){
                alert("error")
            }
        });
    });
});
