package org.example.tools.interfaces;

import org.example.dao.MenuItemDao;
import org.example.entities.MenuItem;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class AdminInterface implements Runnable {
    private final Scanner input = new Scanner(System.in);
    private final MenuItemDao menuItemDao = new MenuItemDao(MenuItem.class);

    public void run() {
        while (true) {
            System.out.println("==============================\n");
            System.out.println("[Страница администратора]\n");
            System.out.println("Доступные действия:\n ");
            System.out.println("1) Добавить блюдо");
            System.out.println("2) Удалить блюдо");
            System.out.println("3) Изменение информации о блюде");
            System.out.println("4) Завершить сеанс\n");
            System.out.print("Пожалуйста, введите соответствующее число: ");
            String action = input.nextLine();
            System.out.println();

            boolean isItTimeToStop = false;

            switch (action) {
                case "1" -> {
                    addMenuItem();
                }
                case "2" -> {
                    deleteMenuItem();
                }
                case "3" -> {
                    changeMenuItem();
                }
                case "4" -> {
                    System.out.println("Завершение сеанса...");
                    isItTimeToStop = true;
                }
                default -> System.out.println("Некорректный ввод, пожалуйста, повторите попытку...\n");
            }

            if (isItTimeToStop) {
                break;
            }
        }
    }

    private void addMenuItem() {
        while (true) {
            System.out.println("==============================\n");
            System.out.print("Введите название блюда: ");
            String menuItemName = input.nextLine();
            System.out.println();
            if (menuItemName.isEmpty()) {
                System.out.println("Некорректный ввод: название не может быть пустой строкой\n");
                continue;
            }

            System.out.print("Введите цену: ");
            String data = input.nextLine();
            System.out.println();
            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: цена не может быть пустой строкой\n");
                continue;
            }
            Integer price = 0;
            try {
                price = Integer.parseInt(data);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод: цена должна быть числом");
            }

            System.out.print("Введите количество: ");
            data = input.nextLine();
            System.out.println();
            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: количество не может быть пустой строкой\n");
                continue;
            }
            Integer count = 0;
            try {
                count = Integer.parseInt(data);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод: количество должно быть числом");
                continue;
            }

            System.out.print("Введите время готовки: ");
            data = input.nextLine();
            System.out.println();
            if (data.isEmpty()) {
                System.out.println("Некорректный ввод: время не может быть пустой строкой\n");
                continue;
            }
            Integer time = 0;
            try {
                time = Integer.parseInt(data);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод: время должно быть числом");
                continue;
            }

            menuItemDao.save(new MenuItem(menuItemName, price, count, time));
            System.out.println("Блюдо успешно добавлено!\n");
            break;
        }
    }
    private void deleteMenuItem() {
        while (true) {
            System.out.println("==============================\n");
            System.out.print("Введите название блюда: ");
            String menuItemName = input.nextLine();
            System.out.println();
            if (menuItemName.isEmpty()) {
                System.out.println("Некорректный ввод: название не может быть пустой строкой\n");
                continue;
            }

            try {
                MenuItem menuItem = menuItemDao.findByName(menuItemName);
                menuItemDao.delete(menuItem);
                System.out.println("Блюдо успешно удалено!\n");
                break;
            } catch (NoSuchElementException e) {
                System.out.println("Некорректный ввод: блюдо с таким именем не существует\n");
            }
        }
    }
    private void changeMenuItem() {
        while (true) {
            System.out.println("==============================\n");
            System.out.print("Введите название блюда: ");
            String menuItemName = input.nextLine();
            System.out.println();
            if (menuItemName.isEmpty()) {
                System.out.println("Некорректный ввод: название не может быть пустой строкой\n");
                continue;
            }

            MenuItem menuItem = new MenuItem();
            try {
                menuItem = menuItemDao.findByName(menuItemName);

                System.out.println("Что Вы хотите изменить?\n ");
                System.out.println("1) Название блюда");
                System.out.println("2) Цена блюда");
                System.out.println("3) Количество блюда");
                System.out.println("4) Время готовки блюда\n");
                System.out.print("Пожалуйста, введите соответствующее число: ");
                String action = input.nextLine();
                System.out.println();

                switch (action) {
                    case "1" -> {
                        System.out.print("Введите новое название: ");
                        String new_name = input.nextLine();
                        System.out.println();
                        if (new_name.isEmpty()) {
                            System.out.println("Некорректный ввод: название не может быть пустой строкой\n");
                            continue;
                        }
                        menuItem.setItemName(new_name);
                    }
                    case "2" -> {
                        System.out.print("Введите новую цену: ");
                        String data = input.nextLine();
                        System.out.println();
                        if (data.isEmpty()) {
                            System.out.println("Некорректный ввод: цена не может быть пустой строкой\n");
                            continue;
                        }
                        Integer new_price = 0;
                        try {
                            new_price = Integer.parseInt(data);
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод: цена должна быть числом");
                            continue;
                        }
                        menuItem.setPrice(new_price);
                    }
                    case "3" -> {
                        System.out.print("Введите новое количество: ");
                        String data = input.nextLine();
                        System.out.println();
                        if (data.isEmpty()) {
                            System.out.println("Некорректный ввод: количество не может быть пустой строкой\n");
                            continue;
                        }
                        Integer new_count = 0;
                        try {
                            new_count = Integer.parseInt(data);
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод: количество должно быть числом");
                            continue;
                        }
                        menuItem.setCount(new_count);
                    }
                    case "4" -> {
                        System.out.print("Введите новое время: ");
                        String data = input.nextLine();
                        System.out.println();
                        if (data.isEmpty()) {
                            System.out.println("Некорректный ввод: время не может быть пустой строкой\n");
                            continue;
                        }
                        Integer new_time = 0;
                        try {
                            new_time = Integer.parseInt(data);
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод: время должно быть числом");
                            continue;
                        }
                        menuItem.setTime(new_time);
                    }
                    default -> System.out.println("Некорректный ввод, пожалуйста, повторите попытку...\n");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Некорректный ввод: блюдо с таким именем не существует\n");
            }
            menuItemDao.update(menuItem);
            System.out.println("Блюдо успешно изменено!\n");
            break;
        }
    }
}
