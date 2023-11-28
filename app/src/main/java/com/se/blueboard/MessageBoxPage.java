package com.se.blueboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.MessageListAdapter;
import io.grpc.okhttp.internal.Util;
import model.Message;
import model.User;
import utils.FirebaseController;
import utils.Utils;

public class MessageBoxPage extends AppCompatActivity {
    private static Context context;
    private MessageListAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_box);

        recyclerView = findViewById(R.id.MailList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Options
        // default
        Query query = db.collection("messages").where(Filter.or(
                Filter.equalTo("receiverId", MainActivity.loginUser.getId()),
                Filter.equalTo("senderId", MainActivity.loginUser.getId())
        )).orderBy("date");
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        // unread
        Query unreadQuery = db.collection("messages").where(Filter.and(
                Filter.equalTo("receiverId", MainActivity.loginUser.getId()),
                Filter.equalTo("isRead", false)
        )).orderBy("date");
        FirestoreRecyclerOptions<Message> unReadOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(unreadQuery, Message.class)
                .build();

        // receive
        Query receiveQuery = db.collection("messages")
                .whereEqualTo("receiverId", MainActivity.loginUser.getId()).orderBy("date");
        FirestoreRecyclerOptions<Message> receiveOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(receiveQuery, Message.class)
                .build();

        // send
        Query sendQuery = db.collection("messages")
                .whereEqualTo("senderId", MainActivity.loginUser.getId()).orderBy("date");
        FirestoreRecyclerOptions<Message> sendOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(sendQuery, Message.class)
                .build();

        mAdapter = new MessageListAdapter(options, getApplicationContext());
        recyclerView.setAdapter(mAdapter);

        // 전체
        Button all = findViewById(R.id.filter_all);
        all.setOnClickListener(view -> {
            mAdapter.updateOptions(options);
            recyclerView.setAdapter(mAdapter);
        });

        // 읽지 않음
        Button unread = findViewById(R.id.filter_unread);
        unread.setOnClickListener(view -> {
            mAdapter.updateOptions(unReadOptions);
            recyclerView.setAdapter(mAdapter);
        });

        // 받은 메시지
        Button received = findViewById(R.id.filter_received);
        received.setOnClickListener(view -> {
            mAdapter.updateOptions(receiveOptions);
            recyclerView.setAdapter(mAdapter);
        });

        // 보낸 메시지
        Button sent = findViewById(R.id.filter_send);
        sent.setOnClickListener(view -> {
            mAdapter.updateOptions(sendOptions);
            recyclerView.setAdapter(mAdapter);
        });

        // 작성 버튼
        ImageButton goToWrite = findViewById(R.id.goto_write);
        goToWrite.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), MessageSendPage.class, null);
        });

        // 검색창
        searchView = findViewById(R.id.message_searchMessage);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null) {
                    mAdapter.updateOptions(options);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    Query searchQuery = db.collection("messages").where(Filter.or(
                            Filter.equalTo("title", query),
                            Filter.equalTo("senderId", query),
                            Filter.equalTo("receiverId", query),
                            Filter.equalTo("content", query)
                    ));
                    FirestoreRecyclerOptions<Message> searchOptions = new FirestoreRecyclerOptions.Builder<Message>()
                            .setQuery(searchQuery, Message.class).build();
                    mAdapter.updateOptions(searchOptions);
                    recyclerView.setAdapter(mAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    public static Context getContext() {
        return MessageBoxPage.context;
    }
}
