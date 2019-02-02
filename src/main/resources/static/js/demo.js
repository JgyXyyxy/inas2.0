var myChart = echarts.init(document.getElementById('select_area'));
option = {
    title: {
        text: 'Graph 简单示例'
    },
    tooltip: {
        formatter: function (x) {
            return x.data.label.split("||").join("<br/>");
        }//设置提示框的内容和格式 节点和边都显示name属性
    },
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    series : [
        {
            type: 'graph',
            layout: 'none',
            symbolSize: 50,
            roam: true,
            label: {
                normal: {
                    show: true
                }
            },
            edgeSymbol: ['circle', 'arrow'],
            edgeSymbolSize: [4, 10],
            edgeLabel: {
                normal: {
                    textStyle: {
                        fontSize: 20
                    }
                }
            },
            data: [{
                id: 'O1',
                label:'',
                x: 300,
                y: 300
            }, {
                id: 'O2',
                label:'',
                x: 500,
                y: 300
            }, {
                name: 'A',
                label:'时间: 公元200年 ||地点：未知  || 描述: 俘虏并善待关羽 ',
                x: 700,
                y: 300
            },{
                name: 'B',
                label:'时间: 公元200年10月 || 地点: 官渡 || 描述: 获得了官渡之战的胜利 || 影响: 为统一北方奠定了基础 \n',
                x: 900,
                y: 300
            },{
                id: 'O3',
                label:'',
                x: 1100,
                y: 300
            },{
                id: 'E1',
                label:'',
                x: 300,
                y: 600

            },{
                name: 'N',
                label:'时间: 公元200年 || 描述: 被迫投向于曹操',
                x: 500,
                y: 600
            },{
                name: 'M',
                label:'时间: 公元208年10月 || 地点: 华容道 || 描述: 私下放走了曹操',
                x: 700,
                y: 600
            }, {
                id: 'E2',
                label:'',
                x: 900,
                y: 600
            }],
            // links: [],
            links: [{
                source: 'O1',
                target: 'O2'
            }, {
                source: 'O2',
                target: 'A'
            },{
                source: 'A',
                target: 'B'
            }, {
                source: 'B',
                target: 'O3'
            },{
                source: 'E1',
                target: 'N'
            }, {
                source: 'N',
                target: 'M'
            }, {
                source: 'M',
                target: 'E2'
            },{
                source: 'A',
                label:'关联描述：积极作用',
                target: 'M',
                lineStyle: {
                    normal: {
                        width: 5,
                        curveness: 0.2
                    }
                }

            }
            ]

        }
    ]
};

myChart.setOption(option);