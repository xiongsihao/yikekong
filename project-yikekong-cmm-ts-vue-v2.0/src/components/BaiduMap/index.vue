<template>
  <div class="baidu-map">
    <baidu-map
      class="map"
      ref="map"
      :center="center"
      :zoom="zoom"
      :scroll-wheel-zoom="true"
      @zoomend="syncCenterAndZoom"
      @moveend="syncCenterAndZoom"
    > 
      <bm-marker 
       v-for="(item,index) in pointArr"
        :key="index"
        :position="{lat: item.lng, lng:item.lat}"
        :icon="{
          url: item.level == 1 
          ? require('./../../assets/img/map.png')
          : item.level.online ? require('./../../assets/img/mapBlue.png') : require('./../../assets/img/mapGray.png')
          , size: {width: 26, height: 30}
        }"
        @mouseover="infoWindowOpen(item)"
        @mouseout="infoWindowClose(item)"
        @click="bmMarkerClick"
        style="position:relative"
        >
        <bm-info-window :show="item.st" @close="infoWindowClose" style="width:250px" class="infoBox" @open="infoWindowOpen">
           <p><span class="tit">设备编号：</span> <span>{{item.deviceId}}</span></p>
           <p><span class="tit">设备状态：</span><span>{{item.online ? '在线' : '离线'}}</span></p>
           <p class="list"><span class="tit">报警指标：</span>
              <ul><li v-for="(it,ind) in item.quotaList" :key="ind">{{it.quotaName}} {{it.value}} {{it.unit}}</li></ul>
           </p>
        </bm-info-window>
     </bm-marker>
    </baidu-map>
  </div>
</template>

<script lang="ts">
import Baidu from "vue-baidu-map";
import BaiduMap from "vue-baidu-map/components/map/Map.vue";
import { Component, Vue, Emit, Prop, Watch } from "vue-property-decorator";
import './../../assets/img/map.png';

Vue.use(Baidu, {
  ak: "GiFl9MH70WYaGrvN1Gv5K5ZA84E1uqvt"
});

@Component({
  name: "BaiduMaps"
})

export default class extends Vue {
  @Prop({ default: []}) private data!: Array<any>;

  private center = {lng: 116.400244, lat: 39.92556 };
  private zoom = 7;
  private pointArr: any = [];
  private show: boolean = false;

  @Watch('data')
  onPersonChanged1(val: any, oldVal: any) {
    this.addPoints();
  }

  infoWindowClose (item:any) {
    item.st = false
  }
  infoWindowOpen (item:any) {
    item.st = true
  }

  addPoints() {
    const points:any[] = [];
    this.data.forEach((res: any) => {
      res.level = 0
      res['quotaList'].forEach((val:any) => {val.level > 0 ? res.level = 1 : null} )
      points.push({...res, lng: res.location.split(',')[0], lat:res.location.split(',')[1], st: false})
    })
    this.pointArr = [...points];
    // console.log(this.pointArr)
  }
  private initCharts() {
    console.log("BaiduMap");
  }
  private syncCenterAndZoom(e: any) {
    // console.log(e.target.getZoom());
    // console.log(e.target.getCenter());
  }

  @Emit("bmMarkerCallback")
  bmMarker(data: object) {
    return data;
  }

  private bmMarkerClick(e: object) {
    this.bmMarker(e);
  }

  mounted() {
    // this.addPoints();
  }
}
</script>
<style lang="scss">
/* 消息内容 */
.BMap_bubble_content {
	background-color:white;
}
/* 隐藏边角 */
.BMap_top, .BMap_bottom, .BMap_center, .BMap_pop div:nth-child(1) div {
	display:none;
}
.BMap_pop div:nth-child(3), .BMap_pop div:nth-child(4), .BMap_pop div:nth-child(5), .BMap_pop div:nth-child(7), .BMap_pop div:nth-child(8){
	  display: none;
}
.BMap_pop div:nth-child(9){
  top: 42px !important;
  overflow: inherit !important;
}
.BMap_pop div:nth-child(9) img{
  display: none !important;
}
.BMap_pop img {
	top:43px !important;
	left:215px !important;
  z-index: -99 !important;
}
.BMap_shadow div img{
  display: none !important;
}
.infoBox {
  position: absolute;
  background: #fff;
  padding: 10px 14px;
  border-radius: 3px;
  font-size: 14px;
  line-height: 25px;
  box-shadow: 0px 0px 10px rgba(0,0,0,0.2);
  color: #666;
  p, ul, li {
    padding: 0;
    margin: 0;
    list-style: none;
  }
  .tit{
    display: inline-block;
    color: #999;
    width: 72px !important;
  }
  .list{
    display: flex;
  }
  &::before{
    position: absolute;
    content: '';
    bottom: -4px;
    left: 36%;
    width: 10px;
    height: 10px;
    transform: rotateZ(45deg);
    border-radius: 3px;
    background: #fff;
    border: solid 1px #fff;
  }
}
</style>
<style lang="scss" scoped>

.baidu-map {
  width: 100%;
  height: 100%;
  .map {
    width: 100%;
    height: 100%;
  }
}
</style>
