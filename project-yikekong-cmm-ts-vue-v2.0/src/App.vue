<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  name: 'App'
})
export default class extends Vue {}
</script>
<style lang="scss">
  .el-button--primary{
    background: #5584FF;
  }
  .el-button + .el-button{
    margin-left: 12px;
  }
  .el-button--small{
    font-size: 14px;
    border-radius: 4px;
  }
  .el-dialog__header{
    text-align: left;
    font-size: 16px;
    font-family: PingFangSC, PingFangSC-Semibold;
    font-weight: 600;
    text-align: left;
  }
  .el-dialog{
    border-radius: 10px;
  }
  .el-input--small .el-input__inner{
    width: 300px;
    height: 36px;
    line-height: 36px;
  }
  .el-form-item__error {
    left: auto;
  }
  .el-form-item__error::after {
    position: absolute;
    content: '!';
    top: 4px;
    right: -5px;
  }
  .el-radio__input.is-checked + .el-radio__label{
    color:#333333;
  }
  .el-radio__inner{
    width: 16px;
    height: 16px;
    border-color: #333333;
  }
  .el-radio__input.is-checked .el-radio__inner{
    width: 16px;
    height: 16px;
    border-color: #333333;
    background: #333333;
  }
  .el-radio__input.is-checked .el-radio__inner::after{
    border:solid 2px #fff;
    background: #333333;
    width: 14px;
    height: 14px;
  }
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
  .pagination{
     text-align: right;
    .el-input__inner, .el-pagination .btn-prev, .el-pagination .btn-next, .el-pager li{
      background: transparent;
    }
  }
  .switch{
      position: absolute;
      right: 20px;
      top:3px;
      .el-switch__core:after{
        width: 10px;
        height: 10px;
      }
      .el-switch__core{
        width: 22px !important;
        height: 14px;
      }
    }
  .el-pagination{
    margin-top: 30px;
  }
  .el-switch.is-checked .el-switch__core::after{
    margin-left: -11px !important;
  }
  .noData{
    position: relative;
    // display: none;
    flex: 1;
    color: #999;
    text-align: center;
    line-height: calc(100vh - 300px);
    &::after{
      content: '没有查询到数据哦~';
      position: absolute;
      top: 35px;
      left: 50%;
      transform: translateX(-50%);
      // line-height: calc(100vh - 220px);
      display: block;
      font-size: 16px;
      text-align: center;
    }
  }
  .show{
    display: inline-block;
  }
</style>
