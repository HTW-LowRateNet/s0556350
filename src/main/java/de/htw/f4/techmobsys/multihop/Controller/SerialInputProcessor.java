package de.htw.f4.techmobsys.multihop.Controller;

import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.beans.Message;


public class SerialInputProcessor implements Runnable {
    private MainController mainController;
    private Message message;
    private Console console;

    public SerialInputProcessor(MainController mainController, Message message) {
        this.mainController = mainController;
        this.message = message;
        this.console = mainController.getConsole();
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
        processIncomming(message);

    }

    public void processIncomming(Message msg) {
        console.box(3, "Processing", msg.toString());
        switch (msg.mc) {
            case KoordinatorDiscovery:
                if (MainController.coordinator) {
                    mainController.doCoordinatorStuff(msg);
                }
                break;
            case ALIV:
                mainController.processALIV(msg);

                break;
            case POLL:
                mainController.processPOLL(msg);
                mainController.doCoordinatorStuff(msg);
                break;

            case Message:
                mainController.processMSG(msg);
                break;

            case Address:

                mainController.doCoordinatorStuff(msg);
                break;

            case NeigbourDiscovery:
                break;

            case NRST:
                mainController.init();
                break;

            default:
                console.println("Unknown MessageCode");
                break;
        }
    }
}
