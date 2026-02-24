// load createApp function
import {createApp} from "vue";

// load root compont
import App from "./App.vue";

// create Vue App Object
const app=createApp(App);

// mount to HTML Label
app.mount("#app");