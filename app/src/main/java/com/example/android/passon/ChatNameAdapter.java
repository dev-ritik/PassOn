package com.example.android.passon;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

/*
adapter to select whom to chat
 */
public class ChatNameAdapter extends RecyclerView.Adapter<ChatNameAdapter.ViewHolder> {
    private ArrayList<ChatHead> chats;
    private ArrayList<String> times;
    private Context context;
    private LinearLayout dialogBox1;
    private TextView userName, userNameConnection;
    private ImageView cancelButton, acceptButton, shareDetails, deleteConnection, chatConnection, backgroundButton;
    private FrameLayout screen;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView requesterName, requesterInitials;

        private ViewHolder(View view) {
            super(view);
            requesterName = (TextView) view.findViewById(R.id.requestName);
            requesterInitials = (TextView) view.findViewById(R.id.initial);

        }
    }

    public ChatNameAdapter(ArrayList<ChatHead> chats, ArrayList<String> time, Context context, LinearLayout dialogBox, FrameLayout screen, ImageView backgroundButton) {
        this.chats = chats;
        this.times = time;
        this.context = context;
        this.dialogBox1 = dialogBox;
        this.screen = screen;
        this.backgroundButton = backgroundButton;
    }

//    public ChatNameAdapter(ArrayList<ChatHead> chatHeads, Context context) {
//        chats = chatHeads;
//        this.context = context;
//    }

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
        final String time = times.get(position);

        Log.i("point cna90", chat.getUserId());

        if (dialogBox1.equals(NotificationActivity.requestDialog))
            Log.i("point cna82", "equal");
        else Log.i("point cna83", "not equal");
        if (dialogBox1.equals(NotificationActivity.connectionDialog))
            Log.i("point cna85", "equal");
        else Log.i("point cna86", "not equal");

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
                screen.getForeground().setAlpha(180);
                backgroundButton.setVisibility(View.VISIBLE);
                if (dialogBox1.equals(NotificationActivity.requestDialog)) {
                    Log.i("point cna105", "requestDialog");
                    dialogBox1.setVisibility(View.VISIBLE);
                    userName = (TextView) dialogBox1.findViewById(R.id.userNameAccept);
                    cancelButton = (ImageView) dialogBox1.findViewById(R.id.cancelRequest);
                    acceptButton = (ImageView) dialogBox1.findViewById(R.id.acceptRequest);
                    shareDetails = (ImageView) dialogBox1.findViewById(R.id.giveHelp);
                    userName.setText(chat.getUsername() + " sent you a donation request");
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        post.getBookRequestUsers().remove(Main2Activity.mUserId);
                            changeData("connectionRequestUsers", Main2Activity.mUserId, chat.getUserId(), chat.getUsername(), time);
                            Toast.makeText(view.getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                            dialogBox1.setVisibility(View.INVISIBLE);
                            NotificationActivity.mAdapterRequest.notifyDataSetChanged();
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
                            setDataMe(Main2Activity.mUserId, chat.getUserId(), chat.getUsername(), time);
                            dialogBox1.setVisibility(View.INVISIBLE);
                            backgroundButton.setVisibility(View.INVISIBLE);
                            screen.getForeground().setAlpha(0);
//                            setDataRequester(Main2Activity.mUserId,chat.getUserId(),Main2Activity.mUser);
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("person1", chat.getUserId());
                            context.startActivity(intent);

                        }
                    });
                }

                if (dialogBox1.equals(NotificationActivity.connectionDialog)) {
                    Log.i("point cna143", "connectionDialog");

                    dialogBox1.setVisibility(View.VISIBLE);
                    userNameConnection = (TextView) dialogBox1.findViewById(R.id.userNameConnection);
                    deleteConnection = (ImageView) dialogBox1.findViewById(R.id.deleteConnection);
                    chatConnection = (ImageView) dialogBox1.findViewById(R.id.chatConnection);

                    userNameConnection.setText("chat with " + chat.getUsername());
                    deleteConnection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeData("connectedUsers", Main2Activity.mUserId, chat.getUserId(), chat.getUsername(), time);
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            dialogBox1.setVisibility(View.INVISIBLE);
                            NotificationActivity.mAdapterConnected.notifyDataSetChanged();
                            backgroundButton.setVisibility(View.INVISIBLE);

                        }
                    });
                    chatConnection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogBox1.setVisibility(View.INVISIBLE);
                            backgroundButton.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("person1", chat.getUserId());
                            context.startActivity(intent);
                        }
                    });
                }

            }
        });
        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point cna182", "bb clicked");
                screen.getForeground().setAlpha(0);
                backgroundButton.setVisibility(View.INVISIBLE);
                dialogBox1.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void setDataMe(String userId, final String acceptedId, final String acceptedName, final String time) {
//switches a person from request to connection
        Log.i(userId, "point cna114");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final HashMap<String, Object> userAdded = new HashMap<>();
                    userAdded.put(time, new ChatHead(acceptedId, acceptedName));
                    child.getRef().child("connectedUsers").updateChildren(userAdded).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("point cna208", "completed changing");
//                            NotificationActivity.connections.add(new ChatHead(acceptedId, acceptedName));
//                            NotificationActivity.mAdapterConnected.notifyDataSetChanged();
                            backgroundButton.setVisibility(View.INVISIBLE);
                            screen.getForeground().setAlpha(0);
                            if (Main2Activity.userInfo.getConnectedUsers() == null)
                                Main2Activity.userInfo.setConnectedUsers(userAdded);
                            else
                                Main2Activity.userInfo.getConnectionRequestUsers().put(acceptedId, acceptedName);
                        }

                    });
                    HashMap<String, Object> userDeleted = new HashMap<>();
                    userDeleted.put(time, null);
                    child.getRef().child("connectionRequestUsers").updateChildren(userDeleted).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("point cna221", "completed changing");
//                            NotificationActivity.requests.remove(new ChatHead(acceptedId, acceptedName));
//                            NotificationActivity.mAdapterRequest.notifyDataSetChanged();
                            Main2Activity.userInfo.getConnectionRequestUsers().put(acceptedId, null);
                            backgroundButton.setVisibility(View.INVISIBLE);
                            screen.getForeground().setAlpha(0);
                        }
                    });
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
//                    Log.i("point cna250","post changing");
//                    ArrayList<String> requesters=(ArrayList<String>)child.child("bookRequestUsers").getValue();
//                    requesters.remove()
//                    child.getRef().child("bookRequestUsers").setValue(requesters);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void setDataRequester(final String myId, final String requesterID, final String myName) {

        Log.i(requesterID, "point cna217");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(requesterID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, Object> userAdded = new HashMap<>();
                    userAdded.put(myId, myName);
                    child.getRef().child("connectedUsers").updateChildren(userAdded);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void changeData(final String node, String userId, final String peopleUid, final String peopleName, final String time) {
//deletes a request or connection
        Log.i(userId, "point cna110");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, Object> users = new HashMap<>();
                    users.put(time, null);
                    child.getRef().child(node).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("point cna286", "completed changing");
                            if (node.equals("connectedUsers")) {
//                                NotificationActivity.connections.remove(new ChatHead(peopleUid, peopleName));
//                                NotificationActivity.mAdapterConnected.notifyDataSetChanged();
                                Main2Activity.userInfo.getConnectedUsers().put(peopleName, null);
                                backgroundButton.setVisibility(View.INVISIBLE);
                                screen.getForeground().setAlpha(0);
                            } else {
//                                NotificationActivity.requests.remove(new ChatHead(peopleUid, peopleName));
//                                NotificationActivity.mAdapterRequest.notifyDataSetChanged();
                                Main2Activity.userInfo.getConnectionRequestUsers().put(peopleName, null);
                                backgroundButton.setVisibility(View.INVISIBLE);
                                screen.getForeground().setAlpha(0);
                            }
                            NotificationActivity.mAdapterRequest.notifyDataSetChanged();
                            NotificationActivity.mAdapterConnected.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//    }
//    private void deleteRequestPostData(String mUid){
        Query query1 = mPostDatabaseReference.orderByChild("time").equalTo(time);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArrayList<String> requesters = (ArrayList<String>) child.child("bookRequestUsers").getValue();
                    requesters.remove(peopleUid);
                    child.getRef().child("bookRequestUsers").setValue(requesters);
                    PostFragment.mAdapterPost.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}