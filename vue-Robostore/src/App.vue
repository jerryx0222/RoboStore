<script setup lang="ts">
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'
import { cartItems } from './stores/cart'
import { isLoggedIn, currentUser, logout } from './stores/auth'

const route = useRoute()
const router = useRouter()

const cartCount = computed(() => cartItems.value.reduce((sum, i) => sum + i.qty, 0))

function handleLogout() {
  logout()
  router.push('/')
}

const sidebarItems = [
  { label: '主菜', name: '精選便當/餐點', path: '/products/main-dishes' },
  { label: '人氣外送', name: '飲品/果汁', path: '/products/popular' },
  { label: '飲品', name: '手搖/果汁', path: '/products/drinks' },
  { label: '餐點', name: '餐點推薦', path: '/products/meals' },
  { label: '季節特品', name: '秋季特品', path: '/products/seasonal' },
]
</script>

<template>
  <div class="app-wrapper">
    <!-- Header -->
    <header class="site-header">
      <div class="header-inner">
        <div class="logo">
          <RouterLink to="/">
            <div class="logo-icon">R</div>
            <span class="logo-text">RoboStore</span>
          </RouterLink>
        </div>

        <div class="header-right">
          <!-- Login status bar -->
          <div class="auth-bar">
            <template v-if="isLoggedIn">
              <span class="auth-welcome">您好，{{ currentUser!.name }}</span>
              <span class="auth-sep">|</span>
              <button class="btn-auth" @click="handleLogout">登出</button>
            </template>
            <template v-else>
              <RouterLink to="/login" class="btn-auth">登入</RouterLink>
            </template>
          </div>

          <!-- Main nav -->
          <nav class="top-nav">
            <RouterLink to="/" :class="{ active: route.path === '/' }">首頁</RouterLink>
            <RouterLink to="/news" :class="{ active: route.path === '/news' }">最新消息</RouterLink>
            <RouterLink to="/member" :class="{ active: route.path === '/member' }">會員中心</RouterLink>
            <RouterLink to="/cart" :class="{ active: route.path === '/cart' }">購物車({{ cartCount }})</RouterLink>
            <RouterLink to="/contact" :class="{ active: route.path === '/contact' }">聯絡我們</RouterLink>
          </nav>
        </div>
      </div>
    </header>

    <!-- Main layout -->
    <div class="main-layout">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-title">商品分類</div>
        <div
          v-for="item in sidebarItems"
          :key="item.path"
          class="sidebar-item"
          :class="{ 'sidebar-active': route.path === item.path }"
        >
          <RouterLink :to="item.path">
            <span class="sidebar-label">[{{ item.label }}]</span>
            <span class="sidebar-name">{{ item.name }}</span>
          </RouterLink>
        </div>
      </aside>

      <!-- Content -->
      <main class="main-content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Header */
.site-header {
  background-color: var(--header-bg);
  border-bottom: 2px solid #c8a030;
}

.header-inner {
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  gap: 16px;
}

.logo a {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
}

.logo-icon {
  width: 42px;
  height: 42px;
  background: #e07020;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  font-weight: bold;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #6b3a00;
}

.header-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}

/* Auth bar */
.auth-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #6b3a00;
  min-height: 20px;
}

.auth-welcome {
  color: #6b3a00;
}

.auth-sep {
  color: #c8a030;
}

.btn-auth {
  background: none;
  border: none;
  color: #e07020;
  font-size: 12px;
  cursor: pointer;
  padding: 0;
  text-decoration: underline;
  font-family: inherit;
  transition: color 0.2s;
}

.btn-auth:hover {
  color: #c05010;
}

/* Main nav */
.top-nav {
  display: flex;
  gap: 0;
}

.top-nav a {
  display: block;
  padding: 8px 16px;
  color: #5a3a00;
  font-size: 14px;
  border-radius: 4px 4px 0 0;
  transition: background 0.2s;
  border: 1px solid transparent;
}

.top-nav a:hover,
.top-nav a.active {
  background-color: var(--nav-active-bg);
  color: var(--nav-active-text);
  border-color: #a07820;
}

/* Main layout */
.main-layout {
  max-width: 1100px;
  margin: 12px auto;
  display: flex;
  gap: 0;
  width: 100%;
  padding: 0 16px;
  flex: 1;
}

/* Sidebar */
.sidebar {
  width: 180px;
  flex-shrink: 0;
  background: var(--sidebar-bg);
  border-radius: 4px 0 0 4px;
  overflow: hidden;
}

.sidebar-title {
  background: #4f5c2e;
  color: #fff;
  padding: 10px 12px;
  font-weight: bold;
  font-size: 14px;
  text-align: center;
  border-bottom: 1px solid #3d4722;
}

.sidebar-item a {
  display: block;
  padding: 10px 12px;
  color: var(--sidebar-text);
  border-bottom: 1px solid #7a8f46;
  transition: background 0.2s;
  font-size: 13px;
  line-height: 1.4;
}

.sidebar-item a:hover,
.sidebar-active a {
  background: var(--sidebar-active-bg);
}

.sidebar-label {
  display: block;
  font-size: 11px;
  color: #d4e8a0;
  margin-bottom: 2px;
}

.sidebar-name {
  display: block;
}

/* Content */
.main-content {
  flex: 1;
  background: var(--content-bg);
  border: 1px solid #ddd;
  border-left: none;
  border-radius: 0 4px 4px 0;
  min-height: 500px;
  overflow: hidden;
}
</style>
