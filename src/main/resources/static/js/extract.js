var myChart = echarts.init(document.getElementById('select_area'));
var initialNodes = [];
var initialEdges = [];
var allNodes = [];
var allEdges = [];
var newNodes = [];
var newEdges = [];
var selected = {}
var deslist = {}
var pointsForId = {}
var desForId = {}
var initialPointsForId = {}
var selectedNum = 0;
var pre = "dd";
var selectlist = [];
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
    $("#detail").click(function () {
        window.location.href = "/object/" + $("#objectId").val();
    });
    $("#params").click(function () {
        window.location.href = "/param/" + $("#objectId").val();
    });
    $("#net").click(function () {
        window.location.href = "/net/" + $("#objectId").val();
    });
    $("#commit").click(function () {
        var raw = $('#rawinfo').val();
        var id = $('#objectId').val();
        $.ajax({
            type: "post",
            url: "/commit.do",
            data: {newNodes: JSON.stringify(newNodes), newEdges: JSON.stringify(newEdges), rawinfo: raw, id: id},
            success: function (result) {
                alert(result);
                window.location.href = "/search";
            }
        });
    });
    $("#resetNode").click(function () {
        console.log(initialNodes);
        console.log(initialEdges);
        newNodes = [];
        newEdges = [];
        allNodes = JSON.parse(JSON.stringify(initialNodes));
        allEdges = JSON.parse(JSON.stringify(initialEdges));
        pointsForId = JSON.parse(JSON.stringify(initialPointsForId));
        updateSelectList();
        setGraphParas(allNodes, allEdges);
    });
    $("#showGraph").click(function () {
        $.ajax({
            type: "post",
            url: "/graphtest.do",
            data: {objectId: $('#objectId').val()},
            dataType: "json",
            success: function (result) {
                document.getElementById("edit").style.display = "block";
                document.getElementById("buttonGroup").style.display = "block";
                allNodes = result.nodes;
                allEdges = result.edges;
                initialNodes = JSON.parse(JSON.stringify(result.nodes));
                initialEdges = JSON.parse(JSON.stringify(result.edges));
                selectlist.push($('#objectId').val());
                pointsForId[$('#objectId').val()] = result.nodes;
                initialPointsForId[$('#objectId').val()] = JSON.parse(JSON.stringify(result.nodes));
                updateSelectList();
                setGraphParas(allNodes, allEdges);
            }
        });
    });
    $("#modelFooter").on("click", "#toSearch", function () {
        pre = $('#prefix').val();
        console.log(pre)
        $.ajax({
            type: "post",
            url: "/searchtest.do",
            data: {prefix: $('#prefix').val()},
            dataType: "json",
            success: function (result) {
                createDesTable(result)

            }
        });
    });
    $("#extractresult").on("click", "#addNewLine", function () {
        var line = "<tr>"
            + "<td class=\"col-md-2\">" + "<input class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-2\">" + "<input  class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-1\">" + "<input  class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-6\">" + "<input  class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-2\">" + "<button type=\"button\" class=\"addNewNode\">添加</button>" + "</td>"
            + "</tr>";
        $("#resultTable").append(line);
    });
    $("#modelBody").on("click", "#addNewEntity", function () {
        var line = "<div id=\"newform\" class=\"form-inline\">\n" +
            "        <div class=\"form-group\">\n" +
            "            <label for=\"name\">名称</label>\n" +
            "            <input class=\"form-control\" id=\"name\" placeholder=\"名称\"/>\n" +
            "        </div>\n" +
            "        <div class=\"form-group\">\n" +
            "            <label for=\"description\">描述</label>\n" +
            "            <input class=\"form-control\" id=\"description\" placeholder=\"实体描述\"/>\n" +
            "        </div>\n" +
            "        <div class=\"form-group\">\n" +
            "        <button id=\"savenew\"  class=\"btn btn-primary\">添加</button>\n" +
            "        </div>\n" +
            "    </div>";
            // "        <button id=\"savenew\"  class=\"btn btn-primary\">添加</button>\n";
        $("#modelBody").append(line);
    });
    $("#modelBody").on("click", "#savenew", function () {
        var nameVal = $('#name').val()
        var desVal = $('#description').val()
        var prefix = pre
        $.ajax({
            type: "post",
            url: "/saveNewEntitytest.do",
            data: {name: nameVal, description: desVal, prefix: prefix},
            // dataType: "json",
            success: function (result) {
                createDesTable(result);
            }
        });
    });
    $('#myModal').on('hide.bs.modal', function () {
        var line =
            "<form class=\"form-inline\">\n" +
            "         <div class=\"form-group\">\n" +
            "                <label for=\"prefix\">Keyword</label>\n" +
            "                <input name=\"prefix\" class=\"form-control\" id=\"prefix\" placeholder=\"Michael Jackson\"/>\n" +
            "         </div>\n" +
            "</form>";
        $("#modelBody").html(line);
        var line2 = "<button type=\"button\" id=\"toSearch\" class=\"btn btn-primary\">Search</button>\n" +
            "        <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>";
        $("#modelFooter").html(line2);
    });

    $("#modelFooter").on("click", "#select", function () {
        var radio_ary = document.getElementsByName("test1");

        for (var i = 0; i < radio_ary.length; i++) {
            if (radio_ary[i].checked == true) { //得到选中的单选按钮状态（判断是否被选中）
                var parent = radio_ary[i].parentNode.parentNode;
            }
        }

        var single = parent.childNodes;
        var id = single[0].innerText;
        var des = single[1].innerText;
        selected[id] = des;
        deslist[selectedNum] = des;
        selectedNum = selectedNum + 1;
        selectlist.push(id);
        createSelectedTable();
        $.ajax({
            type: "post",
            url: "/graphforconn.do",
            data: {objectId: $("#objectId").val(), selectedId: id, num: selectedNum},
            // dataType: "json",
            success: function (result) {
                pointsForId[id] = result.nodes;
                initialPointsForId[id] = JSON.parse(JSON.stringify(result.nodes));
                allNodes = allNodes.concat(result.nodes);
                allEdges = allEdges.concat(result.edges);
                initialEdges = initialEdges.concat(result.edges);
                initialNodes = initialNodes.concat(result.nodes);
                updateSelectList();
                setGraphParas(allNodes, allEdges);
            }
        });
    });

    $("#extractresult").on("click", ".addNewNode", function () {
        var lineMark = this.parentNode.parentNode;
        var single = lineMark.childNodes;
        var entityName = single[0].firstChild.value;
        var timePoint = single[1].firstChild.value;
        var location = single[2].firstChild.value;
        var description = single[3].firstChild.value;
        var r = new RegExp("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        var r_1 = new RegExp("([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})");
        var r_2 = new RegExp("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-([0-9]{2}))");
        var ym = r_2.test(timePoint);
        var yy = r_1.test(timePoint);
        var bool = r.test(timePoint) || ym || yy;


        if (bool) {
            var time = timePoint.split("-");
            var day = 0;
            if (yy) {
                day = 0;
            } else if (ym) {
                day = getDays(time[0], time[1], 1) - 1;
            } else {
                day = getDays(time[0], time[1], time[2]);
            }
            var days = time[0] * 365 + day;
            var tag = false;
            var id;
            for (var i = 0; i < selectlist.length; i++) {
                if (entityName === getNameFormId(selectlist[i])) {
                    tag = true
                    id = selectlist[i];
                    break;
                }
            }
            if (tag) {
                var uid = uuid();
                var pointId = id.concat(":").concat(uid);
                var label = timePoint.concat(" ").concat(description);
                var color = "blue";
                if (id === $("#objectId").val()) {
                    color = "red"
                }
                pointsForId[id].push({x: 1, y: 1, id: pointId, label: label, size: 20, color: color, serial: days})
                newNodes.push({id: pointId, timepoint: timePoint, location: location, description: description})
                pointsForId[id].sort(sortSerial);
                var insert = 0;
                for (var i = 0; i < pointsForId[id].length; i++) {
                    if (pointsForId[id][i].x === 1) {
                        insert = i;
                    }
                }

                var x1 = pointsForId[id][insert - 1].x;
                var y1 = pointsForId[id][insert - 1].y;
                for (var i = insert; i < pointsForId[id].length; i++) {
                    pointsForId[id][i].x = x1 + 10;
                    pointsForId[id][i].y = y1;
                    x1 = x1 + 10;
                }
                var freshedNodes = [];
                for (key in pointsForId) {
                    freshedNodes = freshedNodes.concat(pointsForId[key]);
                }
                allNodes = freshedNodes;
                var a = pointsForId[id];
                if (insert === (pointsForId[id].length - 1)) {
                    allEdges.push({sourceID: pointsForId[id][insert - 1].id, targetID: pointId, note: ""})
                } else {
                    for (var i = 0; i < allEdges.length; i++) {
                        if ((allEdges[i].sourceID === pointsForId[id][insert - 1].id) && (allEdges[i].targetID === pointsForId[id][insert + 1].id)) {
                            allEdges.splice(i, 1);
                        } else if ((allEdges[i].targetID === pointsForId[id][insert - 1].id) && (allEdges[i].sourceID === pointsForId[id][insert + 1].id)) {
                            allEdges.splice(i, 1);
                        }
                    }
                    allEdges.push({sourceID: pointsForId[id][insert - 1].id, targetID: pointId, note: ""});
                    allEdges.push({sourceID: pointId, targetID: pointsForId[id][insert + 1].id, note: ""});
                }
                updateSelectList();
                setGraphParas(allNodes, allEdges);

            } else {
                alert("未加载或添加相应实体");
            }

        } else {
            alert("时间格式错误(YYYY-MM-DD),例：2018-09-01");
        }
    });


    $("#addConn").click(function () {
        var s1 = $('#select1 option:selected').text().split(":");
        var s2 = $('#select2 option:selected').text().split(":");
        var source = s1[0].concat(":", desForId[s1[1]]);
        var target = s2[0].concat(":", desForId[s2[1]]);
        var note = $('#connType').val();
        allEdges.push({sourceID: source, targetID: target, note: note});
        newEdges.push({sourceID: source, targetID: target, note: note});
        setGraphParas(allNodes, allEdges);
    });
});

function createSelectedTable() {
    var tableStr = "<table class=\"table table-striped\">";
    tableStr = tableStr
        + "<tr>"
        + "<th>序号</th>"
        + "<th>描述</th>"
        + "</tr>";
    for (var key in deslist) {
        tableStr = tableStr + "<tr>"
            + "<td class=\"col-md-2\">" + key + "</td>"
            + "<td class=\"col-md-3\">" + deslist[key] + "</td>"
            + "</tr>"
    }
    tableStr = tableStr + "</table>";
    $("#selectedEntities").html(tableStr);
}

function createDesTable(result) {
    var tableStr = "<table class=\"table table-striped\">";
    var add = "<a id=\"addNewEntity\" class=\"text-right\">添加一个新实体</a>"
    tableStr = tableStr
        + "<tr>"
        + "<th>ID</th>"
        + "<th>描述</th>"
        + "<th>选定</th>"
        + "</tr>";
    for (var key in result) {
        tableStr = tableStr + "<tr>"
            + "<td class=\"col-md-2\">" + key + "</td>"
            + "<td class=\"col-md-3\">" + result[key] + "</td>"
            + "<td class=\"col-md-1\">" + "<input name='test1' type=\"radio\" class=\"select2\" value=\"\" />" + "</td>"
            + "</tr>"
    }
    tableStr = tableStr + "</table>";
    $("#modelBody").html(tableStr);
    $("#modelBody").append(add);
    var line = "<button id=\"select\"  class=\"btn btn-primary\">引入</button>";
    line = line + "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>";
    $("#modelFooter").html(line);

}

function createExtractTable(data) {
    var addnew = "<tr>" +
        "<td><a id=\"addNewLine\">添加新的事件节点</a></td>" +
        "</tr>";
    var panelStr = " <div class=\"panel panel-success\" >\n" +
        "<div class=\"panel-heading\">LTP事件抽取结果</div>\n" +
        "<form>\n" +
        "<div class=\"panel-body\">\n";
    var tableStr = panelStr + "<table class=\"table table-striped\" id=\"resultTable\">";
    tableStr = tableStr
        + "<tr>"
        + "<th>实体名称</th>"
        + "<th>时间</th>"
        + "<th>地点</th>"
        + "<th>详情</th>"
        + "<th>添加</th>"
        + "</tr>";
    var len = data.length;
    for (var i = 0; i < len; i++) {
        tableStr = tableStr + "<tr>"
            + "<td class=\"col-md-2\">" + "<input value=" + data[i].entityName + " class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-2\">" + "<input value=" + data[i].timepoint + " class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-1\">" + "<input value=" + data[i].location + " class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-6\">" + "<input value=" + data[i].description + " class=\"form-control\"/>" + "</td>"
            + "<td class=\"col-md-2\">" + "<button type=\"button\" class=\"addNewNode\">添加</button>" + "</td>"
            + "</tr>";
    }
    tableStr = tableStr + "</table>" + addnew + "</div>" + "</form>" + "</div>";
    //添加到div中
    $("#extractresult").html(tableStr);
}

function updateSelectList() {
    var idAndLabel = {}
    desForId = {};
    for (key in pointsForId) {
        var labellist = [];
        for (var i = 0; i < pointsForId[key].length; i++) {
            var label = pointsForId[key][i].label;
            var id = pointsForId[key][i].id;
            var combine = id.split(":");
            desForId[label] = combine[1];
            labellist.push(label);
        }
        idAndLabel[key] = labellist;
    }
    var line = "";
    for (key in idAndLabel) {
        var name = getNameFormId(key);
        line = line + "<optgroup label=" + name + "/>"
        for (var i = 0; i < idAndLabel[key].length; i++) {
            var combine = key.concat(":", idAndLabel[key][i]);
            line = line + "<option>" + combine + "</option>";
        }
        line = line + "</optgroup>";
    }
    $("#select1").html(line);
    $("#select1").selectpicker('refresh');
    $("#select2").html(line);
    $("#select2").selectpicker('refresh');
}

function getDays(year, month, day) {
    // 构造1月1日
    var date = new Date(year, month - 1, day);
    var lastDay = new Date(year, month, day);
    lastDay.setMonth(0);
    lastDay.setDate(1);

    // 获取距离1月1日过去多少天
    var days = (date - lastDay) / (1000 * 60 * 60 * 24) + 1;
    return days;
}

function getNameFormId(id) {
    return id.substring(0, id.length - 8);
}

function setGraphParas(nodes, edges) {
    myChart.setOption({
        title: {
            text: '                               时间演进与关联网络示意图'
        },
        tooltip: {
            formatter: function (x) {
                return x.data.label.split("||").join("<br/>");
            }//设置提示框的内容和格式 节点和边都显示name属性
        },
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
            {
                type: 'graph',
                layout: 'none',
                edgeSymbol: ['circle', 'arrow'],
                // progressiveThreshold: 700,
                data: nodes.map(function (node) {
                    return {
                        x: node.x,
                        y: node.y,
                        id: node.id,
                        label: node.label,
                        name: node.label,
                        symbolSize: node.size,
                        itemStyle: {
                            normal: {
                                color: node.color
                            }
                        }
                    };
                }),
                edges: edges.map(function (edge) {
                    return {
                        source: edge.sourceID,
                        target: edge.targetID,
                        name: edge.note,
                        label: {
                            normal: {
                                show: true,
                                formatter: function (x) {
                                    return x.data.name;
                                }
                            }
                        },
                    };
                }),
                label: {
                    normal: {
                        show: true,
                        position: 'bottom',
                    },
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

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

function sortSerial(a, b) {
    return a.serial - b.serial
}