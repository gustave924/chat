package com.bignerdranch.android.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String USER_NAME = "USER_NAME";
    private static final  String USER_COUNT = "USER_COUNT";


    private ImageButton mSendImageButton;
    private EditText mMessageEditText;
    private ChatAdapter mAdapter;
    private List<Message> mMessageList = new ArrayList<>();
    private String mUserName;
    private Socket mSocket;

    public static Intent newInstance(Context ctx, String userName, int usersCount){
        Intent i = new Intent(ctx, ChatActivity.class);
        i.putExtra(USER_NAME, userName);
        i.putExtra(USER_COUNT, usersCount);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView  chatRecyclerView = findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        mAdapter = new ChatAdapter(ChatActivity.this, mMessageList);
        chatRecyclerView.setAdapter(mAdapter);

        mSendImageButton = findViewById(R.id.send_image_button);
        mMessageEditText = findViewById(R.id.message_edit_text);
        Intent i = getIntent();
        mUserName = i.getStringExtra(USER_NAME);
        int numOfUsers = i.getIntExtra(USER_COUNT, 0);

        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);

        mSocket.on("typing", onTyping);
        mSocket.connect();

        mSendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageEditText.getText().toString();
                if (!message.equals("") && message != null) {
                    mMessageEditText.setText("");
                    Message message1 = new Message(mUserName, message);
                    mMessageList.add(message1);
                    mSocket.emit("new message", message1.getMessageContent());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSocket.emit("typing");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
    }

    Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                        Toast.makeText(ChatActivity.this, username+" is typing.", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {

                    }
                }
            });
        }
    };

    Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userName;
                    String message;
                    String numOfUsers;
                    try {
                        userName = "";
                        message = jsonObject.getString("username")+" left the chat";

                        Message messageObject = new Message(userName, message);
                        mMessageList.add(messageObject);
                        numOfUsers = "There is a "+jsonObject.getInt("numUsers")+" participants";
                        Message messageObject2 = new Message(userName, numOfUsers);
                        mMessageList.add(messageObject2);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userName;
                    String message;
                    String numOfUsers;
                    try {
                        userName = "";
                        message = jsonObject.getString("username")+" joined the chat";

                        Message messageObject = new Message(userName, message);
                        mMessageList.add(messageObject);
                        numOfUsers = "There is a "+jsonObject.getInt("numUsers")+" participants";
                        Message messageObject2 = new Message(userName, numOfUsers);
                        mMessageList.add(messageObject2);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userName;
                    String message;

                    try {
                        userName = jsonObject.getString("username");
                        message = jsonObject.getString("message");
                        Message messageObject = new Message(userName, message);
                        mMessageList.add(messageObject);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };
}
