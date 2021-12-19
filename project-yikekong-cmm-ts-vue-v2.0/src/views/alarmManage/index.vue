<template>
  <div class="alarmManage-container">
    <Tabs :tabsData="tabsData" />
     <div class="topBox">  
       <div class="seachBox">
          <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="报警名称：" class="taglong">
            <el-input v-model="formInline.name" placeholder="请输入报警名称" maxlength="10"></el-input>
          </el-form-item>
          <el-form-item label="指标：">
            <el-select v-model="formInline.quotaId" placeholder="全部">
              <el-option label="全部" value="" ></el-option>
              <el-option v-for="(item,index) in quotaIdSelectData" :key="index" :label="item.name" :value="item.id" ></el-option>
            </el-select>
          </el-form-item>
            <div class="searchBut" @click="onSubmit"><i class="iconfont icon-sousuo"></i> 搜索</div>
         
        </el-form>
      </div>
       <div class="butHandle">
          <el-button type="primary" @click="handleClick()" size="small" ><span class="iconfont icon-tianjia"></span>添加报警</el-button>
       </div>
     </div>
     
      <div class="contBox">
         <div v-for="(item, ind) in tableData" :class="['item']"  :key="ind">
           <div class="infoList">
             <div class="col"><span class="iconfont icon-baojing"></span>{{item.quota ? item.quota.name : '--'}} </div>
             <div class="col"><span class="iconfont icon-shuju1"></span>{{item.name}} </div>
           </div>
           <div class="action">
             <span @click="changeItem(item)" class="iconfont icon-xiugailiebiao"></span>
             <span @click="delItem(item)" class="iconfont icon-shanchu2"></span>
           </div>
         </div>
         <div class="noData" v-if="total <= 0">
          <img src="./../../assets/nodata.png" alt="" width="129">
        </div>
      </div>

    <div class="pagination" v-show="total > 20">
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

    <el-dialog
      :visible.sync="dialogVisible"
      width="500px"
      center
      @close="resetForm('quota')"
    >
      <div
        slot="title"
        class="dialog-title"
      >
        {{title}}
      </div>

      <el-form
        :rules="rules"
        :model="form"
        ref="quota"
        label-width="100px"
      >
        <el-form-item
          prop="name"
          label="报警名称："
        >
          <el-input
            style="width:100%"
            placeholder="请输入报警名称"
            v-model="form.name"
            maxlength='20'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="quotaId"
          label="报警指标："
        >
          <el-select
            v-model="form.quotaId"
            placeholder="请选择报警指标"
            size="small"
          >
            <el-option
              v-for="(item,index) in quotaIdSelectData"
              :key="index"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="webHook"
          label="web hook："
        >
          <el-input
            placeholder="请输入web hook"
            maxlength='200'
            v-model="form.webHook"
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="报警阈值："
          required
        >
          <div style="display:flex">
             <div style="flex:1">
            <el-form-item prop="operator">
              <el-select
                v-model="form.operator"
                placeholder="请选择"
                size="small"
              >
                <el-option
                  label=">"
                  value=">"
                ></el-option>
                <el-option
                  label="<"
                  value="<"
                ></el-option>
                <el-option
                  label="="
                  value="="
                ></el-option>
              </el-select>
            </el-form-item>
          </div>
            <div style="width: 10px"></div>
            <div style="flex:1">
            <el-form-item prop="threshold">
              <el-input
                placeholder="请输入报警阀值"
                v-model.number="form.threshold"
                maxlength='5'
                size="small"
                onkeyup="this.value = this.value.replace(/[^\d.]/g,'');"
              ></el-input>
            </el-form-item>
          </div>
        </div>  
        </el-form-item>
        <el-form-item
          prop="level"
          label="报警级别："
        >
          <el-radio-group v-model="form.level">
            <el-radio :label="1">一般</el-radio>
            <el-radio :label="2">严重</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="沉默周期：" required>
          <div style="display:flex">
             <div style="flex:1">
               <el-form-item prop="cycle">
                  <el-input
                  placeholder="请输入沉默周期"
                  v-model.number="form.cycle"
                  maxlength='5'
                  size="small"
                  onkeyup="this.value = this.value.replace(/[^\d.]/g,'');"
                ></el-input>
              </el-form-item>
            </div>
            <div style="width: 20px"></div>
            <div style="flex:1">
              <el-form-item prop="time">
              <el-select
                v-model="form.time"
                placeholder="请选择"
                size="small"
              >
                <el-option
                  v-for="(item,index) in timeSelectData"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          @click="resetForm('quota')"
          size="small"
          class="butBox"
        >取 消</el-button>
        <el-button
          type="primary"
          @click="submitForm('quota')"
          size="small"
          class="butBox"
          style="margin-left: 30px;"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide } from "vue-property-decorator";
import Table from "@/components/Table/index.vue";
import Tabs from "@/components/Tabs/index.vue";
import { alarm, addAlarm, putAlarm, delAlarm } from "@/api/alarmManage";
import { quota } from "@/api/fingerMark";

@Component({
  name: "alarmLog",
  components: {
    Table,
    Tabs
  }
})
export default class extends Vue {
  private formInline: {[index:string]:any} = {
    page: 1,
    pageSize: 20
  };
  private total: number = 1;
  private tableData: any = [];
  private title: string = '添加报警'
  private tabsData: object = [
    {
      label: "报警日志",
      url: "/alarmLog/list"
    },
    {
      label: "报警管理",
      url: "/alarmManage/list",
      selected: true
    }
  ];

  private timeSelectData: object = [
    {
      label: "小时",
      value: "60"
    },
    {
      label: "分钟",
      value: "1"
    }
  ];

  private quotaIdSelectData: object = [];
  private restKey = 0
  private dialogVisible = false;

  @Provide() form: object = {
    name: "",
    quotaId: "",
    operator: "",
    threshold: "",
    level: 1,
    cycle: "",
    webHook: "",
    time: ""
  };
  
  @Provide() rules: any = {
    name: [{ required: true, message: "请输入报警名称", trigger: "blur" }],
    quotaId: [{ required: true, message: "请选择报警指标", trigger: "change" }],
    webHook: [{ required: true, message: "请输入webHook", trigger: "blur" }],
    operator: [
      { required: true, message: "请选择的报警阈值", trigger: "change" }
    ],
    threshold: [
      { required: true, message: "请选择的报警阈值", trigger: "change" }
    ],
    level: [{ required: true, message: "请选择报警级别", trigger: "change" }],
    cycle: [{ required: true, message: "请输入沉默周期", trigger: "blur" }]
  };

  //是否新增
  private isAddPut = true;

  //搜索
  private onSubmit(refId: any) {
    this.formInline.page = 1
    this.getList();
  }

  //获取报警列表
  private async getList() {
    const data: any = await alarm({ ...this.formInline });
    this.tableData = [...data.items];
    this.total = data.counts;
  }
  //获取指标
  private async getQuotaList() {
    const data: any = await quota({
      page: 1,
      pageSize: 1000
    });
    this.quotaIdSelectData = [...data.items];
  }

  private handleSizeChange(val: any) {
    this.formInline.pageSize = val
    this.getList()
  }
  private handleCurrentChange(val: any) {
    this.formInline.page = val
    this.getList()
  }

  //添加报警提交
  submitForm(refId: any) {
    (this as any).$refs[refId].validate(async (valid: boolean) => {
      if (valid == true) {
        let params: any = this.form
        if ((this.form as any).time == "60") {
          params.cycle = params.cycle * 60
        }
        if (this.isAddPut) {
          delete (this as any).form.id;
          const data: any = await addAlarm(params);
        } else {
          const data: any = await putAlarm(params);
        }
        this.getList();
        this.dialogVisible = false;
        (this as any).$refs[refId].resetFields();
      }
    });
  }
  //添加报警
  private handleClick() {
    this.dialogVisible = true;
    this.isAddPut = true;
    this.title = "添加报警";
    (this as any).form.time = "1";
    this.$nextTick(() => {
      (this as any).$refs["quota"].resetFields();
    });
  }
  //修改报警
  private changeItem(item: any) {
    this.title = "修改报警"
    this.dialogVisible = true;
    this.isAddPut = false;
    if (item.cycle % 60 == 0) {
        item.time = '60';
        item.cycle = item.cycle/60
      } else {
        item.time = '1';
      }
    this.$nextTick(() => {
      this.form = Object.assign({}, item);
    });
  }
  //删除报警
  private async delItem(item: any) {
    this.$confirm('确认删除?', '', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(async () => {
       const data: any = await delAlarm(item.id);
    this.getList();
    }).catch((err:any) => {
      console.log('取消操作！')
    })
  }

  resetForm(refId: any) {
    (this as any).$refs[refId].resetFields();
    this.dialogVisible = false;
  }
  mounted() {
    this.getList();
    this.getQuotaList();
  }
}
</script>

<style lang="scss">
.alarmManage{
   &-container{
    .el-input--small .el-input__inner{
      width: 100% !important;
    }
    .el-select--small{
      width: 100%;
    }
    .el-dialog--center .el-dialog__body{
      padding: 25px 30px 30px;
    }
    .taglong .el-form-item__content{
       width: calc(100% - 85px) !important;
     }
   }
} 
</style>
<style lang="scss" scoped>
.alarmManage{
  &-container{
    background: #f3f4f7;
    padding: 28px 35px;
    .topBox{
      padding: 0 14px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .butHandle{
        .iconfont{
          margin-right: 5px;
          position: relative;
          top:1px;
        }
      }
    }
    .seachBox{
      margin-top: 20px;
      .searchBut{
        display: inline-block;
        position: relative;
        left: -10px;
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
      display: flex;
      flex-wrap: wrap;
      font-size: 14px;
      .item{
        width: calc(50% - 14px);
        height: 44px;
        line-height: 44px;
        background: #fff;
        border-radius: 2px;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        margin: 7px 7px;
        .infoList{
          display: flex;
          flex: 1;
          padding-left: 25px;
          .col{
            flex:1;
            color: #20232a;
          }
          div:last-child{
            flex: 2;
          }
          .iconfont{
            font-size: 14px;
            margin-right: 10px;
            color:#afb6ca
          }
        }
        .action{
          padding-right: 40px;
          span{
            display: inline-block;
            cursor: pointer;
            color:#5584FF;
            margin: 0 10px;
            font-size: 20px;
          }
          span:last-child{
            color: #f00;
          }
        }
        &:hover{
          transition: 500ms ease;
          transform: scale(1.02, 1.1);
        }
      }
    }
    .butBox{
      width: 80px;
      height: 36px;
      border-radius: 4px;
    }
  }
}
</style>
