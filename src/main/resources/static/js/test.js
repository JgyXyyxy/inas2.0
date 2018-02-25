$(function () {
    $("#analysis").click(function () {
        $.ajax({
            type: "post",
            url: "/extract.do",
            data: {rawinfo: $('#rawinfo').val()},
            success: function (result) {
                createExtractTable(result);
            }
        });
    })
});
function createExtractTable(data) {
    var panelStr =
        " <div class=\"panel panel-primary\" >\n" +
        "<div class=\"panel-heading\">Extract Result</div>\n" +
        "<div class=\"panel-body\" id=\"resultBody\">\n" +
        "</div>\n" + "</div>";
    $("#extractresult").html(panelStr);

    var tableStr = "<table class=\"table table-striped\" id=\"resultTable\">";
    tableStr = tableStr
        + "<tr>"
        +"<th>实体名</th>"
        +"<th>时间点</th>"
        +"<th>地点</th>"
        +"<th>描述</th>"
        +"</tr>";
    $("#resultBody").append(tableStr);
    var len = data.length;
    for ( var i = 0; i < len; i++) {
        var input1 = $("<input type='text' name='input1' />");
        input1.attr('value', data[i]);

    }
}