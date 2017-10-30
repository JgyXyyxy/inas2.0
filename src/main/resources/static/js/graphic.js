var myChart = echarts.init(document.getElementById('select_area'));
//
// myChart.showLoading();
// $.getJSON('/graphic.json', function (data) {
//     myChart.hideLoading();
//     myChart.setOption(option = {
//         title: {
//             text: 'Object Dependencies'
//         },
//         animationDurationUpdate: 1500,
//         animationEasingUpdate: 'quinticInOut',
//         series : [
//             {
//                 type: 'graph',
//                 layout: 'none',
//                 // progressiveThreshold: 700,
//                 data: data.nodes.map(function (node) {
//                     return {
//                         x: node.x,
//                         y: node.y,
//                         id: node.id,
//                         name: node.label,
//                         symbolSize: node.size,
//                         itemStyle: {
//                             normal: {
//                                 color: node.color
//                             }
//                         }
//                     };
//                 }),
//                 edges: data.edges.map(function (edge) {
//                     return {
//                         source: edge.sourceID,
//                         target: edge.targetID
//                     };
//                 }),
//                 label: {
//                     emphasis: {
//                         position: 'right',
//                         show: true
//                     }
//                 },
//                 roam: true,
//                 focusNodeAdjacency: true,
//                 lineStyle: {
//                     normal: {
//                         width: 0.5,
//                         curveness: 0.3,
//                         opacity: 0.7
//                     }
//                 }
//             }
//         ]
//     }, true);
// });
//
// function getValue(str) {
//     var map = eval("(" + str + ")");
// }


$(function(){
    $.ajax({
        type : "get",
        async : true,
        url : "/graphic.do",
        data : {},
        dataType : "json",
        success : function(result) {
            console.log(result);
            myChart.setOption({
                title: {
                    text: 'Object Dependencies'
                },
                animationDurationUpdate: 1500,
                animationEasingUpdate: 'quinticInOut',
                series : [
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
                            emphasis: {
                                position: 'right',
                                show: true
                            }
                        },
                        roam: true,
                        focusNodeAdjacency: true,
                        lineStyle: {
                            normal: {
                                width: 0.5,
                                curveness: 0.3,
                                opacity: 0.7
                            }
                        }
                    }
                ]
            }, true);
        }
    });
});

