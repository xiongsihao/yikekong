<template>
  <section class="app-main">
    <router-view />
    <!-- <transition
      name="fade-transform"
      mode="out-in"
    >
      <router-view />
    </transition> -->
  </section>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({
  name: "AppMain"
})
export default class extends Vue {}
</script>

<style lang="scss" scoped>
.app-main {
  position: relative;
  background-color: #f3f4f7;
}
</style>
