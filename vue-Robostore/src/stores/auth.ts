import { ref, computed } from 'vue'
import { apiLogin, type UserInfo } from '../api'

export type { UserInfo as User }

export const currentUser = ref<UserInfo | null>(null)
export const isLoggedIn = computed(() => currentUser.value !== null)

export async function login(account: string, password: string): Promise<boolean> {
  try {
    const user = await apiLogin(account, password)
    currentUser.value = user
    return true
  } catch {
    return false
  }
}

export function logout() {
  currentUser.value = null
}
