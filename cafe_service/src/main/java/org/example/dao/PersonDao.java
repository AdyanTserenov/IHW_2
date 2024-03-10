package org.example.dao;

import org.example.entities.Person;
import org.example.tools.Hibernate;
import org.hibernate.Session;

import java.util.List;
import java.util.NoSuchElementException;

public class PersonDao extends Dao<Person> {

    public PersonDao(Class<Person> typeParameterClass) { super(typeParameterClass); }

    @Override
    public List<Person> getAll() {
        return (List<Person>) Hibernate.getSessionFactory().openSession().createQuery("From person").list();
    }

    public Person findByLogin(String login) {
        Session session = Hibernate.getSessionFactory().openSession();
        Person result = session.createQuery("From person where login like '%s'".formatted(login), Person.class).list().getFirst();
        session.close();
        return result;
    }

    public boolean loginIsFree(String login) {
        try {
            Person person = findByLogin(login);
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}
