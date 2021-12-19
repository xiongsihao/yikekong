<template>
  <div class="totle-container">
      <div class="cont">
          <div class="num">{{data.deviceCount}}</div>
          <div>设备数量</div>
      </div>
      <div class="cont noBord">
          <div class="num">{{data.alarmCount}}</div>
          <div>报警设备数</div>
      </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
  name: "totleCont"
})
export default class extends Vue {
  @Prop({ default: {deviceCount: '0', alarmCount: '0'}}) private data!: object;
}
</script>

<style lang="scss" scoped>
.totle-container {
    position: absolute;
    display: flex;
    top: 80PX;
    left: 60PX;
    z-index: 99;
    width: 320px;
    padding: 30px 0;
    opacity: 0.85;
    background: #356cfa;
    color: rgba(255,255,255,0.61);
    border-radius: 6px;
    box-shadow: 0px 2px 12px 0px rgba(0,0,0,0.1); 
    font-size: 12px;
    justify-content: center;
    .cont{
        position: relative;
        flex: 1;
        text-align: left;
        line-height: 30px;
        height: 60px;
        div{
            padding:0 30px;
        }
        &::after{
            content:'';
            position: absolute;
            top: 5px;
            height: 50px;
            border-right: 1px solid rgba(242,242,242,0.15);
        }
    }
    .noBord{
        border:none
    }
    .num{
        font-size: 24px;
        font-weight: 600;
        color: #fff;
    }
}
</style>
