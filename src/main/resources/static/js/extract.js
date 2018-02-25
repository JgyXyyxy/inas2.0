var myChart = echarts.init(document.getElementById('select_area'));
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
    });
    $("#showGraph").click(function () {
        $.ajax({
            type: "post",
            url: "/graphtest.do",
            data: {objectId: $('#objectId').val()},
            dataType: "json",
            success: function (result) {
                document.getElementById("edit").style.display= "block";
                setGraphParas(result);
            }
        });
    });
    $("#extractresult").on("click","a",function () {
        var line ="<tr>"
            +"<td class=\"col-md-2\">" + "<input class=\"form-control\"/>" +"</td>"
            +"<td class=\"col-md-2\">"+ "<input  class=\"form-control\"/>"  + "</td>"
            +"<td class=\"col-md-1\">"+ "<input  class=\"form-control\"/>"  + "</td>"
            +"<td class=\"col-md-6\">"+ "<input  class=\"form-control\"/>"  + "</td>"
            +"<td class=\"col-md-2\">"+ "<button class=\"addToPic\">添加</button>"  + "</td>"
            +"</tr>";
        $("#resultTable").append(line);
    })
});

function createExtractTable(data) {
    var addnew = "<tr>" +
        "<td><a id=\"addNewLine\">Add New Line</a></td>" +
        "</tr>";
    var panelStr = " <div class=\"panel panel-primary\" >\n" +
        "<div class=\"panel-heading\">Extract Result</div>\n" +
        "<form>\n" +
        "<div class=\"panel-body\">\n";
    var tableStr = panelStr + "<table class=\"table table-striped\" id=\"resultTable\">";
    tableStr = tableStr
        + "<tr>"
        +"<th>实体名</th>"
        +"<th>时间点</th>"
        +"<th>地点</th>"
        +"<th>描述</th>"
        +"<th>添加</th>"
        +"</tr>";
    var len = data.length;
    for ( var i = 0; i < len; i++) {
        tableStr = tableStr + "<tr>"
            +"<td class=\"col-md-2\">" + "<input value="+data[i].entityName +" class=\"form-control\"/>" +"</td>"
            +"<td class=\"col-md-2\">"+ "<input value="+data[i].timepoint +" class=\"form-control\"/>"  + "</td>"
            +"<td class=\"col-md-1\">"+ "<input value="+data[i].location +" class=\"form-control\"/>"  + "</td>"
            +"<td class=\"col-md-6\">"+ "<input value="+data[i].description +" class=\"form-control\"/>"  + "</td>"
            +"<td class=\"col-md-2\">"+ "<button class=\"addToPic\">添加</button>"  + "</td>"
            +"</tr>";
    }
    tableStr = tableStr + "</table>"+ addnew +"</div>" + "</form>" + "</div>";
    //添加到div中
    $("#extractresult").html(tableStr);
}

function setGraphParas(result) {
    myChart.setOption({
        title: {
            text: '                                      Object Dependencies'
        },
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
            {
                type: 'graph',
                layout: 'none',
                // progressiveThreshold: 700,
                data: result.nodes.map(function (node) {
                    return {
                        x: node.x,
                        y: node.y,
                        id: node.id,
                        name: node.label,
                        symbolSize: node.size,
                        itemStyle: {
                            normal: {
                                color: node.color
                            }
                        }
                    };
                }),
                edges: result.edges.map(function (edge) {
                    return {
                        source: edge.sourceID,
                        target: edge.targetID
                    };
                }),
                label: {
                    // normal:{
                    //   show:true,
                    //   position:'bottom'
                    // }
                    emphasis: {
                        position: 'bottom',
                        show: true
                    }
                },
                roam: true,
                focusNodeAdjacency: true,
                lineStyle: {
                    normal: {
                        width: 2,
                        curveness: 0.3,
                        opacity: 0.7
                    }
                }
            }
        ]
    }, true);
}