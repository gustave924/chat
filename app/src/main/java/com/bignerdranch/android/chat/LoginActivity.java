package com.bignerdranch.android.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity {

    private Socket mSocket;
    private AppCompatButton mJoinButton;
    private EditText mUserNameEditText;
    private String mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mJoinButton = findViewById(R.id.join_app_compat_button);
        mUserNameEditText = findViewById(R.id.user_name_edit_text);
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = mUserNameEditText.getText().toString();
                if(userName.equals("")){
                    mUserNameEditText.setError("You have to fill this");
                }else{
                    mUserName = userName;
                    mSocket.connect();
                    mSocket.emit("add user", mUserName);

                    Intent i = ChatActivity.newInstance(LoginActivity.this, mUserName, 2);
                    startActivity(i);
                }
            }
        });
        mSocket.on("login", onLogin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("login", onLogin);
    }



    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject)args[0];

            int numUsers;

            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
//            Intent i = ChatActivity.newInstance(LoginActivity.this, mUserName, numUsers);
//            startActivity(i);
        }
    };


}
