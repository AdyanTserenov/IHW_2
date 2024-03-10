package org.example.dao;

import org.example.entities.FoodOrder;
import org.example.tools.Hibernate;
import org.hibernate.Session;

import java.util.List;

public class FoodOrderDao extends Dao<FoodOrder> {
    private boolean flag = false;
    public FoodOrderDao(Class<FoodOrder> typeParameterClass) { super(typeParameterClass); }

    @Override
    public List<FoodOrder> getAll() {
        return (List<FoodOrder>)Hibernate.getSessionFactory().openSession().createQuery("From food_order ").list();
    }

    public synchronized void setDoneStatus(Integer id) {
        if (!flag) {
            flag = true;
            System.out.println("\n\nВаш заказ № " + findById(id).getId() + " готов!\n");
        }
    }

    public List<FoodOrder> getOrders(Integer person_id) {
        Session session = Hibernate.getSessionFactory().openSession();
        List<FoodOrder> orders = (List<FoodOrder>) session.createQuery("from food_order where person.id=" + person_id).list();
        session.close();
        return orders;
    }
}
