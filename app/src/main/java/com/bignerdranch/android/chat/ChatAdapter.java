package com.bignerdranch.android.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int USER_MESSAGE = 0;
    private static final int SERVER_MESSAGE = 1;
    private Context mContext;
    private List<Message> mMessageList;

    public ChatAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessageList.get(position).getSenderUserName() == null){
            return SERVER_MESSAGE;
        }else{
            return USER_MESSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (i){
            case USER_MESSAGE:
                View v1 = inflater.inflate(R.layout.message_recycler_view_element,viewGroup, false);
                return new MessageViewHolder(v1);
            case SERVER_MESSAGE:
                View v2 = inflater.inflate(R.layout.server_message_recycler_view_element, viewGroup, false);
                return new ServerMessageViewHolder(v2);

                default:
                    return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if(viewHolder instanceof MessageViewHolder) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;
            messageViewHolder.bind(mMessageList.get(i));
        }
        else if(viewHolder instanceof ServerMessageViewHolder){
            ServerMessageViewHolder viewHolder1 = (ServerMessageViewHolder) viewHolder;
            viewHolder1.bind(mMessageList.get(i));
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView mSenderTextView;
        private TextView mMessageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            mSenderTextView = itemView.findViewById(R.id.message_sender_text_view);
            mMessageTextView = itemView.findViewById(R.id.message_content_text_view);
        }

        public void bind(Message message){
            mSenderTextView.setText(message.getSenderUserName());
            mMessageTextView.setText(message.getMessageContent());
        }
    }

    class ServerMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView mServerMessageTextView;

        public ServerMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mServerMessageTextView = itemView.findViewById(R.id.server_message_text_view);
        }

        public void bind(Message message){
            mServerMessageTextView.setText(message.getMessageContent());
        }
    }
}
