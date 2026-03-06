package com.robostore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    private static final Map<String, List<Product>> PRODUCTS = new HashMap<>();

    static {
        PRODUCTS.put("drinks", List.of(
            new Product(101, "招牌珍珠奶茶", 65, "#c8864a", "🧋"),
            new Product(102, "宇治抹茶拿鐵", 75, "#6a8a3a", "🍵"),
            new Product(103, "宇治抹茶星冰樂", 80, "#7a9a40", "🍵"),
            new Product(104, "草莓果昔", 70, "#d45870", "🍓"),
            new Product(105, "百香果綠茶", 60, "#c8a820", "🌿"),
            new Product(106, "芒果多多", 70, "#e8a820", "🥭"),
            new Product(107, "蜂蜜檸檬茶", 55, "#e8d040", "🍋"),
            new Product(108, "黑糖珍珠鮮奶", 75, "#6a4820", "🥛"),
            new Product(109, "玫瑰荔枝氣泡飲", 75, "#e8789a", "🌹")
        ));
        PRODUCTS.put("main-dishes", List.of(
            new Product(201, "蜂蜜烤雞腿便當", 120, "#8a6030", "🍱"),
            new Product(202, "蒜香排骨便當", 130, "#a05030", "🍱"),
            new Product(203, "紅燒牛腩便當", 150, "#8a3020", "🍱"),
            new Product(204, "薑燒豬肉便當", 110, "#c06030", "🍱"),
            new Product(205, "鮭魚定食", 140, "#e87040", "🐟"),
            new Product(206, "炸雞腿定食", 125, "#c87020", "🍗")
        ));
        PRODUCTS.put("popular", List.of(
            new Product(301, "韓式炸雞", 160, "#c04020", "🍗"),
            new Product(302, "日式拉麵", 180, "#a06020", "🍜"),
            new Product(303, "台式肉燥飯", 80, "#8a5030", "🍚"),
            new Product(304, "越式河粉", 130, "#d4a050", "🍜"),
            new Product(305, "泰式綠咖哩", 140, "#5a8a30", "🥘"),
            new Product(306, "牛肉漢堡", 150, "#c06020", "🍔")
        ));
        PRODUCTS.put("meals", List.of(
            new Product(401, "主廚今日推薦", 145, "#7a5020", "👨‍🍳"),
            new Product(402, "椒麻雞絲冷麵", 110, "#4a7a3a", "🍜"),
            new Product(403, "麻婆豆腐飯", 100, "#c04830", "🍚"),
            new Product(404, "三杯雞定食", 135, "#8a4020", "🍗")
        ));
        PRODUCTS.put("seasonal", List.of(
            new Product(501, "南瓜濃湯套餐", 120, "#d86020", "🎃"),
            new Product(502, "栗子蒙布朗", 90, "#8a6030", "🌰"),
            new Product(503, "秋柿牛奶凍", 65, "#e87040", "🍮"),
            new Product(504, "蘋果派", 75, "#c05030", "🥧")
        ));
    }

    @GetMapping("/products/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return PRODUCTS.getOrDefault(category, List.of());
    }

    @GetMapping("/home")
    public Map<String, List<Product>> getHomeProducts() {
        Map<String, List<Product>> result = new HashMap<>();
        result.put("newProducts", List.of(
            new Product(1, "招牌珍珠奶茶", 65, "#c8864a", "🧋"),
            new Product(2, "蜂蜜烤雞腿便當", 120, "#8a6030", "🍱"),
            new Product(3, "宇治抹茶拿鐵", 75, "#6a8a3a", "🍵"),
            new Product(4, "蒜香排骨便當", 130, "#a05030", "🍱"),
            new Product(5, "芒果西米露", 55, "#d4a020", "🥭"),
            new Product(6, "巧克力鬆餅", 85, "#6a4020", "🍫")
        ));
        result.put("chefRecommends", List.of(
            new Product(7, "紅燒牛腩飯", 150, "#8a3020", "🍱"),
            new Product(8, "韓式泡菜豬肉飯", 135, "#c05030", "🍚"),
            new Product(9, "日式照燒雞腿定食", 145, "#7a5020", "🍗"),
            new Product(10, "椒麻雞絲冷麵", 110, "#4a7a3a", "🍜")
        ));
        return result;
    }
}
