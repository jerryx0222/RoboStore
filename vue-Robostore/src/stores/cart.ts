import { ref, computed } from 'vue'

export interface CartItem {
  id: number
  name: string
  price: number
  qty: number
}

export const cartItems = ref<CartItem[]>([])

export function addToCart(product: { id: number; name: string; price: number }) {
  const existing = cartItems.value.find((i) => i.id === product.id)
  if (existing) {
    existing.qty++
  } else {
    cartItems.value.push({ ...product, qty: 1 })
  }
}

export function removeFromCart(id: number) {
  const idx = cartItems.value.findIndex((i) => i.id === id)
  if (idx !== -1) cartItems.value.splice(idx, 1)
}

export const cartTotal = computed(() =>
  cartItems.value.reduce((sum, i) => sum + i.price * i.qty, 0),
)
