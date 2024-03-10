package org.example.dao;

import org.example.tools.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public abstract class Dao<T> {
    private final Class<T> typeParameterClass;

    protected Dao(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public void save(T obj) {
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(obj);
        tx.commit();
        session.close();
    }
    public void update(T obj) {
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(obj);
        tx.commit();
        session.close();
    }
    public void delete(T obj) {
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(obj);
        tx.commit();
        session.close();
    }
    public T findById(Integer id) {
        Session session = Hibernate.getSessionFactory().openSession();
        T result = session.get(typeParameterClass, id);
        session.close();
        return result;
    }
    public List<T> getAll() { return null; }
}
