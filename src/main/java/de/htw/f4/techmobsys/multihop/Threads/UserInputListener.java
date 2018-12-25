package de.htw.f4.techmobsys.multihop.Threads;

import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.Controller.MainController;

import java.util.Scanner;


public class UserInputListener implements Runnable {
    private Console console;
    private MainController mainController;

    public UserInputListener(Console console, MainController mainController) {
        this.console = console;
        this.mainController = mainController;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
//        console.println(this.toString() + " started!");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String temp = scanner.next();
                console.println("You typed: " + temp);
                console.emptyLine();
//                mainController.sendMessage(temp);
            }
        }
    }

    private void process(String userInput) {
        switch (userInput.toLowerCase().split(" ")[0]) {
            case "coordinator":
                console.println(userInput + ": " + MainController.coordinator);
                break;
            case "send":
                break;
        }
    }
}
