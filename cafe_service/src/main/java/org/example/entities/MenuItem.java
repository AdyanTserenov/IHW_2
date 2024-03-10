package org.example.entities;

import jakarta.persistence.*;

@Entity(name = "menu_item")

public class MenuItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "count")
    private Integer count;

    @Column(name = "time")
    private Integer time;

    public MenuItem() {}

    public MenuItem(String itemName, Integer price, Integer count, Integer time) {
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.time = time;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() { return count; }

    public void setCount(Integer count) { this.count = count; }

    public Integer getTime() { return time; }

    public void setTime(Integer time) { this.time = time; }
}
