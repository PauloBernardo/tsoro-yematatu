package com.example.tsoroyematatu;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Context {
    public static String serverIp;
    public static Integer serverPort;
    private static Context instance = null;
    private final Socket client;
    private final ArrayList<String> buffer;
    private final ArrayList<ContextListening> listeners;

    private Context() throws IOException {
        client = new Socket(serverIp, serverPort);
        buffer = new ArrayList<>();
        listeners = new ArrayList<>();

        Task<Integer> threadOne = new Task<>(){
            @Override
            protected Integer call() throws Exception {
                DataInputStream istream = new DataInputStream(client.getInputStream());
                while (true) {
                    String MRcv = istream.readUTF();
                    System.out.println("Remote message: "+ MRcv);
                    Platform.runLater(() ->  {
                        try {
                            buffer.add(MRcv);
                            for (ContextListening listener: listeners) {
                                listener.handleResponse(MRcv);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        };

        Thread th = new Thread(threadOne);
        th.setDaemon(true);
        th.start();
    }

    public static Context getInstance() throws IOException {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public Socket getClient() {
        return client;
    }

    public void addListening(ContextListening contextListening) {
        listeners.add(contextListening);
    }

    public void removeListening(ContextListening contextListening) {
        listeners.remove(contextListening);
    }
}
