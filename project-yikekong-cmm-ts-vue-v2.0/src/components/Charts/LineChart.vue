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
  name: 'LineChart'
})
export default class extends mixins(ResizeMixin) {
  @Prop({ required: true }) private chartData!: any
  @Prop({ default: 'chart' }) private className!: string
  @Prop({ default: '100%' }) private width!: string
  @Prop({ default: '340px' }) private height!: string

  private nameList = []

  // @Watch('chartData', { deep: true })
  // onChartDataChange(value: any) {
  //     this.chartsDataes()  
  //     this.setOptions(value) 
  // }

  mounted() {
    setTimeout(() => {
      this.$nextTick(() => {
        this.initChart()
      })
      }, 10)
  }

  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  }
  private chartsDataes(){
      const colorList = ['rgba(255,87,87,1)','rgba(85,203,255,1)','rgba(86,133,255,1)','rgba(87,206,158,1)','rgba(255,152,64,1)','rgba(255,87,87,1)','rgba(85,203,255,1)',]
      const shadowColorList = ['rgba(255,87,87,0.4)','rgba(85,203,255,0.4)','rgba(86,133,255,0.4)','rgba(87,206,158,0.4)','rgba(255,152,64,0.4)','rgba(255,87,87,0.4)','rgba(85,203,255,0.4)',]
      const seriesData = {
          showSymbol:false,
          itemStyle: {
            color: '#00FF5A',
          },
          lineStyle: {
            color: '#FF005A',
            width: 3,
            shadowColor: 'rgba(0,0,0,0.3)',
            shadowBlur: 10,
            shadowOffsetY: 10
          },
          smooth: true,
          type: 'line',
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        }
      let setSerise = null
      setSerise = this.chartData.series.map( (val:any) => {
        return Object.assign(val, seriesData)
      })
      setSerise = setSerise.map((element:any, index:number) => {
        element.itemStyle = {color:colorList[index]}
        element.lineStyle = {
            color: colorList[index],
            width: 4,
            shadowColor:shadowColorList[index],
            shadowBlur: 10,
            shadowOffsetY: 10
          }
        return element
      });
     this.chartData.series = setSerise
     this.nameList = Array.isArray(this.chartData.series) ? this.chartData.series.map((item:any) => item.name) : []
     
  } 
  private initChart() {
    this.chart = echarts.init(this.$el as HTMLDivElement, 'macarons')
    this.chartsDataes()
    this.setOptions(this.chartData)
  }
  

  private setOptions(chartData: any) {
    if (this.chart) {
      this.chart.setOption({
        xAxis: {
          type: 'category',
          data: chartData.xdata,
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
          data: this.nameList,
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
        // series: chartData.series
        series: chartData.series
      } as EChartOption<EChartOption.SeriesLine>)
    }
  }
}
</script>
