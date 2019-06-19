//引入vue框架
import Vue from 'vue'
//引入根组件
import App from './App'
//引入路由设置
import router from './router'
//完整引入ElementUI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import './styles/element-variables.scss'
import 'font-awesome/css/font-awesome.min.css'
import './utils/filter_utils.js'

//使用ElementUI
Vue.use(ElementUI)
//关闭生产模式下给出的提示
Vue.config.productionTip = false
window.bus = new Vue();
//定义实例
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  //引入的根组件
  components: {App}
})
