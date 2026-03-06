<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { login } from '../stores/auth'

const router = useRouter()
const route = useRoute()

const account = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

// ─── Canvas 圖形驗證碼 ────────────────────────────────────────────────────────
// 排除易混淆字元 (0/O, 1/I/L)
const CHARS = 'ABCDEFGHJKMNPQRSTUVWXYZ23456789'

function genCode(len = 5): string {
  let s = ''
  for (let i = 0; i < len; i++) s += CHARS[Math.floor(Math.random() * CHARS.length)]
  return s
}

const captchaCode = ref(genCode())
const captchaCanvas = ref<HTMLCanvasElement | null>(null)
const captchaInput = ref('')
const captchaError = ref(false)

function drawCaptcha() {
  const canvas = captchaCanvas.value
  if (!canvas) return
  const ctx = canvas.getContext('2d')!
  const w = canvas.width, h = canvas.height

  // 背景
  ctx.fillStyle = '#f5ecd4'
  ctx.fillRect(0, 0, w, h)

  // 干擾曲線
  for (let i = 0; i < 5; i++) {
    ctx.strokeStyle = `hsl(${Math.random() * 360},45%,72%)`
    ctx.lineWidth = 1.5
    ctx.beginPath()
    ctx.moveTo(Math.random() * w, Math.random() * h)
    ctx.bezierCurveTo(
      Math.random() * w, Math.random() * h,
      Math.random() * w, Math.random() * h,
      Math.random() * w, Math.random() * h,
    )
    ctx.stroke()
  }

  // 雜點
  for (let i = 0; i < 50; i++) {
    ctx.fillStyle = `hsl(${Math.random() * 360},40%,65%)`
    ctx.beginPath()
    ctx.arc(Math.random() * w, Math.random() * h, Math.random() * 1.5 + 0.3, 0, Math.PI * 2)
    ctx.fill()
  }

  // 字元（旋轉 + 偏移 + 隨機色）
  const charW = w / captchaCode.value.length
  captchaCode.value.split('').forEach((ch, i) => {
    ctx.save()
    ctx.translate(charW * i + charW / 2, h / 2)
    ctx.rotate((Math.random() - 0.5) * 0.55)
    ctx.font = `bold ${23 + Math.random() * 7}px monospace`
    ctx.fillStyle = `hsl(${Math.random() * 360},65%,28%)`
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(ch, (Math.random() - 0.5) * 4, (Math.random() - 0.5) * 6)
    ctx.restore()
  })
}

onMounted(drawCaptcha)
watch(captchaCode, () => nextTick(drawCaptcha))

function refreshCaptcha() {
  captchaCode.value = genCode()
  captchaInput.value = ''
  captchaError.value = false
}
// ─────────────────────────────────────────────────────────────────────────────

async function handleLogin() {
  errorMsg.value = ''
  captchaError.value = false
  if (!account.value || !password.value) {
    errorMsg.value = '請填寫帳號與密碼'
    return
  }
  if (captchaInput.value.toUpperCase() !== captchaCode.value) {
    captchaError.value = true
    refreshCaptcha()
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
    refreshCaptcha()
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
            placeholder="請輸入電子信箱、帳號名稱或電話"
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

        <div class="form-row">
          <label class="form-label">驗證碼</label>
          <div class="captcha-wrap">
            <canvas ref="captchaCanvas" width="160" height="50" class="captcha-canvas"></canvas>
            <button type="button" class="btn-refresh" @click="refreshCaptcha" title="換一張">↺</button>
          </div>
          <input
            v-model="captchaInput"
            type="text"
            class="form-input"
            placeholder="請輸入圖中文字（不分大小寫）"
            autocomplete="off"
            maxlength="5"
            style="margin-top: 6px;"
          />
          <div v-if="captchaError" class="captcha-error">驗證碼錯誤，請重新輸入</div>
        </div>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? '登入中...' : '登入' }}
        </button>
      </form>

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

.captcha-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
}

.captcha-canvas {
  border: 1px solid var(--border-color);
  border-radius: 4px;
  display: block;
}

.btn-refresh {
  background: none;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  padding: 6px 10px;
  font-size: 18px;
  cursor: pointer;
  color: #888;
  line-height: 1;
}

.btn-refresh:hover {
  background: #f0f0f0;
  color: #444;
}

.captcha-error {
  margin-top: 5px;
  font-size: 12px;
  color: #c03020;
}
</style>
