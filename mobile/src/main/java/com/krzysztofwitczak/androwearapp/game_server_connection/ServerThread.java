package com.krzysztofwitczak.androwearapp.game_server_connection;
import android.util.Log;

import com.krzysztofwitczak.androwearapp.emotions.Emotion;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

class ServerThread implements Runnable {
    private Socket socket;

    ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            Emotion emotion = Emotion.randomEmotion();
            int heartRate = new Random().nextInt(50) + 40;
            dOut.writeUTF("{\"emotion\":\"" + emotion + "\",\"heartBeat\":" + heartRate + "}");
            dOut.flush();
        } catch (Exception e) {
            Log.i("server", "Error occurred in data sync!");
            e.printStackTrace();
        }
    }
}
