package org.example.entities;

import jakarta.persistence.*;

@Entity(name = "food_order")

public class FoodOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "ind_in_order")
    private Integer ind;

    @Column(name = "status")
    private String status;

    public FoodOrder() {}

    public FoodOrder(Person customer, Integer totalAmount, Integer ind, String status) {
        this.person = customer;
        this.totalAmount = totalAmount;
        this.ind = ind;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person customer) {
        this.person = customer;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInd() {
        return ind;
    }

    public void setInd(Integer ind) {
        this.ind = ind;
    }
}
