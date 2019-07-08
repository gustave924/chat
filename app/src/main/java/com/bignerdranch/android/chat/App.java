package com.bignerdranch.android.chat;

import android.app.Application;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class App extends Application {

    private static App app;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Log.d("garbadge", "onCreate: ");
    }

    public static App getApp(){
        return app;
    }

    public Socket getSocket() {
        return mSocket;
    }
}