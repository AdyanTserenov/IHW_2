package org.example.dao;

import org.example.entities.MenuItem;
import org.example.tools.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MenuItemDao extends Dao<MenuItem> {
    public MenuItemDao(Class<MenuItem> typeParameterClass) { super(typeParameterClass); }

    @Override
    public List<MenuItem> getAll() {
        return (List<MenuItem>)Hibernate.getSessionFactory().openSession().createQuery("From menu_item ").list();
    }

    public MenuItem findByName(String name) {
        Session session = Hibernate.getSessionFactory().openSession();
        MenuItem result = session.createQuery("From menu_item where itemName like '%s'".formatted(name), MenuItem.class).list().getFirst();
        session.close();
        return result;
    }

    public synchronized boolean reserveItem(Integer id) {
        Session session = Hibernate.getSessionFactory().openSession();
        Integer count = session.createQuery("select m.count from menu_item m where id=" + id, Integer.class).uniqueResult();
        if (count > 0) {
            Transaction tx = session.beginTransaction();
            session.createQuery("update menu_item set count=" + (count - 1) + " where id =" + id).executeUpdate();
            tx.commit();
            session.close();
            return true;
        } else {
            session.close();
            return false;
        }
    }
}
