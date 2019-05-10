package com.example.ledfading;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;

public class LedController extends AsyncTask<String, Void, String> {

    private String serverIp = "192.168.0.115";
    private Socket ledControllerSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Context context;
    private String serverResponse;
    String request;

    public LedController(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            ledControllerSocket = new Socket("192.168.0.115", 5678);

            dataInputStream = new DataInputStream(ledControllerSocket.getInputStream());

            dataOutputStream = new DataOutputStream(ledControllerSocket.getOutputStream());

            serverResponse = dataInputStream.readUTF();


//            ((Activity)context).runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(context, serverResponse, Toast.LENGTH_SHORT).show();
//                }
//            });

            request = "publish led " + params[0];

            dataOutputStream.writeInt(request.length());

            dataOutputStream.write((request).getBytes());

            dataOutputStream.flush();

//            serverResponse = dataInputStream.readUTF();
//
//            ((Activity)context).runOnUiThread(new Runnable() {
//                public void run() {
//                 Toast.makeText(context, serverResponse, Toast.LENGTH_SHORT).show();
//                }
//            });


            request = "Exit";

            dataOutputStream.writeInt(request.length());

            dataOutputStream.write((request).getBytes());

            dataOutputStream.flush();

            dataOutputStream.close();

            ledControllerSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

