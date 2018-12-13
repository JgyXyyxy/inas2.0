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
                y: 400
            }, {
                id: 'O2',
                label:'',
                x: 500,
                y: 300
            }, {
                name: 'B',
                label:'time point: AD 200 || description: Captive Guan Yu and generous treatment.',
                x: 700,
                y: 300
            },{
                id: 'O4',
                label:'',
                x: 900,
                y: 300
            },{
                id: 'B1',
                label:'',
                x: 300,
                y: 600,
                itemStyle: {
                    color: 'blue'
                }
            },{
                id: 'B2',
                label:'',
                x: 500,
                y: 600
            },{
                name: 'A',
                label:'time point: AD 200 || description: Forced to surrender to Cao Cao',
                x: 700,
                y: 600
            }, {
                name: 'C',
                label:'time point: October, AD 208 || place: Huarong Road || description: took the initiative to let Cao Cao escape',
                x: 900,
                y: 600
            },{
                name: 'Node 1',
                label:'',
                x: 300,
                y: 1000
            },{
                name: 'Node m',
                label:'time point: October, AD 200 || place: Guandu || description: The victory of the battle of Guandu || results and impact: Laid the foundation for the reunification \n' +
                '\t\tof the north\n',
                x: 500,
                y: 1000
            },{
                name: 'Node n',
                label:'',
                x: 700,
                y: 1000
            },

            ],
            // links: [],
            links: [{
                source: 'O1',
                target: 'O2'
            }, {
                source: 'O2',
                target: 'B'
            },{
                source: 'B',
                target: 'O4'
            }, {
                source: 'B1',
                target: 'B2'
            },{
                source: 'B2',
                target: 'A'
            }, {
                source: 'A',
                target: 'C'
            },{
                source: 'O1',
                label:'association type: Positive impact',
                target: 'C',
                lineStyle: {
                    normal: {
                        width: 5,
                        curveness: 0.2
                    }
                }

            },{
                source:'Node 1',
                target:'Node m'
            },{
                source:'Node m',
                target:'Node n'
            }
            ]

        }
    ]
};

myChart.setOption(option);