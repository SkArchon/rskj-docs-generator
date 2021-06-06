import Vue from 'vue'
import App from './App.vue'

import VueHighlightJS from 'vue-highlightjs'
import 'highlight.js/styles/tomorrow-night-bright.css' // or other highlight.js theme

// Tell Vue.js to use vue-highlightjs
Vue.use(VueHighlightJS)
Vue.config.productionTip = false

new Vue({
  render: h => h(App)
}).$mount('#app')
