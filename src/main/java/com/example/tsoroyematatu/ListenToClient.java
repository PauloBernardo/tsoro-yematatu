package com.example.tsoroyematatu;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ListenToClient implements Runnable {
    private Socket client;
    private ConnectServer root;

    public ListenToClient(Socket client, ConnectServer root) {
        this.client = client;
        this.root = root;
    }

    public void run() {
        try{
            DataInputStream istream = new DataInputStream(client.getInputStream());
            while (true) {
                String MRcv = istream.readUTF();
                System.out.println("Remoto: "+ MRcv);
            }

        } catch (Exception e){}
    }
}
