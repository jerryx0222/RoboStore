import { ref, computed } from 'vue'

export interface User {
  account: string
  name: string
  level: string
  joinDate: string
}

// Mock user database
const USERS: Record<string, { password: string; user: User }> = {
  'wang_ming@email.com': {
    password: '123456',
    user: {
      account: 'wang_ming@email.com',
      name: '王小明',
      level: '一般會員',
      joinDate: '2022-05-01',
    },
  },
  'admin@robostore.com': {
    password: 'admin123',
    user: {
      account: 'admin@robostore.com',
      name: '管理員',
      level: 'VIP 會員',
      joinDate: '2020-01-01',
    },
  },
}

export const currentUser = ref<User | null>(null)
export const isLoggedIn = computed(() => currentUser.value !== null)

export function login(account: string, password: string): boolean {
  const record = USERS[account]
  if (record && record.password === password) {
    currentUser.value = record.user
    return true
  }
  return false
}

export function logout() {
  currentUser.value = null
}
