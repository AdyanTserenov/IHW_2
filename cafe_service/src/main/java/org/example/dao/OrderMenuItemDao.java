package org.example.dao;

import org.example.entities.MenuItem;
import org.example.entities.OrderMenuItem;
import org.example.tools.Hibernate;
import org.hibernate.Session;

import java.util.List;

public class OrderMenuItemDao extends Dao<OrderMenuItem> {
    public OrderMenuItemDao(Class<OrderMenuItem> typeParameterClass) { super(typeParameterClass); }

    @Override
    public List<OrderMenuItem> getAll() {
        return (List<OrderMenuItem>)Hibernate.getSessionFactory().openSession().createQuery("From order_menu_item ").list();
    }

    public long CountPositions(Integer id) {
        Session session = Hibernate.getSessionFactory().openSession();
        long res = session.createQuery("select count(*) from order_menu_item where order.id=" + id + "and status like 'Приготовлено'", long.class).uniqueResult();
        session.close();
        return res;
    }

    public List<MenuItem> GetPositions(Integer id) {
        Session session = Hibernate.getSessionFactory().openSession();
        List<MenuItem> res = session.createQuery("select menuItem from order_menu_item where order.id=" + id + "group by menuItem", MenuItem.class).list();
        session.close();
        return res;
    }
}
