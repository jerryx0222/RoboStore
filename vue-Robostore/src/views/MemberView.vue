<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { currentUser } from '../stores/auth'
import {
  fetchManufacturers, createManufacturer, updateManufacturer, deleteManufacturer,
  fetchMembers, createMember, updateMember, deleteMember,
  type Manufacturer, type MemberInfo, type MemberForm,
} from '../api'

const member = computed(() => currentUser.value!)

const LEVEL_SYSTEM_ADMIN = 1
const canManageVendors = computed(() => member.value.level <= LEVEL_SYSTEM_ADMIN)

// 系統管理者(0,1)、廠商管理者(10,11)、個人管理者(20,21)
const ADMIN_LEVELS = new Set([0, 1, 10, 11, 20, 21])
const canManageMembers = computed(() => ADMIN_LEVELS.has(member.value.level))

// ─── 會員管理 ────────────────────────────────────────────────────────────────
const memberList = ref<MemberInfo[]>([])
// 只顯示 level 嚴格大於自己的成員（數字越大權限越低）
const visibleMembers = computed(() => memberList.value.filter((m) => m.level > member.value.level))

// ─── 會員 Modal ───────────────────────────────────────────────────────────────
const showMemberModal = ref(false)
const editingMemberId = ref<number | null>(null)
const memberFormError = ref('')
const confirmPassword = ref('')
const memberFormChecked = ref(false)
const ALL_LEVEL_OPTIONS = [
  { value: 0, label: '系統開發者' }, { value: 1, label: '系統管理者' }, { value: 2, label: '系統使用者' },
  { value: 10, label: '廠商開發者' }, { value: 11, label: '廠商管理者' }, { value: 12, label: '廠商使用者' },
  { value: 20, label: '個人開發者' }, { value: 21, label: '個人管理者' }, { value: 22, label: '個人使用者' },
]
// 只顯示 level 嚴格大於登入者的等級（數字越大權限越低），且不含訪客(99)
const LEVEL_OPTIONS = computed(() =>
  ALL_LEVEL_OPTIONS.filter((opt) => opt.value > member.value.level)
)
const memberForm = ref<MemberForm>({ name: '', email: '', password: '', phonenumber: '', level: 22, phoneVerified: false, emailVerified: false })

watch(memberForm, () => { memberFormChecked.value = false }, { deep: true })
watch(confirmPassword, () => { memberFormChecked.value = false })

function openAddMember() {
  editingMemberId.value = null
  memberFormError.value = ''
  confirmPassword.value = ''
  memberFormChecked.value = false
  const defaultLevel = LEVEL_OPTIONS.value[0]?.value ?? 22
  memberForm.value = { name: '', email: '', password: '', phonenumber: '', level: defaultLevel, phoneVerified: false, emailVerified: false }
  showMemberModal.value = true
}

function openEditMember(m: MemberInfo) {
  editingMemberId.value = m.id
  memberFormError.value = ''
  confirmPassword.value = ''
  memberFormChecked.value = false
  memberForm.value = { name: m.name, email: m.email, password: '', phonenumber: m.phonenumber ?? '', level: m.level, phoneVerified: m.phoneVerified, emailVerified: m.emailVerified }
  showMemberModal.value = true
}

function checkMemberForm() {
  memberFormError.value = ''
  if (!memberForm.value.name.trim()) { memberFormError.value = '姓名不得為空'; return }
  if (!memberForm.value.email.trim()) { memberFormError.value = 'Email 不得為空'; return }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(memberForm.value.email.trim())) {
    memberFormError.value = 'Email 格式錯誤'; return
  }
  if (editingMemberId.value === null && !memberForm.value.password.trim()) { memberFormError.value = '新增時密碼不得為空'; return }
  if (memberForm.value.password && memberForm.value.password !== confirmPassword.value) {
    memberFormError.value = '兩次輸入密碼不一致'; return
  }
  if (!memberForm.value.phonenumber.trim()) { memberFormError.value = '電話為必填'; return }
  if (!/^09\d{8}$/.test(memberForm.value.phonenumber.trim())) {
    memberFormError.value = '電話格式錯誤，須為09開頭共10碼'; return
  }
  memberFormChecked.value = true
}

async function submitMemberForm() {
  if (!memberFormChecked.value) return
  const action = editingMemberId.value === null ? '新增' : '儲存'
  const msg = editingMemberId.value === null
    ? `確定要新增會員「${memberForm.value.name}」嗎？`
    : `確定要儲存會員「${memberForm.value.name}」的資料嗎？`
  if (!confirm(msg)) return
  memberFormError.value = ''
  try {
    if (editingMemberId.value === null) {
      const created = await createMember(memberForm.value)
      memberList.value.push(created)
    } else {
      const updated = await updateMember(editingMemberId.value, memberForm.value)
      const idx = memberList.value.findIndex((m) => m.id === editingMemberId.value)
      if (idx !== -1) memberList.value[idx] = updated
    }
    showMemberModal.value = false
  } catch (e: any) {
    memberFormError.value = e.message
    memberFormChecked.value = false
  }
}

async function removeMember(id: number) {
  const target = memberList.value.find((m) => m.id === id)
  if (target?.email === member.value.account) { alert('無法刪除自己的帳號'); return }
  try {
    await deleteMember(id)
    memberList.value = memberList.value.filter((m) => m.id !== id)
  } catch (e: any) {
    alert(e.message)
  }
}

// ─── 廠商管理 ────────────────────────────────────────────────────────────────
const vendors = ref<Manufacturer[]>([])

onMounted(async () => {
  if (canManageVendors.value) {
    vendors.value = await fetchManufacturers()
  }
  if (canManageMembers.value) {
    memberList.value = await fetchMembers()
  }
})

// ─── Modal 狀態 ───────────────────────────────────────────────────────────────
const showModal = ref(false)
const editingId = ref<number | null>(null)
const formError = ref('')
const form = ref({ name: '', phone: '', address: '', branchName: '', groupName: '', status: '啟用', taxId: '' })

function openAdd() {
  editingId.value = null
  formError.value = ''
  form.value = { name: '', phone: '', address: '', branchName: '', groupName: '', status: '啟用', taxId: '' }
  showModal.value = true
}

function openEdit(v: Manufacturer) {
  editingId.value = v.id
  formError.value = ''
  form.value = { name: v.name, phone: v.phone, address: v.address, branchName: v.branchName, groupName: v.groupName, status: v.status, taxId: v.taxId }
  showModal.value = true
}

async function submitForm() {
  if (!form.value.name.trim()) { formError.value = '廠商名稱不得為空'; return }
  formError.value = ''
  try {
    if (editingId.value === null) {
      const created = await createManufacturer(form.value)
      vendors.value.push(created)
    } else {
      const updated = await updateManufacturer(editingId.value, form.value)
      const idx = vendors.value.findIndex((v) => v.id === editingId.value)
      if (idx !== -1) vendors.value[idx] = updated
    }
    showModal.value = false
  } catch (e: any) {
    formError.value = e.message
  }
}

async function removeVendor(id: number) {
  try {
    await deleteManufacturer(id)
    vendors.value = vendors.value.filter((v) => v.id !== id)
  } catch (e: any) {
    alert(e.message)
  }
}

const orders = ref([
  { id: '163001', date: '2023-03-22', amount: 1500, status: '已批准', statusClass: 'approved' },
  { id: '163770', date: '2023-03-28', amount: 1500, status: '配達中', statusClass: 'shipping' },
  { id: '163001', date: '2023-04-10', amount: 1100, status: '已批准', statusClass: 'approved' },
])

const LEVEL_LABELS: Record<number, string> = {
  0: '系統開發者', 1: '系統管理者', 2: '系統使用者',
  10: '廠商開發者', 11: '廠商管理者', 12: '廠商使用者',
  20: '個人開發者', 21: '個人管理者', 22: '個人使用者',
  99: '訪客',
}

function levelLabel(level: number): string {
  return LEVEL_LABELS[level] ?? `Level ${level}`
}
</script>

<template>
  <div class="member-view">
    <div class="page-title">
      <span class="level-badge">{{ levelLabel(member.level) }}</span>
      會員中心，您好！{{ member.name }}
    </div>

    <div class="member-content">
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
            <td>{{ levelLabel(member.level) }}</td>
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
          </tr>
        </thead>
        <tbody>
          <tr v-for="(order, idx) in orders" :key="idx">
            <td>{{ order.id }}</td>
            <td>{{ order.date }}</td>
            <td>NT$ {{ order.amount }}</td>
            <td>
              <span class="status-badge" :class="order.statusClass">{{ order.status }}</span>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 會員管理（各類管理者） -->
      <template v-if="canManageMembers">
        <div class="section-subtitle vendor-subtitle-row" style="margin-top: 20px;">
          <span>會員管理</span>
          <button class="btn-add" @click="openAddMember">＋ 新增會員</button>
        </div>
        <table class="orders-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>姓名</th>
              <th>Email</th>
              <th>電話</th>
              <th>會員等級</th>
              <th>驗證</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="m in visibleMembers" :key="m.id">
              <td>{{ m.id }}</td>
              <td>{{ m.name }}</td>
              <td>{{ m.email }}</td>
              <td>{{ m.phonenumber }}</td>
              <td>{{ levelLabel(m.level) }}</td>
              <td>
                <span class="status-badge" :class="m.emailVerified && m.phoneVerified ? 'approved' : 'disabled'">
                  {{ m.emailVerified && m.phoneVerified ? '已驗證' : '未完成' }}
                </span>
              </td>
              <td class="vendor-actions">
                <button class="btn-edit" @click="openEditMember(m)">編輯</button>
                <button class="btn-remove" @click="removeMember(m.id)">刪除</button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 新增 / 編輯 會員 Modal -->
        <div v-if="showMemberModal" class="modal-overlay" @click.self="showMemberModal = false">
          <div class="modal">
            <div class="modal-title">{{ editingMemberId === null ? '新增會員' : '編輯會員' }}</div>
            <div class="modal-body">
              <label>姓名 <span class="required">*</span></label>
              <input v-model="memberForm.name" class="modal-input" placeholder="姓名" />

              <label>Email <span class="required">*</span></label>
              <input v-model="memberForm.email" class="modal-input" placeholder="Email" />

              <label>
                密碼
                <span v-if="editingMemberId !== null" style="font-weight:normal;color:#888;">（留空則不修改）</span>
                <span v-else class="required">*</span>
              </label>
              <input v-model="memberForm.password" type="password" class="modal-input" placeholder="密碼" />

              <label>
                確認密碼
                <span v-if="editingMemberId !== null" style="font-weight:normal;color:#888;">（修改密碼時必填）</span>
                <span v-else class="required">*</span>
              </label>
              <input v-model="confirmPassword" type="password" class="modal-input" placeholder="再次輸入密碼" />

              <label>電話</label>
              <input v-model="memberForm.phonenumber" class="modal-input" placeholder="電話" />

              <label>會員等級</label>
              <select v-model="memberForm.level" class="modal-input">
                <option v-for="opt in LEVEL_OPTIONS" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
              </select>

              <label>身份驗證</label>
              <div class="verify-checks">
                <label class="check-label">
                  <input type="checkbox" v-model="memberForm.emailVerified" /> Email 已驗證
                </label>
                <label class="check-label">
                  <input type="checkbox" v-model="memberForm.phoneVerified" /> 手機已驗證
                </label>
              </div>

              <p v-if="memberFormError" class="form-error">{{ memberFormError }}</p>
            </div>
            <div class="modal-footer">
              <button class="btn-cancel" @click="showMemberModal = false">取消</button>
              <button class="btn-check" @click="checkMemberForm">檢查</button>
              <button v-if="memberFormChecked" class="btn-save" @click="submitMemberForm">儲存</button>
            </div>
          </div>
        </div>
      </template>

      <!-- 廠商管理（系統開發者 / 系統管理者） -->
      <template v-if="canManageVendors">
        <div class="section-subtitle vendor-subtitle-row" style="margin-top: 20px;">
          <span>廠商管理</span>
          <button class="btn-add" @click="openAdd">＋ 新增廠商</button>
        </div>
        <div class="vendor-panel">
          <table class="vendor-table">
            <thead>
              <tr>
                <th>廠商名稱</th>
                <th>聯絡電話</th>
                <th>地址</th>
                <th>分店名稱</th>
                <th>集團名稱</th>
                <th>狀態</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="vendor in vendors" :key="vendor.id">
                <td>{{ vendor.name }}</td>
                <td>{{ vendor.phone }}</td>
                <td>{{ vendor.address }}</td>
                <td>{{ vendor.branchName }}</td>
                <td>{{ vendor.groupName }}</td>
                <td>
                  <span class="status-badge" :class="vendor.status === '啟用' ? 'approved' : 'disabled'">
                    {{ vendor.status }}
                  </span>
                </td>
                <td class="vendor-actions">
                  <button class="btn-edit" @click="openEdit(vendor)">編輯</button>
                  <button class="btn-remove" @click="removeVendor(vendor.id)">刪除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 新增 / 編輯 Modal -->
        <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
          <div class="modal">
            <div class="modal-title">{{ editingId === null ? '新增廠商' : '編輯廠商' }}</div>
            <div class="modal-body">
              <label>廠商名稱 <span class="required">*</span></label>
              <input v-model="form.name" class="modal-input" placeholder="廠商名稱" />

              <label>聯絡電話</label>
              <input v-model="form.phone" class="modal-input" placeholder="聯絡電話" />

              <label>統一編號</label>
              <input v-model="form.taxId" class="modal-input" placeholder="統一編號（8碼）" maxlength="8" />

              <label>地址</label>
              <input v-model="form.address" class="modal-input" placeholder="地址" />

              <label>分店名稱</label>
              <input v-model="form.branchName" class="modal-input" placeholder="分店名稱" />

              <label>集團名稱</label>
              <input v-model="form.groupName" class="modal-input" placeholder="集團名稱" />

              <label>狀態</label>
              <select v-model="form.status" class="modal-input">
                <option value="啟用">啟用</option>
                <option value="停用">停用</option>
              </select>

              <p v-if="formError" class="form-error">{{ formError }}</p>
            </div>
            <div class="modal-footer">
              <button class="btn-cancel" @click="showModal = false">取消</button>
              <button class="btn-save" @click="submitForm">儲存</button>
            </div>
          </div>
        </div>
      </template>
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
  display: flex;
  align-items: center;
  gap: 10px;
}

.level-badge {
  background: #6b7a3d;
  color: #fff;
  padding: 2px 10px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
  white-space: nowrap;
}

.member-content {
  padding: 16px;
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

.detail-subtitle {
  font-weight: bold;
  font-size: 13px;
  color: #6b3a00;
  margin-bottom: 8px;
}

/* Vendor management panel */
.vendor-panel {
  border: 1px solid var(--border-color);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 16px;
}

.vendor-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

.vendor-table th,
.vendor-table td {
  padding: 5px 8px;
  border: 1px solid var(--border-color);
  text-align: left;
}

.vendor-table th {
  background: #f5e8c0;
  color: #6b3a00;
  font-weight: bold;
}

.vendor-subtitle-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.vendor-actions {
  display: flex;
  gap: 4px;
}

.status-badge.disabled {
  background: #f0d4d4;
  color: #8a1a1a;
}

.btn-edit {
  background: #5a7a9a;
  color: #fff;
  padding: 2px 8px;
  border-radius: 3px;
  font-size: 11px;
  cursor: pointer;
}

.btn-edit:hover { background: #3a5a7a; }

.btn-remove {
  background: #9a3a3a;
  color: #fff;
  padding: 2px 8px;
  border-radius: 3px;
  font-size: 11px;
  cursor: pointer;
}

.btn-remove:hover { background: #7a2a2a; }

.btn-add {
  background: #6b7a3d;
  color: #fff;
  padding: 4px 14px;
  border-radius: 3px;
  font-size: 12px;
  cursor: pointer;
  white-space: nowrap;
}

.btn-add:hover { background: #4f5c2e; }

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: #fff;
  border-radius: 6px;
  width: 380px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.modal-title {
  background: #6b7a3d;
  color: #fff;
  padding: 10px 16px;
  font-weight: bold;
  font-size: 14px;
}

.modal-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.modal-body label {
  font-size: 12px;
  color: #6b3a00;
  font-weight: bold;
  margin-top: 4px;
}

.required { color: #c00; }

.verify-checks {
  display: flex;
  gap: 16px;
}

.check-label {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
  font-weight: normal;
  color: #333;
  cursor: pointer;
}

.form-error {
  color: #c00;
  font-size: 12px;
  margin: 4px 0 0;
}

.modal-input {
  width: 100%;
  padding: 5px 8px;
  border: 1px solid var(--border-color);
  border-radius: 3px;
  font-size: 13px;
  box-sizing: border-box;
}

.modal-footer {
  padding: 10px 16px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  border-top: 1px solid var(--border-color);
}

.btn-cancel {
  background: #aaa;
  color: #fff;
  padding: 5px 16px;
  border-radius: 3px;
  font-size: 13px;
  cursor: pointer;
}

.btn-cancel:hover { background: #888; }

.btn-check {
  background: #8a7020;
  color: #fff;
  padding: 5px 16px;
  border-radius: 3px;
  font-size: 13px;
  cursor: pointer;
}

.btn-check:hover { background: #6a5010; }

.btn-save {
  background: #6b7a3d;
  color: #fff;
  padding: 5px 16px;
  border-radius: 3px;
  font-size: 13px;
  cursor: pointer;
}

.btn-save:hover { background: #4f5c2e; }
</style>
