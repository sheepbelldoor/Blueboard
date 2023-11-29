package com.se.blueboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapter.NotificationAdapter;
import model.Alarm;
import model.Announcement;
import utils.FirebaseController;

import static com.se.blueboard.HomePage.currentUser;

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
        db.collection("alarms")
                .whereIn("id", currentUser.getCourses())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                            if(document.exists()){
                                alarms.add(document.toObject(Alarm.class));
                                Log.d("successGetAlarmList", document.toObject(Alarm.class).toString());

                                listView.setAdapter(adapter);
                            }
                        }
                    }
                });
        listView.setAdapter(adapter);

    }

}
