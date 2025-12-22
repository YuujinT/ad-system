import { createApp } from 'vue'
import './style.css'
import './assets/global.css'
import App from './App.vue'
import router from './router'
// Attach FingerprintJS to window for fp-logic.js which expects a global
import * as FingerprintJS from '@fingerprintjs/fingerprintjs'
// Make global available before fp-logic.js runs
window.FingerprintJS = FingerprintJS
import './views/fp-logic.js'

createApp(App).use(router).mount('#app' )
