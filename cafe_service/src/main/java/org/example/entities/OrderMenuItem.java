package org.example.entities;

import jakarta.persistence.*;

@Entity(name = "order_menu_item")

public class OrderMenuItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private FoodOrder order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @Column(name = "status")
    private String status;

    public OrderMenuItem() {}

    public OrderMenuItem(FoodOrder order, MenuItem menuItem, String status) {
        this.order = order;
        this.menuItem = menuItem;
        this.status = status;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FoodOrder getOrder() {
        return order;
    }

    public void setOrder(FoodOrder order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
