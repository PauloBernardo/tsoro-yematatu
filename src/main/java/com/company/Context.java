package com.company;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Context extends UnicastRemoteObject implements TsoroYematatuClient {
    public static String serverIp;
    public static Integer serverPort;
    private static Context instance = null;
    private TsoroYematatuServerInterface server;
    private final ArrayList<String> buffer;
    private final ArrayList<ContextListening> listeners;

    private Context() throws RemoteException {
        super();
        buffer = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static Context getInstance() throws IOException {
        if (instance == null) {
            try {
                instance = new Context();
                Registry registry = LocateRegistry.getRegistry(0);
                registry.rebind("tsoro-yematatu-client", instance);
                instance.getServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public TsoroYematatuServerInterface getServer() {
        if (server == null) {
            try {
                server = (TsoroYematatuServerInterface) Naming.lookup("tsoro-yematatu-server");
                try {
                    server.registry("tsoro-yematatu-client");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return server;
    }

    public void addListening(ContextListening contextListening) {
        listeners.add(contextListening);
    }

    public void removeListening(ContextListening contextListening) {
        listeners.remove(contextListening);
    }

    @Override
    public void getName(String name) {

    }

    @Override
    public void startRandomMatch() {

    }

    @Override
    public void startChooseMatch() {

    }

    @Override
    public void cancelGame() {

    }

    @Override
    public void endGame(String status) {

    }

    @Override
    public void chooseColor(String color) throws Exception {

    }

    @Override
    public void move(int older, int newer) throws Exception {

    }

    @Override
    public void turn() {

    }

    @Override
    public void begin(String player) {

    }

    @Override
    public void choosePlayer(String color) throws Exception {

    }

    @Override
    public void drawGame(String response) {

    }

    @Override
    public void chatMessage(String name, String message) {

    }
}
