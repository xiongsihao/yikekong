<template>
  <div class="finerMark-container">
    <Tabs :tabsData="tabsData" />
    <div class="topContent">
      <div class="seachBox">
          <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="指标：">
            <el-input v-model="formInline.quotaName" placeholder="请输入指标名称"></el-input>
          </el-form-item>
            <div class="searchBut" @click="onSubmit()"><i class="iconfont icon-sousuo"></i> 搜索</div>
        </el-form>
      </div>
      <div class="addCont">
        <el-button type="primary" style="width:110px; height:36px" size="small" @click="addItem()" > <span class="iconfont icon-tianjia"></span>添加指标</el-button>
        <el-button type="primary" style="width:110px; height:36px" size="small" @click="addGpsItem()" >GPS指标设置</el-button>
      </div>
    </div>
    <div class="contBox" v-infinite-scroll="handleCurrentChange" style="overflow:auto" v-loading="loading" infinite-scroll-immediate="false" >
      <div :class="['item', setTitColor(index)]" v-for="(item,index) in tableData" :key="index" >
        <div @click="changeItem(index)">
          <div class="title">
            <div class="tit"> <span class="iconfont icon-zhibiao"></span><span class="long">{{item.name}}</span> </div>
            <div class="del el-icon-close" @click.stop="delItem(item.id)"></div>
          </div>
          <div class="cont">
            <div class=""><span> 单位：</span> {{item.unit}}</div>
            <div class=""><span> 安全值：</span> {{item.referenceValue}}</div>
          </div>
        </div>
      </div>
      <div class="noData" v-if="total <= 0">
          <img src="./../../assets/nodata.png" alt="" width="129">
      </div>
    </div>

    <el-dialog
      :visible.sync="quotaDialogVisible"
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
        label-width="140px"
      >
        <el-form-item
          prop="name"
          label="指标名称："
        >
          <el-input
            placeholder="请输入指标名称"
            v-model="form.name"
            size="small"
            maxlength='10'
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="unit"
          label="单位："
        >
          <el-input
            placeholder="请输入单位"
            v-model="form.unit"
            maxlength='10'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="subject"
          label="报文主题："
        >
          <el-input
            placeholder="请输入报文主题"
            v-model="form.subject"
            maxlength='50'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="valueType"
          label="指标值数据类型："
        >
          <el-select
            placeholder="请选择指标值数据类型"
            v-model="form.valueType"
            size="small"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="valueKey"
          label="指标值字段："
        >
          <el-input
            placeholder="请输入指标值字段"
            v-model="form.valueKey"
            size="small"
          maxlength='50'
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="snKey"
          label="设备识别码字段："
        >
          <el-input
            placeholder="请输入设备识别码字段"
            v-model="form.snKey"
            maxlength='50'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="webhook"
          label="web hook："
        >
          <el-input
            placeholder="请输入web hook"
            v-model="form.webhook"
            maxlength='50'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="referenceValue"
          label="安全值："
        >
          <el-input
            placeholder="请输入安全值"
            v-model="form.referenceValue"
            maxlength='20'
            size="small"
          ></el-input>
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
          @click="submitQuotaForm('quota')"
          size="small"
          class="butBox"
          style="margin-left:30px"
        >确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :visible.sync="GPSDialogVisible"
      width="500px"
      center
    >
      <div
        slot="title"
        class="dialog-title"
      >
        GPS指标
      </div>

      <el-form
        :model="gpsForm"
        :rules="gpsRules"
        ref="gps"
        label-width="140px"
      >
        <el-form-item
          prop="subject"
          label="报文主题："
        >
          <el-input
            placeholder="请输入报文主题"
            v-model="gpsForm.subject"
            maxlength='20'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="snKey"
          label="设备识别码字段："
        >
          <el-input
            placeholder="请输入设备识别码字段"
            maxlength='50'
            v-model="gpsForm.snKey"
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="singleField"
          label="指标值类型："
        >
          <el-radio-group v-model="gpsForm.singleField">
            <el-radio :label="true">单字段</el-radio>
            <el-radio :label="false">多字段</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-if="gpsForm.singleField"
          prop="valueKey"
        >
          <el-input
            placeholder="请输入指标值字段（经纬度）"
            v-model="gpsForm.valueKey"
            maxlength='20'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          v-if="gpsForm.singleField"
          prop="separation"
        >
          <el-input
            placeholder="请输入分隔符"
            v-model="gpsForm.separation"
            maxlength='20'
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          v-if="!gpsForm.singleField"
          prop="longitude"
        >
          <el-input
            placeholder="请输入指标值字段（经度）"
            v-model="gpsForm.longitude"
            size="small"
            maxlength='20'
          ></el-input>
        </el-form-item>
        <el-form-item
          v-if="!gpsForm.singleField"
          prop="latitude"
        >
          <el-input
            placeholder="请输入指标值字段（纬度）"
            v-model="gpsForm.latitude"
            size="small"
            maxlength='20'
          ></el-input>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          @click="GPSDialogVisible = false"
          size="small"
          class="butBox"
        >取 消</el-button>
        <el-button
          type="primary"
          @click="submitGPSForm('gps')"
          size="small"
          class="butBox"
          style="margin-left:30px"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide, Watch } from "vue-property-decorator";
import Tabs from "@/components/Tabs/index.vue";
import {
  quota,
  addQuota,
  putQuota,
  delQuota,
  gps,
  addGps,
  putGps
} from "@/api/fingerMark";

@Component({
  name: "equipment",
  components: {
    Tabs
  }
})

export default class extends Vue {
  private formInline: object = {
    quotaName: ""
  };
  private page: number = 1;
  private pageSize: number = 20;
  private total: number = 1;
  private tableData: any = [];
  private loading = false
  private title = '添加指标'
  private changeInd = ''

  private setTitColor(index: number){
    switch(index%6) {
      case 1 :{
        return 'colStyB'
        break
      }
      case 2 :{
        return 'colStyC'
        break
      }
      case 3 :{
        return 'colStyD'
        break
      }
      case 4 :{
        return 'colStyE'
        break
      }
    }
  }
  private tabsData: object = [
    {
      label: "设备管理",
      url: "/equipment/list"
    },
    {
      label: "指标管理",
      url: "/fingerMark/list",
      selected: true
    }
  ];

  private options: object = [
    {
      value: "Integer",
      label: "整数"
    },
    {
      value: "Double",
      label: "小数"
    }
  ];

  private quotaDialogVisible = false;
  private GPSDialogVisible = false;
  private getDataType = ''
  //添加指标数据
  private form: object = {
    name: "",
    unit: "",
    subject: "",
    valueKey: "",
    valueType: "",
    snKey: "",
    webhook: "",
    referenceValue: ""
  };

  private rules: any = {
    name: [{ required: true, message: "请输入指标名称", trigger: "blur" }],
    unit: [{ required: true, message: "请输入单位", trigger: "blur" }],
    subject: [{ required: true, message: "请输入报文主题", trigger: "blur" }],
    valueKey: [
      { required: true, message: "请输入指标值字段", trigger: "blur" }
    ],
    valueType: [
      { required: true, message: "请选择指标值字段类型", trigger: "change" }
    ],
    snKey: [
      { required: true, message: "请输入设备识别码字段", trigger: "blur" }
    ],
    webhook: [{ required: true, message: "请输入web hook", trigger: "blur" }],
    referenceValue: [
      { required: true, message: "请输入安全值", trigger: "blur" }
    ]
  };

  //GPS指标
  private gpsForm = {
    subject: "",
    snKey: "",
    singleField: true,
    valueKey: "",
    separation: "",
    latitude: "",
    longitude: ""
  };
  private gpsRules = {
    subject: [{ required: true, message: "请输入报文主题", trigger: "blur" }],
    snKey: [
      { required: true, message: "请输入设备识别码字段", trigger: "blur" }
    ],
    singleField: [
      { required: true, message: "请选择指标值类型", trigger: "change" }
    ],
    valueKey: [
      { required: true, message: "请输入指标值字段（经纬度）", trigger: "blur" }
    ],
    separation: [{ required: true, message: "请输入分隔符", trigger: "blur" }],
    latitude: [
      { required: true, message: "请输入指标值字段（纬度）", trigger: "blur" }
    ],
    longitude: [
      { required: true, message: "请输入指标值字段（经度）", trigger: "blur" }
    ]
  };
  //是否新增指标
  private isAddPut: boolean = true;
  //是否新增GPS
  private isGpsAddPut: boolean = true;

  @Watch("gpsForm.singleField")
  onChangeValue(newVal: string, oldVal: string) {
    this.$nextTick(() => {
      (this as any).$refs["gps"].validate();
    });
  }

  //查询
  private onSubmit(refId: any) {
    this.page = 1
    this.getDataType = 'search'
    this.getList();
  }

  private async getList() {
    this.loading = true
    const data: any = await quota({
      page: this.page,
      pageSize: this.pageSize,
      ...this.formInline
    });
    if (this.getDataType === 'page'){
      this.tableData = [...this.tableData, ...data.items];
      this.getDataType = ''
    } else {
      this.tableData = data.items;
    }
    this.total = data.counts;
    this.loading = false
  }

 private handleCurrentChange() {
    if (!this.loading) {
      if (this.tableData.length < this.total) {
        this.page++ 
        this.getDataType = 'page'
        this.getList();
      }else {
        this.$message.success('已经是最后一页了！')
      }
    }
  }
  //添加指标提交
  private submitQuotaForm(refId: any) {
    (this as any).$refs[refId].validate(async (valid: boolean) => {
      if (valid == true) {
        let data: any = null
        if (this.isAddPut) {
          delete (this as any).form.id;
          data = await addQuota(this.form);
          if (data) {
            this.tableData.unshift(this.form)
            this.getList();
          }
        } else {
          data = await putQuota(this.form);
          if (data) {
            this.tableData[this.changeInd] = this.form
            this.getList();
          }
        }
        this.quotaDialogVisible = false;
      }
    });
  }

  //添加指标
  private addItem(refId: any) {
    this.quotaDialogVisible = true;
    this.isAddPut = true;
    this.title =  '添加指标'
    this.$nextTick(() => {
      (this as any).$refs["quota"].resetFields();
    });
  }

  //修改指标
  private changeItem(refId: any) {
    this.changeInd = refId
    this.quotaDialogVisible = true;
    this.title =  '修改指标'
    this.isAddPut = false;
    this.$nextTick(() => {
      this.form = Object.assign({}, this.tableData[refId]);
    });
  }

  //删除指标
  private async delItem(refId: any) {
    this.$confirm('确认删除?', '', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(async () => {
       const data: any = await delQuota(refId);
    this.getList();
    }).catch((err:any) => {
      console.log('取消操作！')
    })
  }

  //添加/修改GPS指标提交
  private submitGPSForm(refId: any) {
    (this as any).$refs[refId].validate(async (valid: boolean) => {
      if (valid == true) {
        if (this.isGpsAddPut) {
          const data: any = await addGps(this.gpsForm);
        } else {
          const data: any = await putGps(this.gpsForm);
        }
        this.GPSDialogVisible = false;
      }
    });
  }

  //添加GPS指标
  private async addGpsItem(refId: any) {
    this.GPSDialogVisible = true;
    const data: any = await gps();
    if (data.id) {
      this.$nextTick(() => {
        this.gpsForm = Object.assign({}, data);
      });
      this.isGpsAddPut = false;
    } else {
      this.$nextTick(() => {
        (this as any).$refs["gps"].resetFields();
      });
    }
  }

  private resetForm(refId: any) {
    (this as any).$refs[refId].resetFields();
    this.quotaDialogVisible = false;
    this.GPSDialogVisible = false;
  }

  mounted() {
    this.getList();
  }
}
</script>


<style lang="scss">
.equipment{
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
        width: calc(100% - 70px);
      }
   }
} 
</style>
<style lang="scss" scoped>
.finerMark{
  &-container{
    background: #f3f4f7;
    padding: 28px 35px 20px 35px;
    .topContent{
      display: flex;
      justify-content: space-between;
      .seachBox{
        padding-left: 10px;
        margin-top: 20px;
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
      .addCont{
        display: flex;
        padding-left: 10px;
        font-size: 14px;
        margin-top: 20px;
        .iconfont{
          margin-right: 5px;
          position: relative;
          top:1px;
        }
      }
    }
    
    .contBox{
      display: flex;
      flex-wrap: wrap;
      align-content:flex-start;
      height: calc(100vh - 240px);
      padding-top: 20px;
      .item{
        position: relative;
        height: 120px;
        margin: 0px 10px 30px 10px;
        width: calc(20% - 20px);
        border-radius: 11px;
        background: #ffffff url(./../../assets/img/watermarkfingerItem.png) calc(100% - 10px) bottom no-repeat;
        background-size: 87px 74px;
        .long{
          display: inline-block;
          width: 74px;
          overflow: hidden;
          height: 30px;
        }
        .title{
          width: 100%;
          display: flex;
          justify-content: space-between;
          .tit{
            display: flex;
            position: relative;
            padding: 0 15px;
            width: 130px;
            height: 32px;
            line-height: 32px;
            overflow: hidden;
            color:#fff;
            background: linear-gradient(left, #6EC1FF, #5584FF);
            border-radius:0px 15px 15px 15px;
            .iconfont{
              margin-right: 10px;
            }
          }
          .del{
            display: none;
            color: #D8D8D8;
            position: relative;
            cursor: pointer;
            right: 5px;
            top: 5px;
          }
        }
        &:hover .del{
          display: block;
        }
        .cont{
          padding: 20px 20px 30px 20px;
          color: #333;
          line-height: 23px;
          span{
            color: #818693;
            font-size: 14px;
          }
        }
      }
      .colStyB{
        .title{
          .tit{
            background: linear-gradient(left, #FF5133, #B075FF);
          }
        }
      }
      .colStyC{
        .title{
          .tit{
            background: linear-gradient(left, #5CE2D1, #55C4FF);
          }
        }
      }
      .colStyD{
        .title{
          .tit{
            background: linear-gradient(left, #FF4E6D, #FF6941);
          }
        }
      }
      .colStyE{
        .title{
          .tit{
            background: linear-gradient(left, #FFB16E, #FF7955);
          }
        }
      }
      .colStyF{
        .title{
          .tit{
            background: linear-gradient(left, #45D282, #17CD99);
          }
        }
      }
      @media screen and(max-width:1366px){
    .item{
        width: calc(25% - 20px);
      }
  }
    }
    .pagination{
      text-align: right;
    }
    .butBox{
      width: 80px;
      height: 36px;
      border-radius: 4px;
    }
  }
}
</style>
