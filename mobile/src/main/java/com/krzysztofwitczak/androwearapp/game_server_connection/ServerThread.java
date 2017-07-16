package com.krzysztofwitczak.androwearapp.game_server_connection;
import android.util.Log;

import com.krzysztofwitczak.androwearapp.emotions.Emotion;

import java.io.DataOutputStream;
import java.net.Socket;

class ServerThread implements Runnable {
    private Socket socket;
    private Emotion emotion;
    private int heartRate;

    ServerThread(Socket socket, Emotion emotion, int heartRate) {
        this.socket = socket;
        this.emotion = emotion;
        this.heartRate = heartRate;
    }

    public void run() {
        try {
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            String msg = "{\"emotion\":\"" + emotion + "\",\"heartBeat\":" + heartRate + "}";
            Log.i("server", "SENDING: " + msg);
            dOut.writeUTF(msg);
            dOut.flush();
        } catch (Exception e) {
            Log.i("server", "Error occurred in data sync!");
            e.printStackTrace();
        }
    }
}
