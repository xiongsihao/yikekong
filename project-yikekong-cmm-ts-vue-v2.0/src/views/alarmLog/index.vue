<template>
  <div class="alarmLog-waterMark">
  <div class="alarmLog-container">
    <Tabs :tabsData="tabsData" />
    <div class="seachBox">
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="报警名称：" class="taglong">
          <el-input v-model="formInline.alarmName" placeholder="请输入报警名称"></el-input>
        </el-form-item>
        <el-form-item label="报警设备：" class="taglong">
          <el-input v-model="formInline.deviceId" placeholder="请输入设备编号"></el-input>
        </el-form-item>
        <!-- <el-form-item label="状态：" class="tagSearch">
          <el-input v-model="formInline.tags" placeholder="请输入"></el-input>
        </el-form-item> -->
        <el-form-item label="时间：" class="tagSearch" >
          <el-date-picker
            v-model="timeVal"
            :clearable="false"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <div class="searchBut" @click="onSubmit()"><i class="iconfont icon-sousuo"></i> 搜索</div>
      </el-form>
    </div>
    <div class="contBox">
      <div :class="{'item': true, 'itemLev': item.level == 1}" v-for="(item,index) in tableData" :key="index">
        <div :class="{'lab': true, 'nofsLab': index == 0, 'nolsLab': index == tableData.length-1}"><span></span></div>
        <div class="itemInfo">
          <div v-if="item.time"><span class="iconfont icon-shijian"></span> {{item.time}}</div>
          <div v-else><span class="iconfont icon-shijian"></span> -- </div>
          <div style="text-align: left;padding-left: 160px;">
            <span class="iconfont icon-baojing"></span> 
            <span>{{item.alarmName}}</span>
            <span v-if="item.level == 1" class="labLev">一般</span>
            <span v-if="item.level == 2" class="labLev">严重</span>  
            </div>
          <div style="text-align: left;padding-left: 160px;"><span class="iconfont icon-shuju1"></span> {{item.quotaName}} <span class="unit">{{item.unit}}</span> </div>
          <div><span class="iconfont icon-shebei"></span> {{item.deviceId}}</div>
        </div>
      </div>
      <div class="noData" v-if="total <= 0">
          <img src="./../../assets/nodata.png" alt="" width="129">
      </div>
    </div>
    <div class="pagination" v-show="total > 10">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="formInline.pageSize"
        :current-page="formInline.page"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide } from "vue-property-decorator";
import Table from "@/components/Table/index.vue";
import Tabs from "@/components/Tabs/index.vue";
import { getAlarmLog } from "@/api/alarmManage";

@Component({
  name: "alarmLog",
  components: {
    Table,
    Tabs
  }
})
export default class extends Vue {
  private formInline: {[index:string]:any} = {
    pageSize: 10,
    page:1
  };
  private timeVal = [new Date(), new Date()]
  private tableData = [];
  private total = 0

  private tableColumnData = [
    {
      prop: "date",
      label: "报警时间"
    },
    {
      prop: "name",
      label: "报警名称"
    },
    {
      prop: "date",
      label: "级别"
    },
    {
      prop: "name",
      label: "指标"
    },
    {
      prop: "date",
      label: "数据"
    },
    {
      prop: "name",
      label: "设备编号"
    }
  ];

  private tabsData: object = [
    {
      label: "报警日志",
      url: "/alarmLog/list",
      selected: true
    },
    {
      label: "报警管理",
      url: "/alarmManage/list"
    }
  ];

  mounted(){
    this.init()
  } 
 
  private init() {
    const params:{[index:string]: any} = {...this.formInline}
    params.start = (this as any).moment(this.timeVal[0]).format('YYYY-MM-DD 00:00:00')
    params.end = (this as any).moment(this.timeVal[1]).format('YYYY-MM-DD 23:59:59')
    getAlarmLog({...params}).then((res:any) => {
      this.tableData = res.items
      this.total = res.counts
    }).catch(err => {
      console.log(err)  
    })
  }

  private onSubmit(refId: any) {
    this.formInline.page = 1
    this.init()
  }

  private resetForm(refId: any) {
    (this as any).$refs[refId].resetFields();
  }

  private handleClick(row: any) {
    console.log(row);
  }

  private handleSizeChange(val: any) {
    this.formInline.pageSize = val
    this.init()
  }
  private handleCurrentChange(val: any) {
    this.formInline.page = val
    this.init()
  }
}
</script>


<style lang="scss">
.alarmLog{
   &-container{
     .el-form--inline .el-form-item{
       width: 270px;
     }
     .tagSearch{
       width: 415px !important;
     }
     .taglong .el-form-item__content{
       width: calc(100% - 85px) !important;
     }
     .el-range-separator{
       width: 20px;
     }
   }
} 
</style>
<style lang="scss" scoped>
.alarmLog-waterMark{
      height: 100%;
      background: #f3f4f7 url(./../../assets/img/waterMarkalarmLog.png) 0 calc(100% - 0px) no-repeat;
      background-size:109px 106px; 
    }
.alarmLog{
  &-container{
    height: calc(100vh - 66px);
    background:  url(./../../assets/img/watermarkalarmLogtop.png) calc(100% - 40px) 20px no-repeat;
    background-size:174px 148px; 
    padding: 28px 35px;
    .seachBox{
      padding-left: 10px;
      margin-top: 20px;
      .searchBut{
        display: inline-block;
        position: relative;
        left: -15px !important;
        width: 89px;
        cursor: pointer;
        height: 40px;
        line-height: 38px;
        text-align: center;
        background: #ffffff;
        border: 1px solid #5584ff;
        border-radius: 4px;
        color: #5584ff;
      }
    }
    
    .contBox{
       .item{
         height: 58px;
         margin-left: 44px;
         position: relative;
         display: flex;
         align-items: center;
         .itemInfo{
            text-align: center;
            flex: 1;
            background: #fff;
            height: 44px;
            line-height: 44px;
            // margin: 7px 0;
            display: flex;
            justify-content: center;
            color: #20232a;
            font-size: 14px;
            div{
              flex:1;
            }
            .iconfont{
              color:#afb6ca;
              margin-right: 10px;
              font-size: 14px;
            }
            .labLev{
              display: inline-block;
              position: relative;
              padding: 2px 5px;
              margin-left: 10px;
              background: #FF5757;
              color:#fff;
              line-height: 12px;
              font-size: 12px;
              border-radius: 3px;
              &::before{
                content: '';
                display: inline-block;
                width: 6px;
                height: 6px;
                position: absolute;
                transform:translateY(-50%) rotate(45deg);
                left: -3px;
                top: 50%;
                background: #FF5757;
              }
            }
            .unit{
              color: #FF5757;
            }
         }
         .lab{
            position: absolute;
            left: -44px;
            height: 58px;
            span{
              position: relative;
              top:50%;
              transform: translateY(-50%);
              display: inline-block;
              width: 15px !important;
              height: 15px;
              border-radius: 100px;
              border:solid 2px #FF5757;
              background: #fff;
              z-index: 5;
            }
            &::before{
              content: '';
              position: absolute;
              display: inline-block;
              width: 0px;
              height: 29px;
              border:solid 1px #cfdcf6;
              top:0px;
              left:50%;
              transform: translateX(-50%);
              z-index: 1;
            }
            &::after{
              content: '';
              position: absolute;
              bottom:0px;
              display: inline-block;
              width: 0px;
              height: 29px;
              border:solid 1px #cfdcf6;
              left:50%;
              transform: translateX(-50%);
              z-index: 1;
            }
         }
         .nofsLab{
           &::before{
              display: none;
            }
         }
         .nolsLab{
           &::after{
              display: none;
            }
         }
         .itemInfo::before{
           content: '';
           display: inline-block;
           width: 10px;
           height: 10px;
           position: relative;
           transform:translateY(-50%) rotate(32deg) skew(-30deg, 0deg);
           left: -5px;
           top: 50%;
           background: #fff;
         }
         &:hover{
           .lab{
             span{
              background-color: #FF5757;
              // background-image: radial-gradient(20 40, #fff 30%, #f00 40%) !important;
             }
           }
           .itemInfo{
             transition: 500ms ease;
             transform: scale(1.02, 1.1);
           }
         }
       }
       .itemLev{
         .itemInfo{
            .labLev{
              background: #FFC400;
              &::before{
                background: #FFC400;
              }
            }
            .unit{
              color: #FFC400;
            } 
         }
          .lab{
            span{
              border:solid 2px #FFC400;
            }
          }
          &:hover{
           .lab{
             span{
              background-color: #FFC400;
              // background-image: radial-gradient(20 40, #fff 30%, #f00 40%) !important;
             }
           }
         }
       }
    }
    .pagination{
      text-align: right;
    }
  }
}
</style>

