<template>
  <div class="addDishBoard-container">
    <div class="topHander">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>添加看板</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="content">
      <el-form :model="form" label-width="100px" style="padding:10px; position: relative;" ref="form" label-position="left" >
        <el-form-item label="看板名称：" prop="name" >
          <el-input v-model="form.name" size="small" placeholder="请输入看板名称" ></el-input>
        </el-form-item>
        <div class="fromRow">
          <el-form-item label="选择指标" prop="quota"  >
            <el-select
              v-model="form.quota"
              filterable
              placeholder="指标（单选）"
              size="small"
              class="select-box"
              @change="getQuotaDevices"
            >
              <el-option
                v-for="item in quotaNumberQuotaArr"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <div class="fromCol">
            <el-select v-model="form.deviceIdList" filterable multiple placeholder="设备（多选）" size="small" :multiple-limit=5 class="select-box" >
              <el-option v-for="item in quotaDevicesArr" :key="item" :label="item" :value="item" />
            </el-select>
          </div>
          
        </div>  
        <div class="butLook" @click="lookHandle"> 预览 </div> 
      </el-form>
      <div class="chart-box" v-if="chartShow">
        <div class="chartTop">
         <div>
          <ChartsFilter  @timeCallback="timeCallback" mark='look' @typeCallback="typeCallback" :isTypeSelectShow="false" />
        </div>
        </div>
        <line-chart :chart-data="lineChartData" :key="restKey" width="100%" height="300px" />
      </div>

      <div class="submit">
        <el-button @click="cancel" size="small">取消</el-button>
        <el-button type="primary" size="small" @click="onSubmit" style="margin-left:30px" >确定</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import ChartsFilter from "@/components/ChartsFilter/index.vue";
import BarChart from "@/components/Charts/BarChart.vue";
import LineChart from "@/components/Charts/LineChart.vue";
import {
  boardSystemBoard,
  quotaNumberQuota,
  quotaDevices,
  board,
  addBoard,
  preview,
} from "@/api/dashboard";

@Component({
  name: "addDashboard",
  components: {
    ChartsFilter,
    BarChart,
    LineChart
  }
})

export default class extends Vue {
  private form = {
    adminId: '1',
    name: '',
    quota: '',
    deviceIdList: [],
    system: false
  };

  private type = 3;
  private chartShow = false;
  private restKey = 0
  private lineChartData:any[] = []

  //指标
  private quotaNumberQuotaArr: any = [];
  //设备
  private quotaDevicesArr: any = [];

  mounted() {
    this.getQuotaNumberQuota();
  }

  // 预览
  async lookHandle(type?:number){
    if (!this.verification()){
      return 
    }
    const start = (this as any).moment().subtract('days', 6).format('YYYY-MM-DD 00:00:00')
    const end = (this as any).moment().format('YYYY-MM-DD 23:59:59')
    const oneHourSt = (this as any).moment().subtract('hour', 1).format('YYYY-MM-DD HH:mm:ss')
    const oneHourEt = (this as any).moment().format('YYYY-MM-DD HH:mm:ss')
      // 1天的时间
    const oneDaySt = (this as any).moment().format('YYYY-MM-DD 00:00:00')
    const oneDayEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
      // 7天的时间
    const DayTimeSt = (this as any).moment().subtract('days', 6).format('YYYY-MM-DD 00:00:00')
    const DayTimeEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
    let params = {...this.form,start:DayTimeSt,end:DayTimeEt,type:'3', quotaId:this.form.quota}
    if (type == 0) {
      params = {...this.form,start:oneHourSt,end:oneHourEt,type:'1', quotaId:this.form.quota}
    } 
    if(type == 1){
      params = {...this.form,start:oneDaySt,end:oneDayEt,type:'2', quotaId:this.form.quota}
    }
    await preview(params).then((res: any) => {
        if (res.xdata) {
          res.name = this.form.name
          this.lineChartData = res
          this.chartShow = true
          this.restKey++
        } else {
          this.$message.error('所选设备暂时没有数据可预览！')
        }
    })
    
  }

  async onSubmit() {
    if (!this.verification()){
      return 
    }
    const params = {...this.form, device:this.form.deviceIdList.join(',')}
    await addBoard(params).then((res: any) => {
        if (res) {
          this.$message.success('看板添加成功')
          this.cancel()
        }
    })
  }

  verification(){
    const verData = this.form
    if (!verData.name || verData.name.trim() == '') {
      this.$message.error('请输入看板名称')
      return false
    }
    if (!verData.quota || String(verData.quota).trim() == '') {
      this.$message.error('请选择指标')
      return false
    }
    if (verData.deviceIdList.length == 0) {
      this.$message.error('请选择设备')
      return false
    }
    return true
  }

  private cancel() {
    this.$router.push("/");
  }
  
  private typeCallback(res: any) {
    this.type = res
  }

  private timeCallback({type, mark}:any){
    this.lookHandle(type)
  }

  //获取指标
  async getQuotaNumberQuota() {
    await quotaNumberQuota().then((res:any) => {
      this.quotaNumberQuotaArr = [...res.items];
    })
    
  }

  //获取设备
  async getQuotaDevices() {
    this.form.deviceIdList = []
    await quotaDevices({ quotaId: this.form.quota,page:1, pageSize: 1000 }).then((res:any) => {
      this.quotaDevicesArr = [...res.items];
    })
  }
}
</script>
<style lang="scss">
.addDishBoard-container{
  .el-breadcrumb{
    font-size: 16px;
  }
  .el-button--small{
    width: 88px;
    height: 36px;
  }
}

</style>
<style lang="scss" scoped>
.addDishBoard{
  &-container{
    height: calc(100vh-68px);
    
    .topHander{
      margin: 0 41px;
      padding:27px 0px 15px 0px;
    }
    .content{
      margin: 0 41px;
      padding: 15px 40px;
      background: #ffffff;
      border-radius: 8px;
      .fromRow{
        display: flex;
      }
      .fromCol{
        position: relative;
        margin-left: 20px;
        top: 2px;
      }
      .chart-box{
        .chartTop{
          display: flex;
          justify-content: space-between;
        }
      }
      .submit{
        text-align: center;
        padding:10px;
        margin-top: 20px;
      }
      .butLook{
        position: absolute;
        right: 10px;
        bottom: 34px;
        width: 88px;
        line-height: 34px;
        background: #ffffff;
        border: 1px solid #5584ff;
        border-radius: 4px;
        text-align: center;
        font-size: 14px;
        color: #5584FF;
        cursor: pointer;
      }
    }
  }
}
</style>
