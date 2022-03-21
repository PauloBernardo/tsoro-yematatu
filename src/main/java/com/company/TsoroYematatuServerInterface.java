package com.company;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;

public interface TsoroYematatuServerInterface extends Remote {

    public boolean registry(String id, String path) throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException;
    public boolean unregister(String id) throws Exception;
    public String setName(String id, String name) throws Exception;
    public String getName(String id) throws Exception;
    public boolean startNewMatch(String id) throws Exception;
    public String startRandomMatch(String id) throws Exception;
    public boolean startChooseMatch(String id, String chooseId) throws Exception;
    public ArrayList<GameDescription> getChooseMatch(String id) throws Exception;
    public ArrayList<GameDescription> getHistory(String id) throws Exception;
    public boolean cancelGame(String id) throws Exception;
    public boolean endGame(String id) throws Exception;
    public boolean chooseColor(String id, String color) throws Exception;
    public boolean move(String id, int older, int newer) throws Exception;
    public boolean choosePlayer(String id, String player) throws Exception;
    public boolean drawGame(String id, String response) throws Exception;
    public String chatMessage(String id, String message) throws Exception;

}
