<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { addToCart } from '../stores/cart'

const route = useRoute()

const categoryTitles: Record<string, string> = {
  'main-dishes': '精選便當/餐點',
  popular: '人氣外送',
  drinks: '飲品 > 手搖/果汁',
  meals: '餐點推薦',
  seasonal: '秋季特品',
}

const categoryTitle = computed(() => categoryTitles[route.params.category as string] || '商品列表')

interface Product {
  id: number
  name: string
  price: number
  color: string
  emoji: string
}

const allProducts: Record<string, Product[]> = {
  drinks: [
    { id: 101, name: '招牌珍珠奶茶', price: 65, color: '#c8864a', emoji: '🧋' },
    { id: 102, name: '宇治抹茶拿鐵', price: 75, color: '#6a8a3a', emoji: '🍵' },
    { id: 103, name: '宇治抹茶星冰樂', price: 80, color: '#7a9a40', emoji: '🍵' },
    { id: 104, name: '草莓果昔', price: 70, color: '#d45870', emoji: '🍓' },
    { id: 105, name: '百香果綠茶', price: 60, color: '#c8a820', emoji: '🌿' },
    { id: 106, name: '芒果多多', price: 70, color: '#e8a820', emoji: '🥭' },
    { id: 107, name: '蜂蜜檸檬茶', price: 55, color: '#e8d040', emoji: '🍋' },
    { id: 108, name: '黑糖珍珠鮮奶', price: 75, color: '#6a4820', emoji: '🥛' },
    { id: 109, name: '玫瑰荔枝氣泡飲', price: 75, color: '#e8789a', emoji: '🌹' },
  ],
  'main-dishes': [
    { id: 201, name: '蜂蜜烤雞腿便當', price: 120, color: '#8a6030', emoji: '🍱' },
    { id: 202, name: '蒜香排骨便當', price: 130, color: '#a05030', emoji: '🍱' },
    { id: 203, name: '紅燒牛腩便當', price: 150, color: '#8a3020', emoji: '🍱' },
    { id: 204, name: '薑燒豬肉便當', price: 110, color: '#c06030', emoji: '🍱' },
    { id: 205, name: '鮭魚定食', price: 140, color: '#e87040', emoji: '🐟' },
    { id: 206, name: '炸雞腿定食', price: 125, color: '#c87020', emoji: '🍗' },
  ],
  popular: [
    { id: 301, name: '韓式炸雞', price: 160, color: '#c04020', emoji: '🍗' },
    { id: 302, name: '日式拉麵', price: 180, color: '#a06020', emoji: '🍜' },
    { id: 303, name: '台式肉燥飯', price: 80, color: '#8a5030', emoji: '🍚' },
    { id: 304, name: '越式河粉', price: 130, color: '#d4a050', emoji: '🍜' },
    { id: 305, name: '泰式綠咖哩', price: 140, color: '#5a8a30', emoji: '🥘' },
    { id: 306, name: '牛肉漢堡', price: 150, color: '#c06020', emoji: '🍔' },
  ],
  meals: [
    { id: 401, name: '主廚今日推薦', price: 145, color: '#7a5020', emoji: '👨‍🍳' },
    { id: 402, name: '椒麻雞絲冷麵', price: 110, color: '#4a7a3a', emoji: '🍜' },
    { id: 403, name: '麻婆豆腐飯', price: 100, color: '#c04830', emoji: '🍚' },
    { id: 404, name: '三杯雞定食', price: 135, color: '#8a4020', emoji: '🍗' },
  ],
  seasonal: [
    { id: 501, name: '南瓜濃湯套餐', price: 120, color: '#d86020', emoji: '🎃' },
    { id: 502, name: '栗子蒙布朗', price: 90, color: '#8a6030', emoji: '🌰' },
    { id: 503, name: '秋柿牛奶凍', price: 65, color: '#e87040', emoji: '🍮' },
    { id: 504, name: '蘋果派', price: 75, color: '#c05030', emoji: '🥧' },
  ],
}

const PAGE_SIZE = 9
const currentPage = ref(1)

const products = computed(() => allProducts[route.params.category as string] || [])
const totalPages = computed(() => Math.ceil(products.value.length / PAGE_SIZE))
const pagedProducts = computed(() => {
  const start = (currentPage.value - 1) * PAGE_SIZE
  return products.value.slice(start, start + PAGE_SIZE)
})

function goPage(p: number) {
  if (p >= 1 && p <= totalPages.value) currentPage.value = p
}
</script>

<template>
  <div class="products-view">
    <div class="page-title">{{ categoryTitle }}</div>

    <div class="product-grid">
      <div v-for="p in pagedProducts" :key="p.id" class="product-card">
        <div class="product-img" :style="{ backgroundColor: p.color }">
          <span class="product-emoji">{{ p.emoji }}</span>
        </div>
        <div class="product-info">
          <div class="product-name">{{ p.name }}</div>
          <div class="product-price">NT$ {{ p.price }}</div>
          <button class="btn-cart" @click="addToCart(p)">加入購物車</button>
        </div>
      </div>
    </div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" class="pagination">
      <button
        v-for="p in totalPages"
        :key="p"
        class="page-btn"
        :class="{ active: currentPage === p }"
        @click="goPage(p)"
      >
        {{ p }}
      </button>
      <button class="page-btn" @click="goPage(currentPage + 1)" :disabled="currentPage >= totalPages">
        下一頁
      </button>
    </div>
  </div>
</template>

<style scoped>
.products-view {
  padding: 0 0 24px;
}

.page-title {
  background: var(--section-title-bg);
  border-left: 4px solid var(--btn-primary);
  padding: 10px 16px;
  font-weight: bold;
  font-size: 15px;
  color: #6b3a00;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 16px;
}

.product-card {
  border: 1px solid var(--border-color);
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  transition: box-shadow 0.2s;
}

.product-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
}

.product-img {
  width: 100%;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-emoji {
  font-size: 42px;
}

.product-info {
  padding: 8px 10px;
}

.product-name {
  font-size: 13px;
  color: #333;
  margin-bottom: 4px;
  line-height: 1.3;
}

.product-price {
  font-size: 14px;
  font-weight: bold;
  color: var(--price-color);
  margin-bottom: 6px;
}

.btn-cart {
  width: 100%;
  background: var(--btn-cart);
  color: #fff;
  padding: 5px 0;
  border-radius: 3px;
  font-size: 12px;
  transition: background 0.2s;
}

.btn-cart:hover {
  background: var(--btn-primary-hover);
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  gap: 6px;
  padding: 16px;
  border-top: 1px solid var(--border-color);
}

.page-btn {
  padding: 4px 12px;
  border: 1px solid #c8a030;
  border-radius: 3px;
  background: #fff;
  color: #6b3a00;
  font-size: 13px;
  cursor: pointer;
  transition: background 0.2s;
}

.page-btn:hover,
.page-btn.active {
  background: var(--nav-active-bg);
  color: #fff;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: default;
}
</style>
