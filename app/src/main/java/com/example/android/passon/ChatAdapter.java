package com.example.android.passon;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;

/**
 * Created by ritik on 26-01-2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<Chat> Chats;

    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView bookPic;
        TextView chat,time;
//        CheckBox favouritePost;
        Button request;
        LinearLayout messagelayout;

        private ViewHolder(View view) {
            super(view);
//            bookPic=(ImageView)view.findViewById(R.id.bookPic);
//            bookName=(TextView)view.findViewById(R.id.bookName);
//            filter1=(TextView)view.findViewById(R.id.filter1);
//            filter2=(TextView)view.findViewById(R.id.filter2);
//            favouritePost = (CheckBox)view.findViewById(R.id.favorite);
//            request=(Button)view.findViewById(R.id.request);
            messagelayout=(LinearLayout)view.findViewById(R.id.messageLayout);
            chat=(TextView)view.findViewById(R.id.chat);
            time=(TextView)view.findViewById(R.id.time);
        }
    }
    public ChatAdapter(ArrayList<Chat> chats) {
        Chats = chats;
    }
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message, parent, false);

        return new ChatAdapter.ViewHolder(v);
    }
    // create a new view
    @Override
    public void onBindViewHolder(final ChatAdapter.ViewHolder holder, int position) {
        final Chat chat = Chats.get(position);
//        Log.i("point Po53",Integer.toString(Posts.size()));
        if (chat.getSenderId().equals("Ritik")) {

            holder.messagelayout.setGravity(RIGHT);
        } else {
            holder.messagelayout.setGravity(LEFT);
        }
        holder.chat.setText(chat.getChatMessage());
        holder.time.setText(chat.getTime());

    }
    @Override
    public int getItemCount() {
        return Chats.size();
    }

}
