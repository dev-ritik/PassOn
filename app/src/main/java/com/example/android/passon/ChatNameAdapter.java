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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;

/**
 * Created by ritik on 25-01-2018.
 */

public class ChatNameAdapter extends RecyclerView.Adapter<ChatNameAdapter.ViewHolder> {
    private ArrayList<ChatHead[]> chats;
    private boolean tapCount = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView requesterName, requesterInitials;

        private ViewHolder(View view) {
            super(view);
//
//            bookName=(TextView)view.findViewById(R.id.bookName);
            requesterName = (TextView) view.findViewById(R.id.requestName);
            requesterInitials = (TextView) view.findViewById(R.id.initial);
        }
    }

    public ChatNameAdapter(ArrayList<ChatHead[]> chatHeads) {
        chats = chatHeads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_head, parent, false);

        return new ViewHolder(v);
    }

    // create a new view
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ChatHead[] chat = chats.get(position);
//        Log.i("point Po53",Integer.toString(Posts.size()));
        holder.requesterName.setText(chat[0]);
        String[] wordArray = chat.getUsername().split(" ");
        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<wordArray.length;i++) sb.append(wordArray[0].charAt(0));
        for(String s:wordArray){
            sb.append(s.charAt(0));
            Log.i(s,"point cna68");
        }

        holder.requesterInitials.setText(sb.toString());
        }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void changeData(String posteruid, final String requesterUid) {

        Log.i(posteruid, "point cna72");
        Log.i(requesterUid, "point cna73");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(posteruid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("connectionRequestUsers").push().setValue(requesterUid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}