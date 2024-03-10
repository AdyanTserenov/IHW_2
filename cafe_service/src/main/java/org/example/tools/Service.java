package org.example.tools;

import org.example.entities.Person;
import org.example.tools.interfaces.AdminInterface;
import org.example.tools.interfaces.ConsumerInterface;

public class Service {
    public static void run() {
        Authentication authentication = new Authentication();
        Person person = authentication.hello();

        if (person.getUserType().equals("admin")) {
            AdminInterface adminInterface = new AdminInterface();
            adminInterface.run();
        } else {
            ConsumerInterface consumerInterface = new ConsumerInterface(person);
            consumerInterface.run();
        }
    }
}
