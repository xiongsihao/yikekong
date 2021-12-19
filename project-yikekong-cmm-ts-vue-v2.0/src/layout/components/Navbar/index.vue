<template>
  <div class="navbar">
    <!-- <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    /> -->
    <!-- <breadcrumb
      id="breadcrumb-container"
      class="breadcrumb-container"
    /> -->
    <div class="logo"><img src="./../../../assets/loginIcon.png" width="62" alt=""></div>
    <div
      :class="{'router-item':true,'router-item-active': routerIndex == index}"
      v-for="(item,index) in routerArr"
      :key="index"
      @click="routerItemClick(index)"
    ><span :class="['iconfont',item.icon]"></span>{{item.name}}</div>
    <div class="right-menu">
      <el-dropdown
        class="avatar-container right-menu-item hover-effect"
        trigger="click"
      >
        <div class="avatar-wrapper">
          <img
            src="./../../../assets/img/Avatar.png"
            class="user-avatar"
          >
          <span class="title">Admin</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <span
              style="display:block;"
              @click="logout"
            >退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { AppModule } from "@/store/modules/app";
import { UserModule } from "@/store/modules/user";
import Breadcrumb from "@/components/Breadcrumb/index.vue";
import Hamburger from "@/components/Hamburger/index.vue";
import { watch } from "fs";

@Component({
  name: "Navbar",
  components: {
    Breadcrumb,
    Hamburger
  }
})
export default class extends Vue {
  private routerIndex = 0;
  private routerArr = [
    {
      name: "首页",
      icon: 'icon-shouyebeifen2',
      url: "/dashboard"
    },
    {
      name: "数据详情",
      icon: 'icon-shuju2',
      url: "/data/info"
    },
    {
      name: "设备管理",
      icon: 'icon-shezhi',
      url: "/equipment/list"
    },
    {
      name: "报警管理",
      icon: 'icon-baojingalarm8beifen',
      url: "/alarmLog/list"
    }
  ];

  //路由改变时选中状态
  // selected == routerArr下标
  private routerBrr = [
    {
      selected: 0,
      url: "/dashboard"
    },
    {
      selected: 0,
      url: "/dashboardDetails"
    },
    {
      selected: 0,
      url: "/addDashboard"
    },
    {
      selected: 1,
      url: "/data/info"
    },
    {
      selected: 2,
      url: "/equipment/list"
    },
    {
      selected: 2,
      url: "/fingerMark/list"
    },
    {
      selected: 3,
      url: "/alarmLog/list"
    },
    {
      selected: 3,
      url: "/alarmManage/list"
    }
  ];

  @Watch("$route")
  onChangeValue() {
    this.routerSelectd();
  }

  mounted() {
    //默认选中
    this.routerSelectd();
  }

  private routerSelectd() {
    this.routerBrr.forEach((item, index) => {
      if (this.$route.path == item.url) {
        this.routerIndex = item.selected;
      }
    });
  }

  get sidebar() {
    return AppModule.sidebar;
  }

  get device() {
    return AppModule.device.toString();
  }

  get avatar() {
    return UserModule.avatar;
  }

  private toggleSideBar() {
    AppModule.ToggleSideBar(false);
  }

  private async logout() {
    this.$confirm('确认退出系统?', '', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    }).then(async () => {
       await UserModule.LogOut();
       this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    }).catch((err:any) => {
      console.log('取消操作！')
    })
  }

  //导航点击
  private routerItemClick(index: any) {
    if (!this.routerArr[index].url) {
      return;
    }
    if (this.$route.path != this.routerArr[index].url) {
      this.routerIndex = index;
      this.$router.push(this.routerArr[index].url);
    }
  }
}
</script>
<style lang="scss">
.el-message-box{
    width: 322px;
    padding: 50px 0;
    text-align: center;
    border-radius: 10px;
}
.el-message-box__message{
  font-size: 20px;
  font-weight: 600px;
  line-height: 40px;
  margin-bottom: 20px;
}
.el-message-box__header{
  display: none;
}
.el-message-box__btns{
  text-align: center;
}
.el-message-box__btns button:nth-child(1){
  width: 80px;
  height: 36px;
  border: 1px solid #5584ff;
  border-radius: 4px;
  color:#5584ff;
}
.el-message-box__btns button:nth-child(2){
  margin-left: 30px;
  width: 80px;
  height: 36px;
  background: #5584ff;
  border-radius: 4px;
}
</style>
<style lang="scss" scoped>
.navbar {
  overflow: hidden;
  position: fixed;
  top:0px;
  z-index: 999;
  width: 100%;
  height: 66px;
  background: #333550;
  box-shadow: 6px 3px 10px 0px rgba(0,0,0,0.05); 
  display: flex;
  align-items: center;
  .iconfont{
    margin-right: 10px;
    font-weight: bold;
  }
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    padding: 0 15px;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .logo {
    width: 155px;
    position: relative;
    top: -1px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: #007aff;
    font-weight: 600;
    margin-right: 20px;
  }
  .router-item {
    position: relative;
    // width: 100px;
    margin-right: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
  }
  .router-item-active::before{
    content: '';
    display: inline-block;
    position: absolute;
    width: 28px;
    height: 2px;
    background: #007aff;
    left: calc(50% + 12px);
    top: calc(50% + 15px);
    transform: translateX(-50%);
  }

  .right-menu {
    position: absolute;
    right: 0;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;
      display: flex;
      align-items: center;
      .avatar-wrapper {
        position: relative;
        display: flex;
        align-items: center;
        .title{
          font-size: 18px;
          color: #fff;
          margin: 0 5px;
        }
        .user-avatar {
          cursor: pointer;
          width: 36px;
          height: 36px;
          border-radius: 100%;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
