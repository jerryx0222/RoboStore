// 開發模式（Vite dev server）使用絕對路徑；production 打包後與後端同 server，用相對路徑
const BASE_URL = import.meta.env.PROD ? '' : 'http://localhost:8080'

export interface Product {
  id: number
  name: string
  price: number
  color: string
  emoji: string
}

export interface UserInfo {
  account: string
  name: string
  level: number
}

export async function fetchProducts(category: string): Promise<Product[]> {
  const res = await fetch(`${BASE_URL}/api/products/${category}`)
  if (!res.ok) throw new Error('無法取得商品資料')
  return res.json()
}

export async function fetchHomeProducts(): Promise<{ newProducts: Product[]; chefRecommends: Product[] }> {
  const res = await fetch(`${BASE_URL}/api/home`)
  if (!res.ok) throw new Error('無法取得首頁商品')
  return res.json()
}

export interface Manufacturer {
  id: number
  name: string
  phone: string
  status: string
  taxId: string
  address: string
  branchName: string
  groupName: string
}

export type ManufacturerForm = Omit<Manufacturer, 'id'>

export interface MemberInfo {
  id: number
  name: string
  email: string
  phonenumber: string
  level: number
  phoneVerified: boolean
  emailVerified: boolean
}

export interface MemberForm {
  name: string
  email: string
  password: string
  phonenumber: string
  level: number
  phoneVerified: boolean
  emailVerified: boolean
}

export async function fetchMembers(): Promise<MemberInfo[]> {
  const res = await fetch(`${BASE_URL}/api/members`)
  if (!res.ok) throw new Error('無法取得會員資料')
  return res.json()
}

export async function createMember(data: MemberForm): Promise<MemberInfo> {
  const res = await fetch(`${BASE_URL}/api/members`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) { const e = await res.json(); throw new Error(e.error ?? '新增會員失敗') }
  return res.json()
}

export async function updateMember(id: number, data: MemberForm): Promise<MemberInfo> {
  const res = await fetch(`${BASE_URL}/api/members/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) { const e = await res.json(); throw new Error(e.error ?? '編輯會員失敗') }
  return res.json()
}

export async function deleteMember(id: number): Promise<void> {
  const res = await fetch(`${BASE_URL}/api/members/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error('刪除會員失敗')
}

export async function fetchManufacturers(): Promise<Manufacturer[]> {
  const res = await fetch(`${BASE_URL}/api/manufacturers`)
  if (!res.ok) throw new Error('無法取得廠商資料')
  return res.json()
}

export async function createManufacturer(data: ManufacturerForm): Promise<Manufacturer> {
  const res = await fetch(`${BASE_URL}/api/manufacturers`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) throw new Error('新增廠商失敗')
  return res.json()
}

export async function updateManufacturer(id: number, data: ManufacturerForm): Promise<Manufacturer> {
  const res = await fetch(`${BASE_URL}/api/manufacturers/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) throw new Error('編輯廠商失敗')
  return res.json()
}

export async function deleteManufacturer(id: number): Promise<void> {
  const res = await fetch(`${BASE_URL}/api/manufacturers/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error('刪除廠商失敗')
}

export async function apiLogin(account: string, password: string): Promise<UserInfo> {
  const res = await fetch(`${BASE_URL}/api/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ account, password }),
  })
  if (!res.ok) throw new Error('帳號或密碼錯誤')
  return res.json()
}
