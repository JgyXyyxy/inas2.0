var myChart = echarts.init(document.getElementById('select_area'));
$(function () {
    $("#search").click(function () {
        document.getElementById("transc").style.display= "block";
        $.ajax({
            type: "post",
            url: "/graph.do",
            data: {objectId: $('#objectId').val()},
            dataType: "json",
            success: function (result) {
                myChart.setOption({
                    title: {
                        text: '                                      实体导向关联网络'
                    },
                    animationDurationUpdate: 1500,
                    animationEasingUpdate: 'quinticInOut',
                    series: [
                        {
                            type: 'graph',
                            layout: 'none',
                            edgeSymbol: ['circle', 'arrow'],
                            // progressiveThreshold: 700,
                            data: result.nodes.map(function (node) {
                                return {
                                    x: node.x,
                                    y: node.y,
                                    id: node.id,
                                    name: node.label,
                                    label:node.label,
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
                                normal:{
                                  show:true,
                                  position:'bottom'
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
                // return false;
            }
        });
    });
    $("#trans").click(function () {
        $.ajax({
            type: "post",
            url: "/graphforevent.do",
            data: {objectId: $('#objectId').val()},
            dataType: "json",
            success: function (result) {
                myChart.setOption({
                    legend: {
                        x: 'center',//图例位置
                        //图例的名称
                        //此处的数据必须和关系网类别中name相对应
                        z:"bottom",
                        data: result.categories.map(function (a) {
                            return a.name;
                        })
                    },

                    series: [
                        {
                            type: 'graph',
                            layout: 'force',
                            label: {
                                normal: {
                                    show:true,
                                    position: 'right'
                                }
                            },

                            draggable: true,
                            // progressiveThreshold: 700,
                            force: {
                                layoutAnimation:true,
                                // xAxisIndex : 0, //x轴坐标 有多种坐标系轴坐标选项
                                // yAxisIndex : 0, //y轴坐标
                                gravity:0.03,  //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
                                edgeLength: 55,  //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
                                repulsion: 150  //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
                            },
                            data: result.nodes.map(function (node) {
                                return {
                                    category: node.serial,
                                    id: node.id,
                                    name: node.label,
                                    symbolSize: node.size
                                    // itemStyle: {
                                    //     normal: {
                                    //         color: node.color
                                    //     }
                                    // }
                                };
                            }),
                            edges: result.edges.map(function (edge) {
                                return {
                                    source: edge.sourceID,
                                    target: edge.targetID
                                };
                            }),
                            roam: true,
                            focusNodeAdjacency: true,
                            categories: result.categories
                        }
                    ]
                }, true);
                // return false;
            }
        });
    });

});

