package com.krzysztofwitczak.androwearapp.game_server_connection;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.Socket;

class ServerThread implements Runnable {
    private Socket socket;

    ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Log.i("SERVER PLX", "RUN CALLEEEED");
        try {
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF("Android hello <EOF>");
            dOut.flush();
        } catch (Exception e) {
            Log.i("server", "ERRRRRRORRR");
            e.printStackTrace();
        }
    }
}
