<template>
  <div class="dashboard-container" @click.stop="() =>  boardShow ? boardShow = false : null">
    <div class="mapCont">
      <div class="title">设备分布</div>
      <div class="map">
        <BaiduMap :data= "mapData" @bmMarkerCallback="bmMarkerCallback" />
      </div>
      <totleCont :data="monitor"></totleCont>
      <PieChart :data="statusCollectData" />
    </div>
    <div class="content">
      <div :class="{listCont:true, wtA:true , bigWit: !boardSystemBoard.some(it => it.id == 3)}" v-if="boardSystemBoard.some(it => it.id == 2)">
        <div>
          <span class="closeBox iconfont icon-shanchu1" @click="delBoardHandle(2)"></span>
          <div class="title"><span>异常设备(今天)</span><span class="mon" @click="() => $router.push('/alarmLog/list')">更多></span></div>
          <div class="table">
            <div class="head">
              <span  style="width:200px; flex:none">报警时间</span>
              <span>报警名称</span>
              <span  style="width:120px; flex:none">等级</span>
              <span>报警设备</span>
              <span style="width:80px; flex:none">数据</span>
            </div>
            <div class="conttent" style="height: 200px;overflow: scroll;">
              <div class="trLine" v-for="it in tableData" :key="it.name">
                  <span style="width:200px; flex:none">{{it.time}}</span>
                  <span>{{it.alarmName}}</span>
                  <span v-if="it.level == 2" style="width:120px; flex:none"><i>严重</i></span>
                  <span v-if="it.level == 1" style="width:120px; flex:none"><i class="pub">一般</i></span>
                  <span>{{it.deviceId}}</span>
                  <span style="width:80px; flex:none">{{it.online ? `${it.value}${it.unit}` : '离线'}}</span>  
              </div>
              <div v-if="tableData.length == 0" style="line-height: 200px;text-align: center;color: #999;">暂无异常设备报警！</div>
            </div>
          </div>
        </div>
      </div>
      <div :class="{listCont:true, wtB:true , bigWit: !boardSystemBoard.some(it => it.id == 2)}" v-if="boardSystemBoard.some(it => it.id == 3)">
        <span class="closeBox iconfont icon-shanchu1" @click="delBoardHandle(3)"></span>
        <div class="title">
            <span>异常趋势（单位：个）</span>
            <ChartsFilter @timeCallback="timeCallback" mark='trend' @typeCallback="typeCallback" :isTypeSelectShow="false" />
          </div>
          <div class="cont">
            <one-line-chart :chart-data="trendChartDatas" :key="chartRest" width="100%" height="250px" />
          </div>
      </div>
      <div :class="{listCont:true, bigWit: unSystemBoardList.length == 0}" v-if="boardSystemBoard.some(it => it.id == 4)">
        <span class="closeBox iconfont icon-shanchu1"  @click="delBoardHandle(4)"></span>
        <div class="title">
          <span>异常数量Top10(单位：个）</span>
          <ChartsFilter @timeCallback="timeCallback" mark='top' @typeCallback="typeCallback" :isTypeSelectShow="false" />
        </div>
        <div class="cont">
          <bar-chart :key="restTop"  :chartData="top10Data" width="100%" height="270px" />
        </div>
      </div>
      <div v-for="(item, ind) in unSystemBoardList" :key="ind" :class="{listCont:true, bigWit: unSystemBoardList.length % 2 == 0 && ind+1 == unSystemBoardList.length}">
          <span class="closeBox iconfont icon-shanchu1"  @click="delBoardHandle(item.id)"></span>
          <div class="title">
            <span>{{item.name}}</span>
            <ChartsFilter @timeCallback="timeCallback" @typeCallback="typeCallback" :mark="item.id" :isTypeSelectShow="false" />
          </div>
          <div class="cont" :key="restKey" v-if="item.chartsData">
            <line-chart :key = restKeyList[ind] :chart-data="item.chartsData" width="100%" height="240px" />
          </div>
          <div class="cont"  v-if="!item.chartsData" style="    text-align: center; line-height: 200px;">
            暂无数据！
          </div>
        </div>
    </div>
    <div class="addDashboard">
      <div class="addBut" @click.stop="() => boardShow = true">编辑看板
      <div class="dishboardSelect" v-show="boardShow">
        <span @click="openAddBoard">编辑看板</span>
        <span @click="() => $router.push('/addDashboard')">+添加看板</span>
      </div>
      </div>
    </div>
    <el-dialog
      :visible.sync="dialogVisible"
      width="345px"
      center
    >
      <div
        slot="title"
        class="dialogTitle"
      >编辑看板</div>
        <el-checkbox-group v-model="checkList" class="checkbox">
          <el-checkbox label="1" disabled>设备分布</el-checkbox>
          <el-checkbox label="2">异常设备</el-checkbox>
          <el-checkbox label="3">异常趋势</el-checkbox>
          <el-checkbox label="4">异常TOP10</el-checkbox>
        </el-checkbox-group>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="addBoardHanle"
          size="small"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import BaiduMap from "@/components/BaiduMap/index.vue";
import totleCont from "./components/totleCont.vue";
import BarChart from "@/components/Charts/BarChart.vue";
import LineChart from "@/components/Charts/LineChart.vue";
import OneLineChart from "@/components/Charts/OneLineChart.vue";
import PieChart from "@/components/Charts/PieChart.vue";
import Table from "@/components/Table/index.vue";
import ChartsFilter from "@/components/ChartsFilter/index.vue";
import mqtt from 'mqtt';

import {
  boardSystemBoard,
  monitor,
  quotaNumberQuota,
  statusCollect,
  deviceList,
  deviceInfo,
  trendDatas,
  boardList,
  boardData,
  top10Alarm,
  delBoard,
  getUnSystemPanel,
  addBoard,
  boardStatus,
} from "@/api/dashboard.ts";

@Component({
  name: "Dashboard",
  components: {
    BaiduMap,
    BarChart,
    totleCont,
    LineChart,
    OneLineChart,
    PieChart,
    Table,
    ChartsFilter,
  }
})

export default class extends Vue {
  private lineChartData = {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  };
  private tableData: any[] = [];
  private boardShow:boolean = false;
  private monitor:string[] = [];
  private statusCollectData: any[] = [];
  private mapData: any[] = [];
  private boardSystemBoard: any[] = []; //系统模块
  private dialogVisible:boolean = false;
  private checkList:string[] = ['1']
  private unSystemBoardList: any[] = []  //非系统面板
  private top10Data: {} = {};
  private restKeyList:any[] = [];
  private restKey = 0;
  private delAct = false;
  private checkNum = NaN;
  private chartRest = 0;
  private restTop = 0;
  private trendChartDatas: string[] = []  //异常趋势图数据
  mounted() {
     this.init() 
  }

  private init() {
    this.getMonitor() // 监控设备数 - 设备数和报警设备数量
    this.getStatusCollect() // 设备状态分布 - 首页环形图数据
    this.getIndexPanel() // 监控设备数 - 设备数和报警设备数量
    this.getDeviceList() // 地图 - 根据经纬度获取设备信息
    this.getUnSystemPanel() // 获取首页非系统面板模块
  }
 
  // 获取首页系统面板模块
  async getIndexPanel(){
    await boardSystemBoard().then((res: any) => {
      this.boardSystemBoard = res
      this.chartRest++
      res.forEach((element:any) => {
        switch(element.id){
          case 2 :{
            this.getMqttData(); //// 异常设备实时数据 今天
            break
          }
          case 3 :{
            this.getTrendData(); // 异常趋势
            break
          }
          case 4 :{
            this.getTop10() // 异常数量Top10 数据获取
            break
          }
        }
      });
    })
  }
  // 异常设备实时数据 今天
  getMqttData(){
    var client  = mqtt.connect('ws://172.17.0.231/mqtt', {
      port: 8083,
      clientId: 'webclient' + Math.floor(Math.random()*1000),
      username: 'admin',
      password: '123456',
      clean: true
    })
    client.on('connect', function () {
      client.subscribe('presence', function (err) {
        console.log('连接成功:')
        client.subscribe('/device/alarm', { qos: 1 }, (error:any) => {
          if (!error) {
            console.log('订阅成功')
          } else {
            console.log('订阅失败')
          }
        })
      })
    })
    // 接收消息处理
    client.on('message', (topic, message) => {
      // console.log('收到来自', topic, '的消息', message.toString())
      let msg:{[index:string]:any} = JSON.parse(message.toString())
      msg.time = (this as any).moment().format('YYYY-MM-DD HH:ss:mm')
      if (this.tableData.length <= 12){
          this.tableData.unshift(msg)
      } else {
        this.tableData.unshift(msg)
        this.tableData.pop()
      }
    })
    // 断开发起重连
    client.on('reconnect', (error:any) => {
      console.log('正在重连:', error)
    })
    // 链接异常处理
    client.on('error', (error:any) => {
      console.log('连接失败:', error)
    })
  }

  // 获取首页非系统面板模块
  async getUnSystemPanel(){
    await getUnSystemPanel().then((res: any) => {
      this.unSystemBoardList = res
      this.unSystemBoardList.forEach((element: any) => {
        this.getBoardData(element)
      });
      if (res.length == 0) {
        this.restTop++
      }
    })
  }
 // 自定义面板 - 用列表中的对应ID获取信息
  async getBoardData(item: any, type?: number){
    const oneHourSt = (this as any).moment().subtract('hour', 1).format('YYYY-MM-DD HH:mm:ss')
    const oneHourEt = (this as any).moment().format('YYYY-MM-DD HH:mm:ss')
      // 1天的时间
    const oneDaySt = (this as any).moment().format('YYYY-MM-DD 00:00:00')
    const oneDayEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
      // 7天的时间
    const DayTimeSt = (this as any).moment().subtract('days', 6).format('YYYY-MM-DD 00:00:00')
    const DayTimeEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
    let params = {startTime:DayTimeSt,endTime:DayTimeEt,type:'3'}
    if (type == 0) {
      params = {startTime:oneHourSt,endTime:oneHourEt,type:'1'}
    } 
    if(type == 1){
      params = {startTime:oneDaySt,endTime:oneDayEt,type:'2'}
    }
    await boardData({id:item.id, ...params}).then((res: any) => {
      if (res.xdata) {
        item.chartsData = res
        if (Number.isNaN(this.checkNum)){
          this.restKeyList.push(0)
        } else {
          this.restKeyList[this.checkNum] ++
          this.$set(this.restKeyList, this.checkNum, this.restKeyList[this.checkNum])
        }
      } else {
        item.chartsData = {name: res.name, series: [], xdata: []}
        this.$set(this.restKeyList, this.checkNum, this.restKeyList[this.checkNum])
      }
    })
    if (this.delAct && this.restKeyList.length == this.unSystemBoardList.length){
      this.restKey++
      this.delAct = false
    }
  }
 // 监控设备数 - 设备数和报警设备数量
  async getMonitor(){
    await monitor().then((res: any)  => {
      this.monitor = res
    })
  }

  // 设备状态分布 - 首页环形图数据
  async getStatusCollect(){
    await statusCollect({}).then((res: any) => {
      this.statusCollectData = res
    })
  }

  // 地图 - 根据经纬度获取设备信息
  async getDeviceList(){
    await deviceList({lon: 116.400244, lat: 39.92556, distance: 10000}).then((res: any) => {
      this.mapData = res
    })
  }

  // 地图 - 查询设备详情 - 未使用
  async getDeviceInfo(){
    await deviceInfo({}).then((res: any) => {
      // console.log(222, res)
    })
  }

  // 异常趋势图
  async getTrendData(type?:number){
    const oneHourSt = (this as any).moment().subtract('hour', 1).format('YYYY-MM-DD HH:mm:ss')
    const oneHourEt = (this as any).moment().format('YYYY-MM-DD HH:mm:ss')
      // 1天的时间
    const oneDaySt = (this as any).moment().format('YYYY-MM-DD 00:00:00')
    const oneDayEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
      // 7天的时间
    const DayTimeSt = (this as any).moment().subtract('days', 6).format('YYYY-MM-DD 00:00:00')
    const DayTimeEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
    let params = {startTime:DayTimeSt,endTime:DayTimeEt,type:'3'}
    if (type == 0) {
      params = {startTime:oneHourSt,endTime:oneHourEt,type:'1'}
    } 
    if(type == 1){
      params = {startTime:oneDaySt,endTime:oneDayEt,type:'2'}
    }
    await trendDatas(params).then((res: any) => {
      this.trendChartDatas = res
      this.chartRest++
    })
  }

  // 异常数量Top10
  async getTop10(type?:number){
    const oneHourSt = (this as any).moment().subtract('hour', 1).format('YYYY-MM-DD HH:mm:ss')
    const oneHourEt = (this as any).moment().format('YYYY-MM-DD HH:mm:ss')
      // 1天的时间
    const oneDaySt = (this as any).moment().format('YYYY-MM-DD 00:00:00')
    const oneDayEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
      // 7天的时间
    const DayTimeSt = (this as any).moment().subtract('days', 6).format('YYYY-MM-DD 00:00:00')
    const DayTimeEt = (this as any).moment().format('YYYY-MM-DD 23:59:59')
    let params = {startTime:DayTimeSt,endTime:DayTimeEt,type:'3'}
    if (type == 0) {
      params = {startTime:oneHourSt,endTime:oneHourEt,type:'1'}
    } 
    if(type == 1){
      params = {startTime:oneDaySt,endTime:oneDayEt,type:'2'}
    }
    await top10Alarm(params).then((res: any) => {
      let chartsData:{[index:string]:any} = {XData: [], YData: [], nameList: []}
      res.reverse().forEach((res:any) => {
        chartsData.nameList.push(res.deviceId)
        chartsData.XData.push(res.heapValue)
        chartsData.YData.push(res.deviceId)
      })
      this.top10Data = chartsData
      this.restTop++
    })
  }
  
  // 看板删除
 async delBoardHandle(id:number){
   this.$confirm('确认删除?', '', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(async () => {
       this.delAct = true;
       await delBoard(id).then((res: any) => {
        if(id <= 4){
          this.getIndexPanel()
        } else {
          this.restKeyList = []
          this.getUnSystemPanel()
        }
      })
    }).catch((err:any) => {
      console.log('取消操作！')
    })
  }
  // 打开编辑看板
  openAddBoard(){
    this.dialogVisible = true
    this.checkList = this.boardSystemBoard.map(res => String(res.id))
  }
  // 添加看板
  async addBoardHanle(){
    const idList = ['1','2','3','4']
    const params = idList.map((res:any) => {
      const st = this.checkList.indexOf(res) == -1 ? true : false
      return {boardId: Number(res), disable: res == '1' ? false :st}
    })
    await boardStatus(params).then((res: any) => {
        this.getIndexPanel()
        this.dialogVisible = false
    })
  }
  
  private bmMarkerCallback(res: object) {
    this.$router.push("/data/info");
  }
  // 时间切换 
  private timeCallback({type, mark}:any) {
    if (mark == 'top'){
      this.getTop10(type)
    }
    if (mark == "trend"){
      this.getTrendData(type)
    }
    if (typeof mark == 'number') {
      console.log(type, mark);
       for(let i=0; i <= this.unSystemBoardList.length; i++ ){
         if ((this.unSystemBoardList[i] as any).id == mark.toString()){
           this.getBoardData(this.unSystemBoardList[i], type)
           this.checkNum = i
           return 
         }
       }
    }
  }

  private typeCallback(res: any) {

  }

}
</script>
<style lang="scss">
.dashboard-container{
  .dialogTitle{
    color: rgba(51,51,51,1);
  }
  .el-checkbox__label{
      color:rgba(51,51,51,1) !important;
    }
    .el-dialog__body{
      padding: 25px 60px 10px;
      line-height: 22px;
    }
    .el-button--small{
      width: 88px;
      height: 36px;
    }
}
</style>
<style lang="scss" scoped>
.dashboard-container {
  min-height: calc(100vh - 66px);
  .mapCont{
    background: #fff;
    margin: 20px;
    border-radius: 8px;
    height: 573px;
    padding: 0 28px 28px 28px;
    display: flex;
    flex-direction: column;
    .title{
      line-height: 48px;
      height: 48px;
    }
    .map{
      flex: 1;
    }
  }
  .content{
    display: flex;
    flex-wrap: wrap;
    padding: 0 10px;
    .listCont{
      position: relative;
      height: 318px;
      width: calc(50% - 20px);
      background: #fff;
      margin:0 10px 20px 10px;
      padding:14px 20px;
      border-radius: 8px;
      box-shadow: 0px 2px 12px 0px rgba(0,0,0,0.1);
      .closeBox{
        display: none;
        position: absolute;
        cursor: pointer;
        color:#666666;
        right: 5px;
        top: 5px;
      }
      &:hover .closeBox{
        display: block;
      }
      .title{
        display: flex;
        justify-content: space-between;
        line-height: 32px;
        margin-bottom: 10px;
        .mon{
          font-size: 14px;
          cursor: pointer;
          color:rgba(85,132,255,1);
        }
      }
      .table{
        font-size: 14px;
        .head{
            display: flex;
            width: 100%;
            line-height: 36px;
            opacity: 0.44;
            background: #eff0f2;
            border-radius: 4px;
            margin-bottom:10px;
            padding: 0 12px;
            span{
              flex: 1;
            }
            span:last-child{
              text-align: right;
            }
        }
        .trLine{
          display: flex;
          line-height: 32px;
          padding: 0 12px;
          color: rgba(153,153,153,1);
          span{
            flex: 1;
            i{
              display: inline-block;
              width: 41px;
              height: 18px;
              color:#fff;
              font-size: 12px;
              line-height: 18px;
              text-align: center;
              font-style: normal;
              background: #ff5757;
              border-radius: 4px;
            }
            .pub{
              background: rgba(255,196,0,1);
            }
          }
          span:last-child{
            text-align: right;
          }
          &:hover{
            background: #f3f7fe;
            border-radius: 13px;
          }
        }
      }
    }
    .wtA{
      width: calc(55% - 20px) !important;
    }
    .wtB{
      width: calc(45% - 20px) !important;
    }
    .bigWit{
      width: 100% !important;
    }
  }
  .addDashboard{
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    width: calc(100% - 40px);
    margin: 0 20px;
    height: 83px;
    border: 2px dashed #ebebeb;
    border-radius: 10px;
    .addBut{
      width: 120px;
      height: 36px;
      cursor: pointer;
      line-height: 36px;
      text-align: center;
      background: #5584ff;
      border-radius: 4px;
      font-size: 14px;
      font-family: PingFangSC, PingFangSC-Regular;
      font-weight: 400;
      color: #ffffff;
    }
    .dishboardSelect{
      position: absolute;
      left:calc(50% + 70px);
      top: -8px;
      text-align: center;
      line-height: 24px;
      font-size: 14px;
      padding:6px 10px;
      background: #fff;
      color: #979797;
      display: flex;
      flex-direction:column;
      border-radius: 4px;
      span{
        padding:0px 10px;
        cursor: pointer;
        &:hover{
          color: #5584ff;
        }
      }
      span:first-child{
        line-height: 24px;
        border-bottom:solid 1px rgba(216,216,216,0.3);
      }
      &::before{
        content: '';
        width: 10px;
        height: 10px;
        background: #fff;
        position: absolute;
        left: -5px;
        top:34px;
        transform: rotate(45deg);
      }
    }
  }
}
</style>
