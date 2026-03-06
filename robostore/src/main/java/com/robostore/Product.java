package com.robostore;

public class Product {
    private int id;
    private String name;
    private int price;
    private String color;
    private String emoji;

    public Product(int id, String name, int price, String color, String emoji) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.color = color;
        this.emoji = emoji;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getColor() { return color; }
    public String getEmoji() { return emoji; }
}
