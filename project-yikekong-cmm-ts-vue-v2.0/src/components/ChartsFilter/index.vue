<template>
  <div class="charts-filter">
    <div
      class="title"
      v-if="isTypeSelectShow"
    >指标：</div>
    <el-select
      v-if="isTypeSelectShow"
      v-model="typeValue"
      placeholder="请选择"
      size="small"
      @change="selectChange"
    >
      <el-option
        v-for="item in typeList"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select>

    <div class="title ml10">时间：</div>
    <div class="time">

      <div
        v-for="(item,index) in timeList"
        :key="index"
        :class="{'item':true, 'active':index == activeIndex}"
        @click="itemClick(index,$event)"
      >{{item.label}}</div>
    </div>

  </div>
</template>

<script lang="ts">
import echarts, { EChartOption } from "echarts";
import { Component, Prop, Emit } from "vue-property-decorator";
import { mixins } from "vue-class-component";

@Component({
  name: "ChartsFilter"
})
export default class extends mixins() {
  @Prop({ default: true }) private isTypeSelectShow!: boolean;
  @Prop({ default: '' }) private mark!: string;
  private activeIndex = 2;
  private timeList = [
    {
      value: 0,
      label: "1小时"
    },
    {
      value: 1,
      label: "1天"
    },
    {
      value: 2,
      label: "7天"
    }
  ];
  private typeValue = "";
  private typeList = [
    {
      value: "选项1",
      label: "黄金糕"
    },
    {
      value: "选项2",
      label: "双皮奶"
    },
    {
      value: "选项3",
      label: "蚵仔煎"
    },
    {
      value: "选项4",
      label: "龙须面"
    },
    {
      value: "选项5",
      label: "北京烤鸭"
    }
  ];

  @Emit("timeCallback")
  timeCallback() {
    return {type:this.timeList[this.activeIndex].value, mark: this.mark};
  }

  @Emit("typeCallback")
  typeCallback() {
    return this.typeValue;
  }

  private itemClick(index: number, event: any) {
    this.activeIndex = index;
    this.timeCallback();
  }

  private selectChange(res: any) {
    console.log(res);
    this.typeCallback();
  }
}
</script>
<style lang="scss" scoped>
.charts-filter {
  display: flex;
  align-items: center;
  .title {
    font-size: 12px;
    margin-right: 10px;
  }
  .ml10 {
    margin-left: 10px;
  }
  .time {
    display: flex;
    align-items: center;
    .item {
      box-sizing: border-box;
      height: 26px;
      line-height: 26px;
      border: 1px solid #bfbfbf;
      padding: 0 10px;
      margin-left: -1px;
      text-align: center;
      color: #999999;
      font-size: 14px;
      background-color: #ffffff;
      cursor: pointer;
      &:first-child {
        margin-left: 0px;
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
      }
      &:last-child {
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
      }
    }
    .active {
      border-color: #007aff;
      color: #007aff;
      z-index: 1;
    }
  }
}
</style>