package com.krzysztofwitczak.androwearapp.game_server_connection;

import com.krzysztofwitczak.androwearapp.emotions.Emotion;

import java.io.IOException;
import java.net.Socket;

public class GameConnectionHelper {

    private static Socket socket;
    public static boolean connectionSuccessful = false;
    private static final int GAME_PORT = 11000;

    public static void setupConnection(final String ip) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, GAME_PORT);
                    connectionSuccessful = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            connectionSuccessful = false;
        }
    }

    public static void transmitHeartData(Emotion emotion, int heartRate) {
        new Thread(new ServerThread(socket, emotion, heartRate)).start();
    }
}
