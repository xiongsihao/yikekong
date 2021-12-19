<template>
  <div class="tabsCont">
    <div class="contBox">
      <div @click="handleClick(tabsData[0].url)" :class="['labTit', {active: selected == tabsData[0].url}]">{{tabsData[0].label}}</div>
      <div @click="handleClick(tabsData[1].url)" :class="['labTit', {active: selected == tabsData[1].url}]">{{tabsData[1].label}}</div>
    </div>
  </div>
</template>

<script lang="ts">
/**
 * 本组件只适用于双个标签
 * tabsData
 * label  名称
 * url 点击跳转的地址
 * selected 默认选中
 */

import echarts, { EChartOption } from "echarts";
import { Component, Prop } from "vue-property-decorator";
import { mixins } from "vue-class-component";

@Component({
  name: "Table"
})
export default class extends mixins() {
  @Prop({ default: [] }) private tabsData!: any;
  private selected = "";
  private handleClick(tab: string) {
    if (this.$route.path != tab) {
      this.$router.push(tab)
    }
  }

  mounted() {
    this.selected = this.$route.path
  }
}
</script>
<style lang="scss" scoped>
.tabsCont{
  width: 100%;
  display: flex;
  justify-content: center;
  .contBox{
    position: relative;
    background: #fff;
    border-radius: 21px;
    // padding: 8px 21px 9px;
    text-align: center;
    display: flex;
    justify-content: center;
    .labTit{
      cursor: pointer;
      line-height: 25px;
      padding: 8px 21px 9px;
    }
    .active{
      position: relative;
      line-height: 25px;
      left:2px;
      bottom: 4px;
      color: #fff;
      background:linear-gradient(right, #6EC1FF, #5584FF);
      border-radius: 17px 0px 17px 17px
    }
    .activeB{
      position: relative;
      line-height: 25px;
      left:2px;
      bottom: 4px;
      color: #fff;
      background:linear-gradient(right, #6EC1FF, #5584FF);
      border-radius: 0 17px 17px 17px
    }
  }
  
}
</style>