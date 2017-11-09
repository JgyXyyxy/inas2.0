var myChart = echarts.init(document.getElementById('select_area'));



$(function () {
    $("#search").click(function () {
        $.ajax({
            type: "post",
            url: "/graph.do",
            data: {objectId: $('#objectId').val()},
            dataType: "json",
            success: function (result) {
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
                // return false;
            }
        });
    })

});

