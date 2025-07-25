import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'
import '@/assets/css/common.css'

import { createApp } from 'vue'


import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import piniaPersistedstate from 'pinia-plugin-persistedstate'
import { getMe } from './util/getMe'

async function initApp() {
  const app = createApp(App)

  const pinia = createPinia()
  pinia.use(piniaPersistedstate)

  app.use(pinia)
  try {
    await getMe();
  } catch (err) {
  }

  app.use(router)

  app.mount('#app')
}

initApp();
