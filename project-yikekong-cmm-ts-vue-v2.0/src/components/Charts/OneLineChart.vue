<template>
  <div
    :class="className"
    :style="{height: height, width: width}"
  />
</template>

<script lang="ts">
import echarts, { EChartOption } from 'echarts'
import { Component, Prop, Watch } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'
import ResizeMixin from './mixins/resize'

@Component({
  name: 'OneLineChart'
})

export default class extends mixins(ResizeMixin) {
  @Prop({ required: true }) private chartData!: any
  @Prop({ default: 'chart' }) private className!: string
  @Prop({ default: '100%' }) private width!: string
  @Prop({ default: '340px' }) private height!: string

  @Watch('chartData', { deep: true })
  private onChartDataChange(value: any) {
    this.setOptions(value)
  }

  mounted() {
    setTimeout(() => {
      this.$nextTick(() => {
        this.initChart()
      })
    }, 500)
  }

  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  }

  private initChart() {
    this.chart = echarts.init(this.$el as HTMLDivElement, 'macarons')
    this.setOptions(this.chartData)
  }

  private setOptions(chartData: any) {
    if (this.chart) {
      this.chart.setOption({
        xAxis: {
          type: 'category',
          data: chartData['xdata'],
        },
        yAxis: {
          splitLine:{
            lineStyle:{
              type: 'dashed'
            },
            show:true
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 30,
          top: 20,
          containLabel: true
        },
        legend: {
          bottom: 0,
          itemWidth: 6,
          itemGap:20,
          icon:"circle",
          data: ['异常趋势'],
        },
        tooltip: {
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
        series: [{
          name: '异常趋势',
          showSymbol:false,
          itemStyle: {
            color: '#FF005A',
          },
          lineStyle: {
            color: '#FF005A',
            width: 3,
            shadowColor: 'rgba(222,115,127,0.5)',
            shadowBlur: 10,
            shadowOffsetX: 10,
            shadowOffsetY: 10
          },
          areaStyle: {
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(222,115,127,0.23)' },
                  {offset: 0.5, color: 'rgba(255,255,255,0.23)'}]
                )
            }
          },
          smooth: true,
          type: 'line',
          data: chartData['series'],
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        }
        ]
      } as any)
    }
  }
}
</script>
