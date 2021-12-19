<template>
  <div class="equipment-container">
    <Tabs :tabsData="tabsData" />
    <div class="seachBox">
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="设备：">
          <el-input v-model="formInline.sn" placeholder="请输入设备编号"></el-input>
        </el-form-item>
        <el-form-item label="标签：">
          <el-input v-model="formInline.tag" placeholder="请输入标签"></el-input>
        </el-form-item>
        <div class="searchBut" @click="onSubmit()"><i class="iconfont icon-sousuo"></i> 搜索</div>
      </el-form>
    </div>
    <div class="contBox">
        <div v-for="(item, ind) in tableData" :class="['item', {'active': item.status}]"  :key="ind">
           <el-switch v-model="item.status" @change="handleClick(item)" class="switch" active-color="#57CE9E" inactive-color="#AFB6CA"> </el-switch>
           <div class="topList" >
              <div class="title">
                  <div class="iconfont icon-shebei11"></div>
                  <div>{{item.deviceId}}</div>
              </div>
              <div></div>
            </div>
            <div class="labList">
                <div class="tit">设备标签：</div>
                <div class="lables"><span v-for="(it, ind) in labHandle(item.tag)" :key="ind">{{it}}</span></div>
            </div>
           </div>
      </div>
      <div class="noData" v-if="total <= 0">
          <img src="./../../assets/nodata.png" alt="" width="129">
      </div>
      <div class="waterMark"></div>
      <div class="pagination" v-if="total > 15">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[15, 30, 40, 50]"
          :page-size="pageSize"
          :current-page="page"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide } from "vue-property-decorator";
import Table from "@/components/Table/index.vue";
import Tabs from "@/components/Tabs/index.vue";
import { getDevice, deviceStatus } from "@/api/equipment";

@Component({
  name: "equipment",
  components: {
    Table,
    Tabs
  }
})
export default class extends Vue {
  private formInline: {[index:string]: any} = {};
  private page: number = 1;
  private pageSize: number = 15;
  private total: number = 1;
  private tableData: any = [];
  private tableColumnData = [];

  private tabsData: object = [
    {
      label: "设备管理",
      url: "/equipment/list",
      selected: true
    },
    {
      label: "指标管理",
      url: "/fingerMark/list"
    }
  ];

  private async getList() {
    const data: any = await getDevice({
      page: this.page,
      pageSize: this.pageSize,
      ...this.formInline
    });
    this.tableData = [...data.items];
    this.total = data.counts;
  }
 //查询
  private onSubmit() {
    this.page = 1
    this.getList();
  }
  //重置表单
  private resetForm(refId: any) {
    (this as any).$refs[refId].resetFields();
    // this.getList();
  }

  private labHandle(item:string){
    const list = item ? item.split(',') : []
    return list
  }

  private async handleClick(row: any) {
    const data: any = await deviceStatus({
      sn: row.deviceId,
      status: row.status
    });
    if (!data) {
      this.$message.error(data.message || '请求失败')
    }
  }

  private handleSizeChange(val: any) {
    this.pageSize = val;
    this.getList();
  }
  private handleCurrentChange(val: any) {
    this.page = val;
    this.getList();
  }

  mounted() {
    this.getList();
  }
}
</script>
<style lang="scss">
.equipment{
   &-container{
      .el-form--inline .el-form-item__content {
        width: calc(100% - 60px);
      }
   }
} 
</style>
<style lang="scss" scoped>
.equipment{
  &-container{
    min-height: calc(100vh - 66px);
    background: #f3f4f7;
    padding: 28px 35px;
    background: #f3f4f7 url(./../../assets/img/waterMarkEquipmentTop.png) calc(100% - 70px) 100px no-repeat;
    background-size:156px 78px; 
    .seachBox{
      padding-left: 10px;
      margin-top: 20px;
      .searchBut{
        display: inline-block;
        position: relative;
        left: -20px;
        width: 89px;
        height: 40px;
        line-height: 38px;
        cursor: pointer;
        text-align: center;
        background: #ffffff;
        border: 1px solid #5584ff;
        border-radius: 4px;
        color: #5584ff;
      }
    }
    .contBox{
      display: flex;
      flex-wrap: wrap;
      .item{
        position: relative;
        margin: 20px 10px;
        width: calc(20% - 20px);
        height: 120px;
        padding: 0 15px;
        background: #ffffff;
        border-radius: 11px;
        .topList{
          display: flex;
          justify-content: space-between;
          .title{
            display: flex;
            position: relative;
            font-size: 14px;
            font-weight: 300;
            text-align: left;
            color: #afb6ca;
            line-height: 25px;
            .iconfont{
              position: relative;
              margin-right: 10px;
              top: -7px;
              display: inline-block;  
              width: 38px;
              height: 29px;
              line-height: 29px;
              font-size: 18px;
              background: #9eaec5;
              border-radius: 0px 12px 12px 12px;
              box-shadow: 0px 2px 5px 0px;
              color: #fff;
              text-align: center;
            }
          }
        }
        .labList{
          display: flex;
          margin-top: 10px;
          .tit{
            width: 60px;
            min-width: 60px;
            line-height: 23px;
            font-size: 12px;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #afb6ca;
          }
          .lables{
            height: 76px;
            overflow: hidden;
            span{
              display: inline-block;
              text-align: center;
              font-size: 12px;
              padding:0 6px;
              line-height: 18px;
              background: rgba(255,117,42, 0.25);
              color: rgba(255,117,42, 1);
              border-radius: 4px;
              margin:0 2px 7px;
            }
            span:nth-child(2){
              background: rgba(255,138,138, 0.25);
              color: rgba(255,138,138,1);
            }
            span:nth-child(3){
              background: rgba(83,138,217,0.25);
              color: rgba(83,138,217,1);
            }
            span:nth-child(4){
              background: rgba(109,177,137,0.25);
              color: rgba(109,177,137,1);
            }
            span:nth-child(5){
              background: rgba(92,177,172,0.25);
              color: rgba(92,177,172,1);
            }
            span:nth-child(6){
              background: rgba(190,160,93,0.25);
              color: rgba(190,160,93,1);
            }
            span:nth-child(7){
              background: rgba(139,155,178,0.25);
              color: rgba(139,155,178,1);
            }
            span:nth-child(8){
              background: rgba(236,173,135,0.25);
              color: rgba(236,173,135,1);
            }
          }
        }
      }
      .active .iconfont{
        background: linear-gradient(right, #6EC1FF, #5584FF) !important;
      }
    }
    @media screen and(max-width:1366px){
      .item{
        width: calc(25% - 20px);
      }
    }
    &:after{
      content: '';
      position: absolute;
      bottom: 0px;
      left:0px;
      width: 149px;
      height: 128px;
      background: url(./../../assets/img/waterMarkEquipmentBot.png) center center no-repeat;
      background-size: 149px 128px; 
    }
  }
}
</style>
