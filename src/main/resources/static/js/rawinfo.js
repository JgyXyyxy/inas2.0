var a;
var count = 0;
$(function () {
    $("#searchraw").click(function () {
        $.ajax({
            type: "post",
            url: "/rawinfo.do",
            data: {keyWord: $('#keyWord').val()},
            dataType: "json",
            success: function (result) {
                document.getElementById("textarea1").style.display="block";
                document.getElementById("next").style.display="block";
                document.getElementById("textarea1").value=result[0];
                a = result;
            }
        });
    });
    $("#next").click(function () {
        count = count+1;
        var yushu = count % 3;
        document.getElementById("textarea1").value=a[yushu];
    })

});


