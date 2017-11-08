var a;
var b;
var count = 0;
$(function () {
    $("#searchraw").click(function () {
        $.ajax({
            type: "post",
            url: "/rawinfo.do",
            data: {keyWord: $('#keyWord').val()},
            dataType: "json",
            success: function (result) {
                document.getElementById("searchresult").style.display="block";
                document.getElementById("photo").style.display="none";
                document.getElementById("newentity").style.display="none";
                document.getElementById("textarea1").value=result.paras[0];
                a = result.paras;
                b = result.entitylist;
                var selection = document.getElementById("select");
                selection.innerHTML = "";
                for(var i=0;i<b.length;i++){
                    var option=document.createElement("option");
                    $(option).val(b[i]);
                    $(option).text(b[i]);
                    $('#select').append(option);
                }

            }
        });
    });
    $("#next").click(function () {
        count = count+1;
        var yushu = count % 3;
        document.getElementById("textarea1").value=a[yushu];
    });
    $("#addnew").click(function () {
        document.getElementById("newentity").style.display="block";
    });

    $("#save").click(function () {
        $.ajax({
            type: "post",
            url: "/saverawinfo.do",
            data: {textarea1: $('#textarea1').val(),select:$('#select').val()},
            success: function (result) {
                alert(result);
            },
            error:function(){
                alert("error")
            }
        });
    });

    $("#savenew").click(function () {
        $.ajax({
            type: "post",
            url: "/savenew.do",
            data: {textarea1: $('#textarea1').val(),name:$('#name').val(),description:$('#description').val()},
            success: function (result) {
                alert(result);
            }
        });
    });

});


