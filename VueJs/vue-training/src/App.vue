<script setup>
    console.log("我的第一個組件");
    let data="Hell1";
    let htmlData="<u>Hello2</u>"
    let name="content";
    let bData=true;

</script>

<template>
    <nav>基本的導列</nav>
    <main>基本的主內容</main>
    <div>{{data}}</div>
    <div v-html="htmlData"></div>
    <div v-bind:class="name">Hello3</div>
    <div :class="name">Hello4</div>
    <div>{{ data.toUpperCase() }}</div>
    <div :class="bData?'dark':'light'">Hello5</div>

</template>

<style scoped>
    main{background-color:#dddddd;}
</style>