package org.example.tools;

import org.example.dao.PersonDao;
import org.example.entities.Person;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Authentication {
    private final Scanner input = new Scanner(System.in);
    private Person person = new Person();
    private final PersonDao personDao = new PersonDao(Person.class);

    private boolean toMainMenu() {
        System.out.println("Желаете вернуться в главное меню?\n");
        System.out.print("Введите 'yes' для выхода: ");
        String ans = input.nextLine();
        System.out.println();
        return ans.equals("yes");
    }

    public Person hello() {
        while (true) {
            System.out.println("==============================\n");
            System.out.println("[Страница авторизации]\n");
            System.out.println("Доступные действия:\n");
            System.out.println("1) Вход");
            System.out.println("2) Регистрация");
            System.out.println("3) Выход из системы\n");
            System.out.print("Пожалуйста, введите соответствующее число: ");
            String action = input.nextLine();
            System.out.println();

            boolean isItTimeToStop = false;

            switch (action) {
                case "1" -> {
                    if (login()) {
                        System.out.println("Вы успешно авторизовались!\n");
                        return person;
                    } else {
                        System.out.println("Возврат в главное меню...\n");
                    }
                }
                case "2" -> {
                    if (registration()) {
                        System.out.println("Вы успешно зарегистрироровались!\n");
                        return person;
                    } else {
                        System.out.println("Возврат в главное меню...\n");
                    }
                }
                case "3" -> {
                    System.out.println("Завершение работы...\n");
                    isItTimeToStop = true;
                }
                default -> System.out.println("Некорректный ввод, пожалуйста, повторите попытку...\n");
            }

            if (isItTimeToStop) {
                System.exit(0);
                break;
            }
        }
        return person;
    }

    public boolean login() {
        while (true) {
            System.out.println("==============================\n");
            System.out.print("Логин: ");
            String login = input.nextLine();
            System.out.println();
            if (login.isEmpty()) {
                System.out.println("Некорректный ввод: логин не может быть пустой строкой\n");
                continue;
            }

            System.out.print("Пароль: ");
            String password = input.nextLine();
            System.out.println();
            if (password.isEmpty()) {
                System.out.println("Некорректный ввод: пароль не может быть пустой строкой\n");
                continue;
            }

            try {
                this.person = personDao.findByLogin(login);
                if (this.person.getPassword().equals(PasswordCipher.getHexString(PasswordCipher.getCipher(password)))) {
                    return true;
                } else {
                    this.person = null;
                    System.out.println("Некорректный ввод: неправильный логин или пароль\n");
                    if (toMainMenu()) {
                        return false;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("Некорректный ввод: такого пользователя не существует\n");
                if (toMainMenu()) {
                    return false;
                }
            }
        }
    }

    public boolean registration() {
        while (true) {
            System.out.println("==============================\n");
            System.out.print("Введите имя пользователя: ");
            String name = input.nextLine();
            System.out.println();
            if (name.isEmpty()) {
                System.out.println("Некорректный ввод: логин не может быть пустой строкой\n");
                if (toMainMenu()) {
                    return false;
                }
                continue;
            }

            System.out.print("Придумайте логин: ");
            String login = input.nextLine();
            System.out.println();
            if (login.isEmpty()) {
                System.out.println("Некорректный ввод: логин не может быть пустой строкой\n");
                if (toMainMenu()) {
                    return false;
                }
                continue;
            }
            if (!personDao.loginIsFree(login)) {
                System.out.println("К сожалению, введенный логин уже существует, повторите попытку...\n");
                if (toMainMenu()) {
                    return false;
                }
                continue;
            }

            System.out.print("Придумайте пароль: ");
            String password = input.nextLine();
            System.out.println();
            if (password.isEmpty()) {
                System.out.println("Некорректный ввод: логин не может быть пустой строкой\n");
                if (toMainMenu()) {
                    return false;
                }
                continue;
            }
            String passwordCiphered = PasswordCipher.getHexString(PasswordCipher.getCipher(password));

            System.out.print("Secret code: ");
            String secret_ans = input.nextLine();
            System.out.println();
            String userType;

            if (secret_ans.equals("You underestimate my power!")) {
                userType = "admin";
            } else {
                userType = "consumer";
            }
            person = new Person(name, login, passwordCiphered, userType);
            personDao.save(person);
            System.out.println("Регистрация прошла успешно!\n");
            return true;
        }
    }
}
