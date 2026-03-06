<script setup lang="ts">
import { cartItems, cartTotal, removeFromCart } from '../stores/cart'
</script>

<template>
  <div class="cart-view">
    <div class="page-title">購物車</div>

    <div class="cart-content">
      <div v-if="cartItems.length === 0" class="empty-cart">
        <div class="empty-icon">🛒</div>
        <div class="empty-text">購物車是空的</div>
        <RouterLink to="/" class="btn-shop">繼續購物</RouterLink>
      </div>

      <template v-else>
        <table class="cart-table">
          <thead>
            <tr>
              <th>商品名稱</th>
              <th>單價</th>
              <th>數量</th>
              <th>小計</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in cartItems" :key="item.id">
              <td>{{ item.name }}</td>
              <td>NT$ {{ item.price }}</td>
              <td>{{ item.qty }}</td>
              <td>NT$ {{ item.price * item.qty }}</td>
              <td>
                <button class="btn-remove" @click="removeFromCart(item.id)">移除</button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="cart-footer">
          <div class="cart-total">合計：<strong>NT$ {{ cartTotal }}</strong></div>
          <button class="btn-checkout">結帳</button>
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped>
.cart-view {
  padding-bottom: 24px;
}

.page-title {
  background: var(--section-title-bg);
  border-left: 4px solid var(--btn-primary);
  padding: 10px 16px;
  font-weight: bold;
  font-size: 15px;
  color: #6b3a00;
}

.cart-content {
  padding: 16px;
}

.empty-cart {
  text-align: center;
  padding: 60px 0;
}

.empty-icon {
  font-size: 56px;
  margin-bottom: 12px;
}

.empty-text {
  font-size: 16px;
  color: #888;
  margin-bottom: 16px;
}

.btn-shop {
  display: inline-block;
  background: var(--btn-primary);
  color: #fff;
  padding: 8px 24px;
  border-radius: 4px;
  font-size: 14px;
  transition: background 0.2s;
}

.btn-shop:hover {
  background: var(--btn-primary-hover);
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
  margin-bottom: 16px;
}

.cart-table th,
.cart-table td {
  padding: 8px 12px;
  border: 1px solid var(--border-color);
  text-align: left;
}

.cart-table th {
  background: #f5e8c0;
  color: #6b3a00;
  font-weight: bold;
}

.btn-remove {
  background: #c03020;
  color: #fff;
  padding: 3px 10px;
  border-radius: 3px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-remove:hover {
  background: #a02010;
}

.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  padding-top: 12px;
  border-top: 2px solid var(--border-color);
}

.cart-total {
  font-size: 15px;
  color: #333;
}

.cart-total strong {
  color: var(--price-color);
  font-size: 16px;
}

.btn-checkout {
  background: var(--btn-primary);
  color: #fff;
  padding: 8px 28px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  transition: background 0.2s;
}

.btn-checkout:hover {
  background: var(--btn-primary-hover);
}
</style>
