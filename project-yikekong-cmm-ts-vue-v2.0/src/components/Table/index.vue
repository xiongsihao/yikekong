<template>
  <div>
    <el-table
      :data="tableData"
      :border="isBorder"
      size="medium"
    >
      <el-table-column
        v-if="isIndex"
        type="index"
        label="序号"
        width="100"
        align="center"
      >
      </el-table-column>
      <el-table-column
        v-for="(item,index) in tableColumnData"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div>{{ getScopeRowItem(scope,item)  }}</div>
        </template>
      </el-table-column>
      <slot name="operating"></slot>
    </el-table>
  </div>
</template>

<script lang="ts">
import echarts, { EChartOption } from "echarts";
import { Component, Prop } from "vue-property-decorator";
import { mixins } from "vue-class-component";

@Component({
  name: "Table"
})
export default class extends mixins() {
  @Prop({ default: [] }) private tableData!: any;
  @Prop({ default: [] }) private tableColumnData!: any;
  @Prop({ default: false }) private isBorder!: boolean;
  @Prop({ default: false }) private isIndex!: boolean;

  private getScopeRowItem(scope: any, item: any) {
    return scope.row[item.prop] ? scope.row[item.prop] : item.defaults;
  }
}
</script>
<style lang="scss">
.el-table th {
  background: #78a8ef;
}
.el-table thead {
  color: #ffffff;
}
</style>
