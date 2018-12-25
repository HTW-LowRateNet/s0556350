package de.htw.f4.techmobsys.multihop.Controller;


public class CoordinatorClock implements Runnable {

    public final int alivClockSpeed;
    private MainController mc;


    public CoordinatorClock(MainController mc) {
        this.mc = mc;
        alivClockSpeed = 10;
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
//        mc.getConsole().println(this.toString() + " started!");

        while (true) {
            if (MainController.coordinator) {
                mc.sendALIV();
                try {
                    Thread.sleep(alivClockSpeed * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
