<script setup lang="ts">
import { ref, computed } from 'vue'
import { currentUser } from '../stores/auth'

const member = computed(() => currentUser.value!)

const orders = ref([
  { id: '163001', date: '2023-03-22', amount: 1500, status: '已批准', statusClass: 'approved' },
  { id: '163770', date: '2023-03-28', amount: 1500, status: '配達中', statusClass: 'shipping' },
  { id: '163001', date: '2023-04-10', amount: 1100, status: '已批准', statusClass: 'approved' },
])

const selectedOrder = ref(orders.value[0]!)

function selectOrder(order: (typeof orders.value)[0]) {
  selectedOrder.value = order
}
</script>

<template>
  <div class="member-view">
    <div class="page-title">會員中心，您好！{{ member.name }}</div>

    <div class="member-layout">
      <!-- Left: member info + orders -->
      <div class="member-left">
        <table class="info-table">
          <tbody>
            <tr>
              <th>您的帳號</th>
              <td>{{ member.account }}</td>
            </tr>
            <tr>
              <th>帳戶名稱</th>
              <td>{{ member.name }}</td>
            </tr>
            <tr>
              <th>會員等級</th>
              <td>{{ member.level }}</td>
            </tr>
            <tr>
              <th>加入日期</th>
              <td>{{ member.joinDate }}</td>
            </tr>
          </tbody>
        </table>

        <div class="section-subtitle">訂單紀錄</div>
        <table class="orders-table">
          <thead>
            <tr>
              <th>訂單編號</th>
              <th>訂購日期</th>
              <th>金額</th>
              <th>配送狀況</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(order, idx) in orders"
              :key="idx"
              :class="{ selected: selectedOrder === order }"
              @click="selectOrder(order)"
            >
              <td>{{ order.id }}</td>
              <td>{{ order.date }}</td>
              <td>NT$ {{ order.amount }}</td>
              <td>
                <span class="status-badge" :class="order.statusClass">{{ order.status }}</span>
              </td>
              <td>
                <button class="btn-detail" @click.stop="selectOrder(order)">查看訂單</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Right: order detail -->
      <div class="member-right">
        <div class="order-detail">
          <div class="detail-title">訂單詳情</div>
          <table class="info-table">
            <tbody>
              <tr>
                <th>訂單編號</th>
                <td>{{ selectedOrder.id }}</td>
              </tr>
              <tr>
                <th>訂購日期</th>
                <td>{{ selectedOrder.date }}</td>
              </tr>
              <tr>
                <th>訂單金額</th>
                <td>NT$ {{ selectedOrder.amount }}</td>
              </tr>
              <tr>
                <th>配送狀況</th>
                <td>
                  <span class="status-badge" :class="selectedOrder.statusClass">{{
                    selectedOrder.status
                  }}</span>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="detail-items">
            <div class="detail-subtitle">訂購商品</div>
            <div class="detail-item">
              <span>招牌珍珠奶茶 x2</span>
              <span>NT$ 130</span>
            </div>
            <div class="detail-item">
              <span>蜂蜜烤雞腿便當 x1</span>
              <span>NT$ 120</span>
            </div>
            <div class="detail-item total">
              <span>合計</span>
              <span>NT$ {{ selectedOrder.amount }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.member-view {
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

.member-layout {
  display: flex;
  gap: 16px;
  padding: 16px;
  align-items: flex-start;
}

.member-left {
  flex: 1;
  min-width: 0;
}

.member-right {
  width: 240px;
  flex-shrink: 0;
}

/* Tables */
.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
  font-size: 13px;
}

.info-table th,
.info-table td {
  padding: 7px 10px;
  border: 1px solid var(--border-color);
  text-align: left;
}

.info-table th {
  background: #f5e8c0;
  color: #6b3a00;
  width: 35%;
  font-weight: bold;
}

.section-subtitle {
  font-weight: bold;
  font-size: 14px;
  color: #6b3a00;
  background: #f5e8c0;
  padding: 6px 10px;
  border-left: 3px solid var(--btn-primary);
  margin-bottom: 8px;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.orders-table th,
.orders-table td {
  padding: 7px 10px;
  border: 1px solid var(--border-color);
  text-align: left;
}

.orders-table th {
  background: #f5e8c0;
  color: #6b3a00;
  font-weight: bold;
}

.orders-table tr.selected td {
  background: #fff8e8;
}

.orders-table tbody tr {
  cursor: pointer;
  transition: background 0.15s;
}

.orders-table tbody tr:hover td {
  background: #fdf5e0;
}

.status-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
}

.status-badge.approved {
  background: #d4f0d4;
  color: #2a6a2a;
}

.status-badge.shipping {
  background: #d4e8f8;
  color: #1a4a8a;
}

.btn-detail {
  background: #6b7a3d;
  color: #fff;
  padding: 3px 10px;
  border-radius: 3px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-detail:hover {
  background: #4f5c2e;
}

/* Right panel */
.order-detail {
  border: 1px solid var(--border-color);
  border-radius: 4px;
  overflow: hidden;
}

.detail-title {
  background: #6b7a3d;
  color: #fff;
  padding: 8px 12px;
  font-weight: bold;
  font-size: 14px;
}

.order-detail .info-table {
  margin: 0;
}

.detail-items {
  padding: 10px 12px;
  border-top: 1px solid var(--border-color);
}

.detail-subtitle {
  font-weight: bold;
  font-size: 13px;
  color: #6b3a00;
  margin-bottom: 8px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #555;
  padding: 3px 0;
  border-bottom: 1px dashed #eee;
}

.detail-item.total {
  font-weight: bold;
  color: var(--price-color);
  border-bottom: none;
  margin-top: 4px;
  font-size: 13px;
}
</style>
