package org.example.tools.interfaces;

import org.example.dao.FoodOrderDao;
import org.example.dao.MenuItemDao;
import org.example.dao.OrderMenuItemDao;
import org.example.entities.FoodOrder;
import org.example.entities.MenuItem;
import org.example.entities.OrderMenuItem;
import org.example.entities.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerInterface implements Runnable {
    private final Scanner input = new Scanner(System.in);
    private final MenuItemDao menuItemDao = new MenuItemDao(MenuItem.class);
    private final FoodOrderDao foodOrderDao = new FoodOrderDao(FoodOrder.class);
    private final OrderMenuItemDao orderMenuItemDao = new OrderMenuItemDao(OrderMenuItem.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(7);
    private final Person person;

    public ConsumerInterface(Person person) {
        this.person = person;
    }

    public void run() {
        while (true) {
            System.out.println("==============================\n");
            System.out.println("[Страница посетителя]\n");
            System.out.println("Доступные действия:\n");
            System.out.println("1) Составление заказа");
            System.out.println("2) Добавить блюда в существующий заказ");
            System.out.println("3) Отменить заказ");
            System.out.println("4) Оплата заказа");
            System.out.println("5) Завершить сеанс\n");
            System.out.print("Пожалуйста, введите соответствующее число: ");
            String action = input.nextLine();
            System.out.println();

            boolean isItTimeToStop = false;
            switch (action) {
                case "1" -> {
                    makeOrder();
                }
                case "2" -> {
                    changeOrder();
                }
                case "3" -> {
                    cancelOrder();
                }
                case "4" -> {
                    pay();
                }
                case "5" -> {
                    System.out.println("Завершение сеанса...");
                    executorService.shutdown();
                    isItTimeToStop = true;
                }
                default -> System.out.println("Некорректный ввод, пожалуйста, повторите попытку...\n");
            }

            if (isItTimeToStop) {
                break;
            }
        }
    }

    private Integer getInteger(String data, int size) {
        Integer choice;
        try {
            choice = Integer.parseInt(data);
            if (choice < 1 || choice > size) {
                System.out.println("Некорректный ввод: введенное число выходит за рамки соответствующего диапазона\n");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод: нужно ввести число\n");
            return null;
        }
        return choice;
    }

    private void printMenu(List<MenuItem> menu) {
        int ind = 1;
        for (MenuItem menuItem : menu) {
            System.out.println(ind++ + ") " + menuItem.getItemName() + ", " + menuItem.getPrice() + " руб, Время ожидания: " + menuItem.getTime() + " минут");
        }
        System.out.println();
    }

    private void makeOrder() {
        List<Integer> currOrder = makeChoice();
        String data;
        while (true) {
            if (currOrder.isEmpty()) {
                return;
            }
            System.out.println("==============================\n");
            System.out.println("Сформировать данный заказ?\n");
            System.out.print("Введите y/n: ");
            data = input.nextLine();
            System.out.println();

            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: ответ не может быть пустой строкой\n");
                continue;
            }
            break;
        }
        if (!data.equals("y")) {
            System.out.println("Отменяем заказ...");
            return;
        }
        Integer totalAmount = 0;
        for (Integer i : currOrder) {
            totalAmount += menuItemDao.findById(i).getPrice();
        }

        FoodOrder foodOrder = new FoodOrder(person, totalAmount, currOrder.size(), "Принят");
        foodOrderDao.save(foodOrder);
        Integer orderId = foodOrder.getId();

        for (Integer i : currOrder) {
            executorService.submit(new MenuItemCooking(i, orderId, menuItemDao, foodOrderDao));
        }
        System.out.println("Ваш заказ готовится\n");
    }

    private List<Integer> makeChoice() {
        List<MenuItem> menu = menuItemDao.getAll();
        List<Integer> currOrder = new ArrayList<>();

        while (true) {
            System.out.println("==============================\n");
            System.out.println("Для завершения выбора, введите слово 'stop'\n");
            if (menu.isEmpty()) {
                System.out.println("На данный момент в меню нет блюд, обратитесь к администратору\n");
                break;
            }

            printMenu(menu);

            System.out.print("Введите номер блюда, которое Вы хотите заказать: ");
            String data = input.nextLine();
            System.out.println();

            if (data.equals("stop")) {
                System.out.println("Возврат на предыдущую страницу...\n");
                break;
            }
            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: номер не может быть пустой строкой\n");
                continue;
            }

            Integer choice = getInteger(data, menu.size());
            if (choice == null) continue;

            System.out.print("Сколько этого блюда Вы хотите заказать? (осталось " + menu.get(choice - 1).getCount() + " шт): ");
            data = input.nextLine();
            System.out.println();
            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: количество не может быть пустой строкой\n");
                continue;
            }

            Integer count = getInteger(data, menu.get(choice - 1).getCount());
            if (count == null) continue;

            for (int i = 0; i < count; ++i) {
                currOrder.add(menu.get(choice - 1).getId());
            }
            menu.get(choice - 1).setCount(menu.get(choice - 1).getCount() - count);
            System.out.println("Добавляем блюдо в Ваш заказ...");
        }
        return currOrder;
    }

    private void printOrders(List<FoodOrder> orders) {
        for (int i = 1; i <= orders.size(); ++i) {
            System.out.println(i + ") Заказ №" + orders.get(i - 1).getId() + "\n");
            System.out.println("Блюда в заказе:");
            List<MenuItem> menuItems = orderMenuItemDao.GetPositions(orders.get(i - 1).getId());
            for (int j = 1; j <= menuItems.size(); ++j) {
                System.out.println("\t" + "-" + menuItems.get(j - 1).getItemName() + ". " + orders.get(i - 1).getInd() + " шт");
            }
            System.out.println("\n==========\n");
        }
    }

    private void changeOrder() {
        System.out.println("==============================\n");
        List<FoodOrder> orders = foodOrderDao.getOrders(person.getId());
        Integer num = getOrderChoice();
        String data;
        FoodOrder foodOrder = orders.get(num - 1);
        if (foodOrder.getStatus().equals("Принят") || foodOrder.getStatus().equals("Готовится")) {
            List<Integer> extraOrder = makeChoice();
            while (true) {
                if (extraOrder.isEmpty()) {
                    return;
                }
                System.out.println("==============================\n");
                System.out.println("Добавить эти блюда в Ваш заказ?\n");
                System.out.print("Введите y/n: ");
                data = input.nextLine();
                System.out.println();

                if (data.isEmpty()) {
                    System.out.println("Некорректный ввод: ответ не может быть пустой строкой\n");
                    continue;
                }
                break;
            }

            if (!data.equals("y")) {
                System.out.println("Отменяем заказ...");
                return;
            }

            Integer totalAmount = foodOrder.getTotalAmount();
            for (Integer i : extraOrder) {
                totalAmount += menuItemDao.findById(i).getPrice();
            }

            foodOrder.setTotalAmount(totalAmount);
            foodOrder.setInd(foodOrder.getInd() + extraOrder.size());
            foodOrderDao.update(foodOrder);
            Integer orderId = foodOrder.getId();

            for (Integer i : extraOrder) {
                executorService.submit(new MenuItemCooking(i, orderId, menuItemDao, foodOrderDao));
            }
            System.out.println("Ваш заказ готовится\n");
        } else {
            System.out.println("К сожалению, в данный заказ невозможно добавить блюда\n");
        }
    }

    private void cancelOrder() {
        System.out.println("==============================\n");
        List<FoodOrder> orders = foodOrderDao.getOrders(person.getId());
        String data;
        Integer num = getOrderChoice();
        while (true) {
            System.out.println("Вы действительно хотите отменить заказ?\n");
            System.out.print("Введите y/n: ");
            data = input.nextLine();
            System.out.println();

            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: ответ не может быть пустой строкой\n");
                continue;
            }
            break;
        }

        if (!data.equals("y")) {
            System.out.println("Возврат в главное меню...\n");
            return;
        }

        FoodOrder foodOrder = orders.get(num - 1);
        if (foodOrder.getStatus().equals("Принят") || foodOrder.getStatus().equals("Готовится")) {
            foodOrder.setStatus("Отменён");
            foodOrderDao.update(foodOrder);
            System.out.println("Ваш заказ успешно отменён!\n");
        } else if (foodOrder.getStatus().equals("Готов") || foodOrder.getStatus().equals("Оплачен")) {
            System.out.println("К сожалению, Ваш заказ отменить не получится\n");
        } else {
            System.out.println("Ваш заказ уже отменён\n");
        }
    }

    private void pay() {
        System.out.println("==============================\n");
        List<FoodOrder> orders = foodOrderDao.getOrders(person.getId());
        Integer num = getOrderChoice();

        FoodOrder foodOrder = orders.get(num - 1);

        String data;
        switch (foodOrder.getStatus()) {
            case "Принят", "Готовится" -> System.out.println("Ваш заказ обрабатывается, оплатите его после получения\n");
            case "Оплачен" -> System.out.println("Ваш заказ уже оплачен\n");
            case "Отменён" -> System.out.println("К сожалению, Ваш заказ отменён, оплата недоступна\n");
            case "Готов" -> {
                System.out.println("К оплате " + foodOrder.getTotalAmount() + " руб\n");
                while (true) {
                    System.out.println("Готовы оплатить заказ?\n");
                    System.out.print("Введите y/n: ");
                    data = input.nextLine();
                    System.out.println();

                    if (data.isEmpty()) {
                        System.out.println("Некорректный ввод: ответ не может быть пустой строкой\n");
                        continue;
                    }
                    break;
                }

                if (!data.equals("y")) {
                    System.out.println("Возврат в главное меню...\n");
                    return;
                }
                foodOrder.setStatus("Оплачен");
                foodOrderDao.update(foodOrder);
                System.out.println("Ваш заказ успешно оплачен!\n");
            }
        }
    }

    private Integer getOrderChoice() {
        List<FoodOrder> orders = foodOrderDao.getOrders(person.getId());
        printOrders(orders);
        String data;
        Integer num;
        while (true) {
            System.out.println("Какой заказ Вас интересует?\n");
            System.out.print("Введите номер заказа из списка: ");
            data = input.nextLine();
            System.out.println();
            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: количество не может быть пустой строкой\n");
                continue;
            }
            num = getInteger(data, orders.size());
            if (num == null) continue;
            break;
        }
        return num;
    }
}
