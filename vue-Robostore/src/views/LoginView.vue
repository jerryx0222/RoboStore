<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { login } from '../stores/auth'

const router = useRouter()
const route = useRoute()

const account = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleLogin() {
  errorMsg.value = ''
  if (!account.value || !password.value) {
    errorMsg.value = '請填寫帳號與密碼'
    return
  }
  loading.value = true
  const ok = await login(account.value, password.value)
  loading.value = false
  if (ok) {
    const redirect = (route.query.redirect as string) || '/member'
    router.push(redirect)
  } else {
    errorMsg.value = '帳號或密碼錯誤，請重新輸入'
  }
}
</script>

<template>
  <div class="login-view">
    <div class="page-title">會員登入</div>

    <div class="login-box">
      <div class="login-header">
        <div class="login-icon">👤</div>
        <h2>會員登入</h2>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-row">
          <label class="form-label">帳號</label>
          <input
            v-model="account"
            type="text"
            class="form-input"
            placeholder="請輸入電子信箱"
            autocomplete="username"
          />
        </div>
        <div class="form-row">
          <label class="form-label">密碼</label>
          <input
            v-model="password"
            type="password"
            class="form-input"
            placeholder="請輸入密碼"
            autocomplete="current-password"
          />
        </div>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? '登入中...' : '登入' }}
        </button>
      </form>

      <div class="login-hint">
        <div class="hint-title">測試帳號</div>
        <div class="hint-row">帳號：wang_ming@email.com　密碼：123456</div>
        <div class="hint-row">帳號：admin@robostore.com　密碼：admin123</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-view {
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

.login-box {
  max-width: 420px;
  margin: 32px auto;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.login-header {
  background: linear-gradient(135deg, #6b7a3d, #8a9e50);
  color: #fff;
  text-align: center;
  padding: 24px 16px 16px;
}

.login-icon {
  font-size: 48px;
  margin-bottom: 8px;
}

.login-header h2 {
  font-size: 18px;
  font-weight: bold;
}

.login-form {
  padding: 24px;
  background: #fff;
}

.form-row {
  margin-bottom: 16px;
}

.form-label {
  display: block;
  font-size: 13px;
  color: #555;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-input {
  width: 100%;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  padding: 8px 12px;
  font-size: 13px;
  font-family: inherit;
  outline: none;
  transition: border-color 0.2s;
}

.form-input:focus {
  border-color: #c8a030;
  box-shadow: 0 0 0 2px rgba(200, 160, 48, 0.15);
}

.error-msg {
  background: #fde8e8;
  color: #c03020;
  border: 1px solid #f0b0a0;
  border-radius: 4px;
  padding: 8px 12px;
  font-size: 13px;
  margin-bottom: 14px;
}

.btn-login {
  width: 100%;
  background: var(--btn-primary);
  color: #fff;
  padding: 10px 0;
  border-radius: 4px;
  font-size: 15px;
  font-weight: bold;
  transition: background 0.2s;
}

.btn-login:hover:not(:disabled) {
  background: var(--btn-primary-hover);
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: default;
}

.login-hint {
  background: #f8f4e8;
  border-top: 1px solid #e8d890;
  padding: 12px 24px;
  font-size: 12px;
  color: #888;
}

.hint-title {
  font-weight: bold;
  color: #a08030;
  margin-bottom: 4px;
}

.hint-row {
  margin-bottom: 2px;
}
</style>
