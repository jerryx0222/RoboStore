<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { addToCart } from '../stores/cart'
import { fetchHomeProducts, type Product } from '../api'

const router = useRouter()

const newProducts = ref<Product[]>([])
const chefRecommends = ref<Product[]>([])

onMounted(async () => {
  try {
    const data = await fetchHomeProducts()
    newProducts.value = data.newProducts
    chefRecommends.value = data.chefRecommends
  } catch {
    // 保持空陣列，不顯示商品
  }
})

function goOrder() {
  router.push('/products/popular')
}
</script>

<template>
  <div class="home">
    <!-- Hero Banner -->
    <div class="hero-banner">
      <div class="hero-text">
        <h1 class="hero-title">熱門排隊美食</h1>
        <p class="hero-sub">現點現做，暖心直送</p>
        <button class="btn-order" @click="goOrder">立刻訂購</button>
      </div>
      <div class="hero-img">
        <div class="food-placeholder hero-food"></div>
      </div>
    </div>

    <!-- 本週新品排行 -->
    <section class="section">
      <div class="section-header">
        <span>本週新品排行</span>
      </div>
      <div class="product-grid">
        <div v-for="p in newProducts" :key="p.id" class="product-card">
          <div class="product-img" :style="{ backgroundColor: p.color }">
            <span class="product-emoji">🍽</span>
          </div>
          <div class="product-info">
            <div class="product-name">{{ p.name }}</div>
            <div class="product-price">NT$ {{ p.price }}</div>
            <button class="btn-cart" @click="addToCart(p)">加入購物車</button>
          </div>
        </div>
      </div>
    </section>

    <!-- 主廚推薦 -->
    <section class="section">
      <div class="section-header">
        <span>主廚推薦</span>
      </div>
      <div class="product-grid">
        <div v-for="p in chefRecommends" :key="p.id" class="product-card">
          <div class="product-img" :style="{ backgroundColor: p.color }">
            <span class="product-emoji">👨‍🍳</span>
          </div>
          <div class="product-info">
            <div class="product-name">{{ p.name }}</div>
            <div class="product-price">NT$ {{ p.price }}</div>
            <button class="btn-cart" @click="addToCart(p)">加入購物車</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home {
  padding-bottom: 24px;
}

/* Hero */
.hero-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #f5deb0 0%, #e8c880 100%);
  padding: 24px 32px;
  min-height: 180px;
  gap: 16px;
}

.hero-text {
  flex: 1;
}

.hero-title {
  font-size: 26px;
  font-weight: bold;
  color: #6b3a00;
  margin-bottom: 6px;
}

.hero-sub {
  font-size: 14px;
  color: #8a5a20;
  margin-bottom: 16px;
}

.btn-order {
  background: var(--btn-primary);
  color: #fff;
  padding: 8px 24px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  transition: background 0.2s;
}

.btn-order:hover {
  background: var(--btn-primary-hover);
}

.hero-img {
  flex-shrink: 0;
}

.hero-food {
  width: 180px;
  height: 130px;
  border-radius: 8px;
  background: linear-gradient(135deg, #c87a30 0%, #e8a050 50%, #c05020 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 60px;
}

.hero-food::after {
  content: '🍗';
}

/* Section */
.section {
  padding: 0 16px 16px;
}

.section-header {
  background: var(--section-title-bg);
  border-left: 4px solid var(--btn-primary);
  padding: 8px 12px;
  font-weight: bold;
  font-size: 15px;
  color: #6b3a00;
  margin-bottom: 12px;
  margin-top: 16px;
}

/* Product Grid */
.product-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
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
  height: 110px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-emoji {
  font-size: 36px;
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
</style>
