package com.se.blueboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.MessageListAdapter;
import model.Message;
import model.User;
import utils.FirebaseController;
import utils.Utils;

public class MessageBoxPage extends AppCompatActivity {
    private static Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CollectionReference collectionRef;
    private List<Message> messageList = new ArrayList<>();

    //    private User currentUser = new User("abc2", )
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_box);

        recyclerView = findViewById(R.id.MailList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionRef = db.collection("messages");
        FirebaseController controller = new FirebaseController();

        mAdapter = new MessageListAdapter(this, messageList);
        recyclerView.setAdapter(mAdapter);
//        controller.getMessageData();
//        String str = "2023-11-26 18:10:55";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = format.parse(str);
//
//            Message m1 = Message.makeMessage("1", "2", "3", "Subject_test", "test111aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", date, null, null, false);
//            Message[] myDataset = {m1};
//            mAdapter = new MessageListAdapter(myDataset);
//            recyclerView.setAdapter(mAdapter);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

        // 메시지 작성 버튼
        ImageButton write = findViewById(R.id.goto_write);
        write.setOnClickListener(view -> {
            Utils.gotoPage(this, MessageSendPage.class, null);
        });

        Log.d("LoginUser", MainActivity.loginUser.toString());
        Query query = collectionRef.where(Filter.or(
                Filter.equalTo("receiverId", MainActivity.loginUser.getId()),
                Filter.equalTo("senderId", MainActivity.loginUser.getId())
        )).orderBy("date");
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentList = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot document : documentList) {
                            messageList.add((Message) document.toObject(Message.class));
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Message load : ALL",e.getMessage());
                    }
                });

        // 전체 버튼
        Button all = findViewById(R.id.filter_all);
        all.setOnClickListener(view -> {
            collectionRef.where(Filter.or(
                            Filter.equalTo("receiverId", MainActivity.loginUser.getId()),
                            Filter.equalTo("senderId", MainActivity.loginUser.getId())
                    )).orderBy("date").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            messageList.clear();
                            List<DocumentSnapshot> documentList = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot document : documentList) {
                                messageList.add((Message) document.toObject(Message.class));
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Message Load : ALL", e.getMessage());
                        }
                    });
        });
        // 읽지 않음
        Button unread = findViewById(R.id.filter_unread);
        unread.setOnClickListener(view -> {
            collectionRef.where(Filter.and(
                            Filter.equalTo("receiverId", MainActivity.loginUser.getId()),
                            Filter.equalTo("isRead", false)
                    )).orderBy("date").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            messageList.clear();
                            List<DocumentSnapshot> documentList = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot document : documentList) {
                                messageList.add((Message) document.toObject(Message.class));
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Message Load : UnRead", e.getMessage());
                        }
                    });
        });
        // 받은 메시지
        Button receiveMsg = findViewById(R.id.filter_received);
        receiveMsg.setOnClickListener(view -> {
            collectionRef.whereEqualTo("receiverId", MainActivity.loginUser.getId()).orderBy("date").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            messageList.clear();
                            List<DocumentSnapshot> documentList = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot document : documentList) {
                                messageList.add((Message) document.toObject(Message.class));
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Message Load : Receive", e.getMessage());
                        }
                    });
        });
        // 보낸 메시지
        Button sendMsg = findViewById(R.id.filter_send);
        sendMsg.setOnClickListener(view -> {
            collectionRef.whereEqualTo("senderId", MainActivity.loginUser.getId()).orderBy("date").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            messageList.clear();
                            List<DocumentSnapshot> documentList = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot document : documentList) {
                                messageList.add((Message) document.toObject(Message.class));
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Message Load : Send", e.getMessage());
                        }
                    });
        });
        // navigation Bar
//        Button Home = findViewById(R.id.)
    }

    public static Context getContext(){
        return MessageBoxPage.context;
    }
}
