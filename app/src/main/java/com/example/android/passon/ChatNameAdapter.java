package com.example.android.passon;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.passon.Main2Activity.mPostDatabaseReference;
import static com.example.android.passon.Main2Activity.mUserDatabaseReference;

/**
 * Created by ritik on 25-01-2018.
 */

public class ChatNameAdapter extends RecyclerView.Adapter<ChatNameAdapter.ViewHolder> {
    private ArrayList<ChatHead> chats;
    private Context context;
    private View dialogBox;
    private TextView userName;
    private ImageView cancelButton,acceptButton,shareDetails;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView requesterName, requesterInitials;

        private ViewHolder(View view) {
            super(view);
            requesterName = (TextView) view.findViewById(R.id.requestName);
            requesterInitials = (TextView) view.findViewById(R.id.initial);

        }
    }

    public ChatNameAdapter(ArrayList<ChatHead> chatHeads, Context context,View dialogBox) {
        chats = chatHeads;
        this.context = context;
        this.dialogBox=dialogBox;
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
        final ChatHead chat = chats.get(position);

        Log.i("point Po53", chat.getUserId());
        holder.requesterName.setText(chat.getUsername());
        String[] wordArray = chat.getUsername().split(" ");
        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<wordArray.length;i++) sb.append(wordArray[0].charAt(0));
        for (String s : wordArray) {
            sb.append(s.charAt(0));
        }

        holder.requesterInitials.setText(sb.toString());
        holder.requesterInitials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "yupp", Toast.LENGTH_SHORT).show();
                LinearLayout dialogBox1=(LinearLayout)dialogBox;
                dialogBox1.setVisibility(View.VISIBLE);
                userName=(TextView) dialogBox1.findViewById(R.id.userNameAccept);
                cancelButton=(ImageView) dialogBox1.findViewById(R.id.cancelRequest);
                acceptButton=(ImageView) dialogBox1.findViewById(R.id.acceptRequest);
                shareDetails=(ImageView)dialogBox1.findViewById(R.id.giveHelp);
                userName.setText(chat.getUsername()+" sent you a donation request");
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        post.getBookRequestUsers().remove(Main2Activity.mUserId);
                        changeData(Main2Activity.mUserId, chat.getUserId());
                        Toast.makeText(view.getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                        dialogBox.setVisibility(View.INVISIBLE);
                        NotificationActivity.mAdapter.notifyDataSetChanged();
                    }
                });

                shareDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "shared", Toast.LENGTH_SHORT).show();
                    }
                });
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setData(Main2Activity.mUserId, chat.getUserId(),chat.getUsername());
                        Toast.makeText(view.getContext(), "Sending Request", Toast.LENGTH_SHORT).show();
                        dialogBox.setVisibility(View.INVISIBLE);
                        Intent intent=new Intent(context,ChatActivity.class);
                        intent.putExtra("person1",chat.getUsername());
                        context.startActivity(intent);

                    }
                });
            }
        });
    }

    public void setData(String userId, final String acceptedId, final String acceptedName) {

        Log.i(userId, "point cna114");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, Object> users = new HashMap<>();
                    users.put(acceptedId, acceptedName);
                    child.getRef().child("connectedUsers").updateChildren(users);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Query query1 = mPostDatabaseReference.orderByChild("time").equalTo(time);
//        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    child.getRef().child("bookRequestUsers").setValue(requestUsers);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void changeData(String userId, final String uid) {

        Log.i(userId, "point cna110");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, Object> users = new HashMap<>();
                    users.put(uid, null);
                    child.getRef().child("connectionRequestUsers").updateChildren(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Query query1 = mPostDatabaseReference.orderByChild("time").equalTo(time);
//        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    child.getRef().child("bookRequestUsers").setValue(requestUsers);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

}