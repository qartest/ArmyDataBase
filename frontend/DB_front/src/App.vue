<template>
  <div id="app">
    <NavigationBar />
    <router-view></router-view>
  </div>
</template>

<script>
import NavigationBar from './components/NavigationBar.vue'

export default {
  name: 'App',
  components: {
    NavigationBar,
  },
}
</script>

<style>
/* Глобальные стили */
body {
  margin: 0;
  font-family: Arial, sans-serif;
}
</style>