<template>
  <div class="pieChart">
    <div :class="className" :style="{height: height, width: width}" />
  </div>
</template>

<script lang="ts">
import echarts, { EChartOption } from "echarts";
import { Component, Prop, Watch } from "vue-property-decorator";
import { mixins } from "vue-class-component";
import ResizeMixin from "./mixins/resize";

@Component({
  name: "PieChart"
})
export default class extends mixins(ResizeMixin) {
  @Prop({ default: "chart" }) private className!: string;
  @Prop({ default: []}) private data!: Array<any>;
  @Prop({ default: "100%" }) private width!: string;
  @Prop({ default: "320px" }) private height!: string;

  // mounted() {
  //   this.$nextTick(() => {
  //     this.initChart();
  //   });
  // } 

  @Watch('data')
  onPersonChanged1(val: any, oldVal: any) {
    if (val.length > 0) {
      this.$nextTick(() => {
        this.initChart();
      });
    }
  }

  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  }

  private dataChange (){
    return this.data.map(key => key.name)
  }        

  private initChart() {
    this.chart = echarts.init(this.$el as HTMLDivElement, "macarons");
    this.chart.setOption({
      tooltip: {
        show: false,
        trigger: "item",
      },
      legend: {
        orient: 'vertical',
        right: "20",
        top: "25",
        itemWidth: 6,
        itemGap:20,
        data: this.dataChange(),
        icon:"circle",
      },
      series: [
        {
          name: '',
          type: "pie",
          radius: [16, 40],
          center: ["39%", "50%"],
          roseType: 'radius',
          label: {
            formatter: '{b} \n {d}%',
            show: false,
          },
          emphasis: {
              label: {
                  show: true
              }
          },
          data: this.data,
          itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.3)'
                  },
                normal:{
                    color:function(params:any) {
                    //自定义颜色
                    var colorList = [          
                            '#5584FF', '#9EAEC5', '#FF5757',  '#FF8C00', '#FF0000', '#FE8463',
                        ];
                        return colorList[params.dataIndex]
                      }
                }
          },
          animationEasing: "cubicInOut",
          animationDuration: 2600
        }
      ]
    } as EChartOption<EChartOption.SeriesPie>);
  }
}
</script>
<style lang="scss" scope>
.pieChart{
  position: absolute;
  left: 60px;
  top: 210px;
  z-index: 99;
  width: 320px;
  height: 135px;
  opacity: 0.92;
  background: #ffffff;
  border-radius: 6px;
  box-shadow: 0px 2px 12px 0px rgba(0,0,0,0.1); 
}
</style>
