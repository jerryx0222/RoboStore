# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RoboStore (無人化餐飲) is an unmanned food & beverage store system with two sub-projects:

- `robostore/` — Spring Boot backend (Java 25, Spring Boot 4.0.3), runs on port 8080
- `vue-Robostore/` — Vue 3 + TypeScript frontend (Vite), runs on port 5173

## Commands

### Backend (`robostore/`)
```bash
cd robostore
./mvnw spring-boot:run      # Start dev server
./mvnw test                 # Run all tests
./mvnw test -Dtest=TestName # Run a single test class
./mvnw package              # Build JAR
```

### Frontend (`vue-Robostore/`)
```bash
cd vue-Robostore
npm run dev        # Start Vite dev server
npm run build      # Type-check + build for production
npm run type-check # TypeScript type check only
npm run preview    # Preview production build
```

## Architecture

### Backend
- Entry point: `RobostoreApplication.java` — also serves as the main `@RestController` with all route handlers currently consolidated in one class
- `MyController.java` — secondary controller (minimal, for testing)
- `CorsConfig.java` — allows all routes from `http://localhost:5173`
- Database: uses raw JDBC (not JPA repositories yet). `executePostgreSQL()` connects to `localhost:5432/JerryX`, schema `website`. `executeMySQL()` connects to `localhost/website`. The `/execute` endpoint currently toggles between them via a hardcoded boolean.
- `Point.java` — simple POJO serialized to JSON (demonstrates Jackson serialization via getters)

### Frontend
- State is managed with plain Vue `ref`/`computed` exported from module-level stores (no Pinia):
  - `src/stores/auth.ts` — `currentUser`, `isLoggedIn`, `login()`, `logout()` with hardcoded mock users
  - `src/stores/cart.ts` — `cartItems`, `cartTotal`, `addToCart()`, `removeFromCart()`
- Router (`src/router/index.ts`): `/member` requires auth; unauthenticated users are redirected to `/login`; already-logged-in users visiting `/login` are redirected to `/member`
- Product routes use a dynamic `:category` param (`/products/main-dishes`, `/products/popular`, etc.)
- Layout is defined in `App.vue`: fixed sidebar with product categories, top nav with auth bar
