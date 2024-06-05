package com.example.bottonnavapp.ui.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottonnavapp.Modelo.ChatGPTMensajes;
import com.example.bottonnavapp.R;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {

    private List<ChatGPTMensajes> chatMessages;

    public ChatMessageAdapter(List<ChatGPTMensajes> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    /*
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatGPTMensajes chatMessage = chatMessages.get(position);
        holder.messageTextView.setText(chatMessage.getMensaje());
    }
    */
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (chatMessages != null && position < chatMessages.size()) {
            ChatGPTMensajes chatMessage = chatMessages.get(position);
            if (chatMessage != null && chatMessage.getMensaje() != null) {
                holder.messageTextView.setText(chatMessage.getMensaje());
            } else {
                Log.w("ChatMessageAdapter", "Chat message or message text is null at position: " + position);
            }
        } else {
            Log.w("ChatMessageAdapter", "Chat messages list is null or position is out of bounds: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void updateMessages(List<ChatGPTMensajes> newMessages) {

        //chatMessages.clear();
        chatMessages.addAll(newMessages);
        Log.d("ChatMessageAdapter", "Messages updated, new size: " + chatMessages.size());
        notifyDataSetChanged();

    }
}
