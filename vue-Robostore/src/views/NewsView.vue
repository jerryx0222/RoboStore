<script setup lang="ts">
const news = [
  {
    id: 1,
    date: '2026-03-01',
    title: '春季新品上市！櫻花季限定飲品',
    content: '三月限定！嚴選春季新鮮食材，推出全新櫻花拿鐵、草莓抹茶等多款春季限定飲品，數量有限，把握機會！',
  },
  {
    id: 2,
    date: '2026-02-20',
    title: '會員生日優惠活動開跑',
    content: '生日當月消費享 85 折優惠，並贈送精美小禮物一份。登入會員中心確認您的生日優惠資格！',
  },
  {
    id: 3,
    date: '2026-02-10',
    title: '外送服務範圍擴大至全台',
    content: '即日起，外送服務擴大至全台各縣市，讓您在家也能享用我們精心製作的美食！',
  },
  {
    id: 4,
    date: '2026-01-28',
    title: '農曆新年營業時間公告',
    content: '農曆年假期間（2/8 - 2/12）正常營業，歡迎線上訂購！年菜預購也開始接受預訂。',
  },
]
</script>

<template>
  <div class="news-view">
    <div class="page-title">最新消息</div>

    <div class="news-list">
      <div v-for="item in news" :key="item.id" class="news-item">
        <div class="news-date">{{ item.date }}</div>
        <div class="news-title">{{ item.title }}</div>
        <div class="news-content">{{ item.content }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.news-view {
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

.news-list {
  padding: 12px 16px;
}

.news-item {
  padding: 14px 0;
  border-bottom: 1px solid var(--border-color);
}

.news-item:last-child {
  border-bottom: none;
}

.news-date {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.news-title {
  font-weight: bold;
  font-size: 14px;
  color: #6b3a00;
  margin-bottom: 6px;
}

.news-content {
  font-size: 13px;
  color: #555;
  line-height: 1.6;
}
</style>
