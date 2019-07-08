package com.bignerdranch.android.chat;

public class Message {
    private String mSenderUserName;
    private String mMessageContent;

    public Message(String senderUserName, String messageContent) {
        mSenderUserName = senderUserName;
        mMessageContent = messageContent;
    }

    public String getSenderUserName() {
        return mSenderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        mSenderUserName = senderUserName;
    }

    public String getMessageContent() {
        return mMessageContent;
    }

    public void setMessageContent(String messageContent) {
        mMessageContent = messageContent;
    }
}
