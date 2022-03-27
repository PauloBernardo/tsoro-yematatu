package com.company;

import javafx.application.Platform;

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
    private String path;
    private TsoroYematatuServerInterface server;
    private final ArrayList<TsoroYematatuClient> listeners;

    private Context() throws RemoteException {
        super();
        listeners = new ArrayList<>();
    }

    public static Context getInstance() throws IOException {
        if (instance == null) {
            try {
                instance = new Context();
                Registry registry = LocateRegistry.getRegistry(0);
                int clientNumber = 1;
                while(true) {
                    try {
                        registry.bind("tsoro-yematatu-client-" + clientNumber, instance);
                        instance.path = "tsoro-yematatu-client-" + clientNumber;
                        break;
                    } catch (Exception e) {
                        System.out.println("Probably the clientNumber " + clientNumber + " is on.");
                        clientNumber++;
                    }
                }
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
                    server.registry(this.path, this.path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return server;
    }

    public void addListening(TsoroYematatuClient contextListening) {
        listeners.add(contextListening);
    }

    public void removeListening(TsoroYematatuClient contextListening) {
        listeners.remove(contextListening);
    }

    @Override
    public void getName(String name) {

    }

    @Override
    public void startRandomMatch() throws RemoteException {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.startRandomMatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void startChooseMatch() throws RemoteException {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.startChooseMatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void cancelGame() throws RemoteException {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.cancelGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void endGame(String status) throws Exception {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.endGame(status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void chooseColor(String color) throws Exception {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.chooseColor(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void move(int older, int newer) throws Exception {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.move(older, newer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void turn() throws RemoteException {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.turn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void begin(String player) throws RemoteException {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.begin(player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void choosePlayer(String color) throws Exception {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.drawGame(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void drawGame(String response) throws Exception {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.drawGame(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void chatMessage(String name, String message) throws RemoteException {
        for(TsoroYematatuClient listener: listeners) {
            Platform.runLater(() -> {
                try {
                    listener.chatMessage(name, message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void unregisterClient() throws Exception {
        if (this.server != null) {
            this.server.unregister(this.path);
            Registry registry = LocateRegistry.getRegistry(0);
            registry.unbind(this.path);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
