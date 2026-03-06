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
  level: string
  joinDate: string
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
