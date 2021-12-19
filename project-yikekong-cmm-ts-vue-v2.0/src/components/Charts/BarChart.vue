<template>
  <div
    :class="className"
    :style="{height: height, width: width}"
  />
</template>

<script lang="ts">
import echarts, { EChartOption } from "echarts";
import { Component, Prop, Watch } from "vue-property-decorator";
import { mixins } from "vue-class-component";
import ResizeMixin from "./mixins/resize";

const animationDuration = 6000;

@Component({
  name: "BarChart"
})
export default class extends mixins(ResizeMixin) {
  @Prop({ default: "chart" }) private className!: string;
  @Prop({ default: "100%" }) private width!: string;
  @Prop({ default: "300px" }) private height!: string;
  @Prop({ default: () => {}}) private chartData!: Array<any>;

  @Watch('chartData')
  onChangeValue(value:any, oldVal:any) {
    this.$nextTick(() => {
      this.initChart();
    });
  }

  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  } 

  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  }
  
  private initChart() {
    let data = this.chartData
    
    this.chart = echarts.init(this.$el as HTMLDivElement, "macarons");
    this.chart.setOption({
      tooltip: {
        formatter: function (param:any) {
            param = param[0]
            return ` ${param['name']}<br/> <span style="color:#ff5757;">●</span> ${(data as any).nameList[(param as any).dataIndex]} : ${(param as any).value}`;
        },
        trigger: 'axis',
        axisPointer:{
          lineStyle:{
            width: 0
          }
        },
        backgroundColor: 'rgb(255, 255, 255, 0.8)',
        shadowColor: 'rgb(0, 0, 0, 0.8)',
        shadowBlur: 10,
        padding: 10,
        textStyle:{
          color: '#666',
        },
        extraCssText: 'box-shadow: 0 0 10px rgba(150, 150, 150, 0.4)'
      },
      grid: {
        left: 10,
        right: 30,
        bottom: 20,
        top: 20,
        containLabel: true
      },
      xAxis: [{
        type: "value",
        axisTick: {
          alignWithLabel: true
        },
        splitLine:{
          lineStyle:{
            type: 'dashed'
          },
          show:true
        }
      }],
      yAxis: [
        {
          type: "category",
          data: (this.chartData as any).YData,
          lineStyle:{
            type: 'dashed'
          },
        }
      ],
      series: [
        {
          name: '',
          type: "bar",
          barCategoryGap: '8',
          barWidth: '35%',
          itemStyle:{
            normal: {
                    color: new echarts.graphic.LinearGradient(
                        1, 0, 0, 0,
                        [
                            {offset: 0, color: '#FF5555'},
                            {offset: 1, color: '#FF7E62'}
                        ]
                    )
            },
          },
          data: (this.chartData as any).XData,
          animationDuration
        },
      ]
    } as EChartOption<EChartOption.SeriesBar>);
  }
}
</script>
