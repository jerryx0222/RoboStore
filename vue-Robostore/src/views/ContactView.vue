<script setup lang="ts">
import { ref } from 'vue'

const form = ref({
  name: '',
  email: '',
  content: '',
})

const submitted = ref(false)

function submitForm() {
  if (!form.value.name || !form.value.email || !form.value.content) {
    alert('請填寫所有欄位')
    return
  }
  submitted.value = true
  form.value = { name: '', email: '', content: '' }
}
</script>

<template>
  <div class="contact-view">
    <div class="page-title">聯絡我們</div>

    <div class="contact-layout">
      <!-- Left: contact info -->
      <div class="contact-info">
        <div class="info-block">
          <div class="info-label">服務電話</div>
          <div class="info-value">0800-000-000</div>
        </div>
        <div class="info-block">
          <div class="info-label">服務地址</div>
          <div class="info-value">台北市信義區信義路五段7號</div>
        </div>
        <div class="info-block">
          <div class="info-label">電子信箱</div>
          <div class="info-value">service@robostore.com.tw</div>
        </div>
        <div class="info-block">
          <div class="info-label">服務時間</div>
          <div class="info-value">週一至週五 09:00 - 18:00</div>
        </div>
      </div>

      <!-- Right: map placeholder -->
      <div class="map-area">
        <div class="map-placeholder">
          <div class="map-icon">📍</div>
          <div class="map-text">地圖位置</div>
        </div>
      </div>
    </div>

    <!-- Online message form -->
    <div class="form-section">
      <div class="section-subtitle">線上留言</div>

      <div v-if="submitted" class="success-msg">
        感謝您的留言！我們將盡快回覆您。
      </div>

      <form v-else class="contact-form" @submit.prevent="submitForm">
        <div class="form-row">
          <label class="form-label">姓名</label>
          <input
            v-model="form.name"
            type="text"
            class="form-input"
            placeholder="請輸入姓名"
          />
        </div>
        <div class="form-row">
          <label class="form-label">信箱</label>
          <input
            v-model="form.email"
            type="email"
            class="form-input"
            placeholder="請輸入電子信箱"
          />
        </div>
        <div class="form-row">
          <label class="form-label">內容</label>
          <textarea
            v-model="form.content"
            class="form-textarea"
            rows="5"
            placeholder="請輸入留言內容"
          ></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn-submit">提交</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.contact-view {
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

.contact-layout {
  display: flex;
  gap: 16px;
  padding: 16px 16px 0;
}

/* Contact Info */
.contact-info {
  flex: 1;
}

.info-block {
  margin-bottom: 14px;
  padding-bottom: 14px;
  border-bottom: 1px solid #eee;
}

.info-block:last-child {
  border-bottom: none;
}

.info-label {
  font-weight: bold;
  color: #6b3a00;
  font-size: 13px;
  margin-bottom: 4px;
}

.info-value {
  color: #555;
  font-size: 13px;
}

/* Map */
.map-area {
  flex: 1;
}

.map-placeholder {
  background: #e8eee0;
  border: 2px solid #c0cc90;
  border-radius: 4px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #6b7a3d;
  gap: 8px;
}

.map-icon {
  font-size: 40px;
}

.map-text {
  font-size: 14px;
  color: #6b7a3d;
}

/* Form */
.form-section {
  padding: 0 16px;
  margin-top: 16px;
}

.section-subtitle {
  font-weight: bold;
  font-size: 14px;
  color: #6b3a00;
  background: #f5e8c0;
  padding: 6px 10px;
  border-left: 3px solid var(--btn-primary);
  margin-bottom: 14px;
}

.contact-form {
  max-width: 500px;
}

.form-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.form-label {
  width: 60px;
  flex-shrink: 0;
  font-size: 13px;
  color: #555;
  padding-top: 6px;
}

.form-input,
.form-textarea {
  flex: 1;
  border: 1px solid var(--border-color);
  border-radius: 3px;
  padding: 6px 10px;
  font-size: 13px;
  font-family: inherit;
  outline: none;
  transition: border-color 0.2s;
}

.form-input:focus,
.form-textarea:focus {
  border-color: #c8a030;
}

.form-textarea {
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-start;
  padding-left: 72px;
}

.btn-submit {
  background: var(--btn-primary);
  color: #fff;
  padding: 7px 28px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: bold;
  transition: background 0.2s;
}

.btn-submit:hover {
  background: var(--btn-primary-hover);
}

.success-msg {
  background: #d4f0d4;
  color: #2a6a2a;
  padding: 12px 16px;
  border-radius: 4px;
  border: 1px solid #a0c8a0;
  font-size: 14px;
}
</style>
