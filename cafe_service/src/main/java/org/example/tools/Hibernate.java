package org.example.tools;

import org.example.entities.FoodOrder;
import org.example.entities.MenuItem;
import org.example.entities.OrderMenuItem;
import org.example.entities.Person;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public class Hibernate {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, "jdbc:postgresql://0.0.0.0:5432/cafe_service");
                settings.put(Environment.USER, "adyan_tserenov");
                settings.put(Environment.PASS, "Kefteme228");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
                settings.put(Environment.C3P0_MIN_SIZE, 5);
                settings.put(Environment.C3P0_MAX_SIZE, 21);
                settings.put(Environment.C3P0_ACQUIRE_INCREMENT, 1);
                settings.put(Environment.C3P0_TIMEOUT, 1800);
                settings.put(Environment.C3P0_MAX_STATEMENTS, 150);

                settings.put(Environment.C3P0_CONFIG_PREFIX + ".initialPoolSize", 5);

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(FoodOrder.class);
                configuration.addAnnotatedClass(MenuItem.class);
                configuration.addAnnotatedClass(OrderMenuItem.class);
                configuration.addAnnotatedClass(Person.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
