<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { addToCart } from '../stores/cart'
import { fetchProducts, type Product } from '../api'

const route = useRoute()

const categoryTitles: Record<string, string> = {
  'main-dishes': '精選便當/餐點',
  popular: '人氣外送',
  drinks: '飲品 > 手搖/果汁',
  meals: '餐點推薦',
  seasonal: '秋季特品',
}

const categoryTitle = computed(() => categoryTitles[route.params.category as string] || '商品列表')

const products = ref<Product[]>([])
const loading = ref(false)
const error = ref('')

async function loadProducts(category: string) {
  loading.value = true
  error.value = ''
  try {
    products.value = await fetchProducts(category)
  } catch {
    error.value = '無法載入商品，請確認後端服務是否啟動'
  } finally {
    loading.value = false
  }
}

const PAGE_SIZE = 9
const currentPage = ref(1)

watch(
  () => route.params.category as string,
  (cat) => { currentPage.value = 1; loadProducts(cat) },
  { immediate: true },
)

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

    <div v-if="loading" class="status-msg">載入中...</div>
    <div v-else-if="error" class="status-msg error">{{ error }}</div>

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
