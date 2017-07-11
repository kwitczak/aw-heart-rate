package com.krzysztofwitczak.androwearapp;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.Socket;

class ServerThread implements Runnable {

    public void run() {
        Log.i("SERVER PLX", "RUN CALLEEEED");
        try {
            Log.i("server", "BEFORE");
            Socket s = new Socket("192.168.1.102", 11000);
//            Socket s = new Socket("www.google.com", 80);
            Log.i("server", "CONNNNNNNNNNNNNNECTED");

            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("Android hello <EOF>");
            dOut.flush();
        } catch (Exception e) {
            Log.i("server", "ERRRRRRORRR");
            e.printStackTrace();
        }
    }
}
