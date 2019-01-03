package de.htw.f4.techmobsys.multihop;


import com.pi4j.io.serial.*;
import com.pi4j.system.SystemInfo;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.Controller.CoordinatorClock;
import de.htw.f4.techmobsys.multihop.Controller.MainController;
import de.htw.f4.techmobsys.multihop.Threads.SerialInputListener;
import de.htw.f4.techmobsys.multihop.Threads.UserInputListener;

import java.io.IOException;


public class App {
    public static void main(String[] args) {
        final Serial serial = SerialFactory.createInstance();

        Console console = new Console();
        SerialConfig config = new SerialConfig();
        config.device(SerialPort.getDefaultPort(SystemInfo.BoardType.RaspberryPi_Unknown))
                .baud(Baud._115200)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        try {
            serial.open(config);
        } catch (IOException e) {
            console.println(e.getMessage());
        }

        // parse optional command argument options to override the default serial settings.
        if (args.length > 0) {
            config = CommandArgumentParser.getSerialConfig(config, args);
        }

        Sender sender = new Sender(serial, console);
        MainController mainController = new MainController(console, serial, sender);




        SerialInputListener serialInputListener = new SerialInputListener(serial, mainController, sender);
        Thread serialInputThread = new Thread(serialInputListener);
        serialInputThread.run();

        UserInputListener userInputListener = new UserInputListener(console, mainController);
        Thread userInputListenerThread = new Thread(userInputListener);
        userInputListenerThread.start();

        CoordinatorClock coordinatorClock = new CoordinatorClock(mainController);
        Thread ccThread = new Thread(coordinatorClock);
        ccThread.start();


//        console.emptyLine();
//        String welcome = "Welcome to the Multihop Network implemantation by Samuel Erb";
//
//        for (int i = 0; i < welcome.length(); i++) {
//            console.print(welcome.charAt(i));
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        console.emptyLine();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        sender.waitForReset();
        mainController.init();
        mainController.run();
    }
}
