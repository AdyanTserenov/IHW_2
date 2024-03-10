package org.example;

import org.example.dao.FoodOrderDao;
import org.example.entities.FoodOrder;
import org.example.tools.Service;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("\nДоброго времени суток, Вас приветствует система управления заказами!\n");

        Service.run();
    }
}
