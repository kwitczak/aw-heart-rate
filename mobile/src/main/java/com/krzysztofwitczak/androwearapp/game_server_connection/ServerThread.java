package com.krzysztofwitczak.androwearapp.game_server_connection;
import android.util.Log;

import com.krzysztofwitczak.androwearapp.emotions.Emotion;
import com.krzysztofwitczak.androwearapp.emotions.EmotionType;

import java.io.DataOutputStream;
import java.net.Socket;

class ServerThread implements Runnable {
    private Socket socket;
    private EmotionType emotionType;
    private float certainty;
    private int heartRate;

    ServerThread(Socket socket, Emotion emotion, int heartRate) {
        this.socket = socket;
        this.emotionType = emotion.getEmotionType();
        this.heartRate = heartRate;
        this.certainty = emotion.getCertainty();
    }

    public void run() {
        try {
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            String msg = String.format("{\"emotionType\":\"%s\",\"heartBeat\":%d,\"certainty\":%d}",
                         emotionType, heartRate, Math.round(certainty));
            Log.i("server", "SENDING: " + msg);
            dOut.writeUTF(msg);

            // TODO: Is this enough to make connection smooth?
            dOut.flush();
        } catch (Exception e) {
            Log.i("server", "Error occurred in data sync!");
            e.printStackTrace();
        }
    }
}
