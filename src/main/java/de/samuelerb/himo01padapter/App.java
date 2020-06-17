package de.samuelerb.himo01padapter;

import com.pi4j.system.SystemInfo;
import de.samuelerb.himo01padapter.networkStack.NetworkInterface;
import de.samuelerb.himo01padapter.networkStack.NetworkPacket;
import de.samuelerb.himo01padapter.networkStack.config.Bandwidth;
import de.samuelerb.himo01padapter.networkStack.config.Config;
import de.samuelerb.himo01padapter.serialAdapter.SerialAdapter;
import de.samuelerb.himo01padapter.serialAdapter.SerialAdapterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        String welcome = "Welcome to the Multihop Network implemantation by Samuel Erb";
        System.out.println(welcome);

        SerialAdapter serialAdapter = null;

        while (serialAdapter == null) {
            serialAdapter = createSerialAdapter();
            if (serialAdapter == null) {
                System.out.println("Could not connect to any interface\nRetry in 5 seconds");
                for (int i = 0; i <= 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.print(".");

                }
                System.out.println();
            }
        }
        Config config = new Config();
        config.setBandwidth(Bandwidth._500KHz);
        config.setFrequency(430_000_000);

        NetworkInterface networkInterface = new NetworkInterface(config, serialAdapter);

        networkInterface.setOwnAddress("FFFF");

        networkInterface.sendNetworkPacket(new NetworkPacket(networkInterface.getOwnAddress(), "01fa", "Hello World"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String address = null;
            String message = null;
            System.out.print("Adresse: ");
            try {
                address = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.print("Eingabe: ");
            try {
                message = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            networkInterface.sendNetworkPacket(new NetworkPacket(networkInterface.getOwnAddress(), address, message));
        }
    }

    private static SerialAdapter createSerialAdapter() {
        SerialAdapter serialAdapter = null;
        try {
            serialAdapter = SerialAdapterFactory.createInstance(SerialAdapterFactory.Type.RASPBERRY, SystemInfo.BoardType.RaspberryPi_3B);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Could not connect to Serial Por of Raspberry Pi\nEmulating...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            try {
                serialAdapter = SerialAdapterFactory.createInstance(SerialAdapterFactory.Type.EMULATOR);
            } catch (Exception ee) {
            }
        } catch (Exception e) {
        }

        return serialAdapter;
    }
}
