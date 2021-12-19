<template>
  <div class="data-container">
      <div class="seachBox">
          <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="状态：">
            <el-select v-model="formInline.state" placeholder="全部">
              <el-option label="全部" value="4"></el-option>
              <el-option label="在线" value="0"></el-option>
              <el-option label="离线" value="1"></el-option>
              <el-option label="一般报警" value="2"></el-option>
              <el-option label="严重报警" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="标签：">
            <el-input v-model="formInline.tag" placeholder="请输入设备标签"></el-input>
          </el-form-item>
          <el-form-item label="设备：">
            <el-input v-model="formInline.deviceId" placeholder="请输入设备ID"></el-input>
          </el-form-item>
          <div class="searchBut" style="display:inline-block" @click="onSubmit"><i class="iconfont icon-sousuo"></i> 搜索</div>
        </el-form>
      </div>
      <div class="contBox"  v-infinite-scroll="handleCurrentChange" style="overflow:auto" v-loading="loading" infinite-scroll-immediate="false">
         <div v-for="(item, ind) in tableData" :class="['item', calssName(item)]"  :key="ind">
           <div class="lable" v-if="item.level == 0"> <span class="hov">{{item.online ? '正常' : '离线'}} </span></div>
           <div class="lable" v-if="item.level != 0"> <span class="hov">{{item.level == 1 ? '一般报警' : '严重报警'}}</span></div>
           <div class="title">{{item.deviceId}}</div>
           <div class="info" >
             <div v-for="(it, ind) in item.quotaList" :key="ind">
               <span>{{it.quotaName}} &nbsp;<i :class="{lab: it.level}"> {{it.value}}{{it.unit}}</i></span>
               <span style="color:#818693">正常值 &nbsp; {{it.referenceValue}} {{it.unit}} </span>
              </div>
           </div>
           <div class="more" @click="openDetailsHandle(item)">...</div>
         </div>
         <div class="noData" v-show="total <= 0">
          <img src="./../../assets/nodata.png" alt="" width="129">
        </div>
      </div>
      <div class="waterMark"></div>
      <el-dialog
      :visible.sync="dialogVisible"
      width="544px"
      center
    >
      <div
        slot="title"
        class="dialog-title"
      >
        设备指标
      </div>
        <div class="dataInfo" >
          <div v-for="(it, ind) in itemData.quotaList" class="item" :key="ind">
            <span>{{it.quotaName}} &nbsp;<i style="color:#999" :class="{lab: it.level != 0}"> {{it.value}} {{it.unit}}</i> </span>
            <span style="color:#818693">正常值 &nbsp; {{it.referenceValue}} {{it.unit}} </span>
          </div>
        </div>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          @click="dialogVisible = false"
          size="small"
          class="butBox"
        >取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide, PropSync, } from "vue-property-decorator";
import Table from "@/components/Table/index.vue";
import Tabs from "@/components/Tabs/index.vue";
import { device, deviceStatus } from "@/api/equipment";

@Component({
  name: "dataInfo",
  components: {
    Table,
    Tabs
  }
})
export default class extends Vue {
  private page: number = 1;
  private pageSize: number = 20;
  private total: number = 1;
  private tableData: any = [];
  private loading = false
  private tabsData: object = [];
  private getDataType = ''
  private itemData = {}
  private dialogVisible = false;
  private formInline: {
    [index:string]:any
    } = {
    state: '4'
  };

  // 不同状态（警报、是否在线）的样式切换
  private calssName(item: any):string{
    let itemClass = ''
    if (item.level != 0){
       itemClass = item.level == 1 ? 'warning' : ''
    } else if (item.online) {
      itemClass = 'line'
    } else {
      itemClass = 'unline'
    }
    return itemClass
  }
  //查询
  private onSubmit() {
    this.page = 1
    this.getDataType = 'search'
    this.getList();
  }

  private async getList() {
    this.loading = true
    let params:{[index:string]:any} = {}
    for (let key in this.formInline){
      if (this.formInline[key]){
        params[key] = this.formInline[key]
      }
    }
    const data: any = await device({
      page: this.page,
      pageSize: this.pageSize,
      ...this.formInline
    });
    if (this.getDataType === 'search'){
      this.tableData = data.items;
      this.getDataType = ''
    } else {
      this.tableData = [...this.tableData, ...data.items];
    }
    this.total = data.counts;
    this.loading = false
  }

  //重置表单
  private resetForm(refId: any) {
    (this as any).$refs[refId].resetFields();
    // this.getList();
  }
  private openDetailsHandle(item: any){
    this.itemData = item
    this.dialogVisible = true
    console.log(item)
  }

  private async handleClick(row: any) {
    const data: any = await deviceStatus({
      id: row.id,
      status: row.status
    });
  }
  
  private handleCurrentChange() {
    if (!this.loading) {
      if (this.tableData.length < this.total) {
        this.page++ 
        this.getList();
      }else {
        this.$message.success('已经是最后一页了！')
      }
    }
  }

  mounted() {
    this.getList();
  }
}
</script>
<style lang="scss">
.data{
   &-container{
      .el-form-item__label{
        color: #666666;
      }
      .el-form--inline .el-form-item{
        width: 280px;
        .el-select {
          width: 100%;
        }
      }
      .el-form--inline .el-form-item__content {
        width: calc(100% - 60px);
      }
   }
} 
</style>
<style lang="scss" scoped>
.data{
  &-container{
    background: #f3f4f7;
    padding: 20px 0;
    .seachBox{
      padding: 0px 35px;
      .searchBut{
        display: inline-block;
        position: relative;
        left: -10px;
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
      padding: 0px 35px;
      height: calc(100vh - 170px);
      overflow-y: scroll;
      align-content:flex-start;
      .item{
        position: relative;
        margin: 30px 10px 50px 10px;
        width: calc(20% - 20px);
        height: 166px;
        background: #ffffff url(./../../assets/img/waterMarkDatainfo3.png) right bottom no-repeat;
        background-size: 70px 65px;
        border-radius: 11px;
        .lable{
            position: absolute;
            font-size: 12px;
            color: #fff;
            left: 50%;
            top: 0;
            transform: translate(-50%, -50%);
            text-align: center; 
            padding: 5px;
            width: 40px;
            height: 40px;
            border-radius: 100%;
            background: #ff5757;
            opacity: 0.76;
            display: flex;
            align-items: center;
            span{
              position: relative;
              display: inline-block;
              width: 40px;
            }
            &::before{
              content: '';
              position: absolute;
              top: -5px;
              left: -5px;
              display: inline-block;
              width: 50px;
              height: 50px;
              border-radius: 100%;
              background: #ff5757;
              opacity: 0.38;
            }
            &::after{
              content: '';
              position: absolute;
              top: -10px;
              left: -10px;
              display: inline-block;
              width: 60px;
              height: 60px;
              border-radius: 100%;
              background: #ff5757;
              opacity: 0.07;
            }
        }
        .title{
          text-align: center;
          color: #333;
          padding-top:40px;
        }
        .info{
          font-size: 14px;
          color: #999;
          padding:30px 20px;
          height: 90px;
          line-height: 27px;
          overflow: hidden;
          div{
            display: flex;
            justify-content: space-between;
            span:last-child {
              font-size: 12px;
              color: #818693;
            }
            i{
              font-style: normal;
            }
          }
          div:first-child{
            i{
              color: #FF5757 !important; 
            }
          }
        }
        .hov{
          &::after{
            position: absolute;
            content: '';
            width: 40px;
            height: 40px;
            box-sizing: content-box;
            border: solid 1px #ff5757;
            left: 50%;
            top: 50%;
            border-radius: 100%;
            z-index: -1;
            transform: translate(-50%, -50%);
            opacity: 0.7;
          }
          @keyframes flower {
            0% {
              border-width: 1;
              opacity: 0.7;
            }
            100%{
              border-width: 10px;
              opacity: 0;
            }
          }
        }
        &:hover .hov{
          &::after{
              animation: flower 1.5s ease 100ms infinite;
            }
        }
        .more{
          position: relative;
          top:-3px;
          text-align: center;
          font-size: 24px;
          font-weight: bold;
          line-height: 0px;
          cursor: pointer;
          color: #5584FF;
          letter-spacing: 1px;
        }
      }
      .warning{
        .lable{
          background: #FFC400;
          &::before{
            background: #FFC400;
          }
          &::after{
            background: #FFC400;
          }
        }
        .info{
          div:first-child{
            i{
              color: #FFC400 !important; 
            }
          }
        }
        .hov{
          &::after{
            border-color: #FFC400;
          }
        }  
      }
      .line{
        background: #ffffff url(./../../assets/img/waterMarkDatainfo2.png) right bottom no-repeat;
        background-size: 70px 65px;
        .lable{
          background: #57CE9E;
          &::before{
            background: #57CE9E;
          }
          &::after{
            background: #57CE9E;
          }
        }
        .info{
          div:first-child{
            i{
              color: #57CE9E !important; 
            }
          }
        }
        .hov{
          &::after{
            border-color: transparent;
          }
        }  
      }
      .unline{
        background: #ffffff url(./../../assets/img/waterMarkDatainfo1.png) right bottom no-repeat;
        background-size: 70px 65px;
        .lable{
          background: #9EAEC5;
          &::before{
            background: #9EAEC5;
          }
          &::after{
            background: #9EAEC5;
          }
        }
        .info{
          div:first-child{
            i{
              color: #9EAEC5 !important; 
            }
          }
        }
        .hov{
          &::after{
            border-color: #9EAEC5;
          }
        }  
      }
      @media screen and(max-width:1466px){
        .item{
            width: calc(25% - 20px);
          }
      }
    }
    .waterMark{
      position: fixed;
      bottom: 0px;
      left:0px;
      width: 106px;
      height: 109px;
      background: url(./../../assets/img/waterMarkDatainfo3.png) left bottom no-repeat;
      background-size: 106px 109px; 
    }
    .dataInfo{
      .item{
        text-align: center;
        line-height: 40px;
        display: flex;
        span{
          flex: 1;
          i{
            font-style: normal;
          }
          .lab{
            color: #ff5757!important
          }
        }
        span:first-child{
          text-align: right;
          padding-right: 20px;
        }
        span:last-child{
          text-align: left;
          padding-left:20px;
          font-size: 12px;
          color:rgba(129,134,147,0.45)
        }
      }
    }
    .butBox{
        width: 80px;
        height: 36px;
        background: #ffffff;
        border: 1px solid #5584ff;
        border-radius: 4px;
        color: #5584ff;
      }
  }
}
</style>
