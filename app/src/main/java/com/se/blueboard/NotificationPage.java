package com.se.blueboard;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import adapter.NotificationAdapter;
import model.Alarm;
import utils.FirebaseController;

public class NotificationPage extends AppCompatActivity {

    FirebaseController controller = new FirebaseController();

    public ArrayList<Alarm> alarms = new ArrayList<>();

    private ListView listView;
    private NotificationAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        adapter = new NotificationAdapter(alarms);
        listView = (ListView) findViewById(R.id.notificationList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();



    }

}
