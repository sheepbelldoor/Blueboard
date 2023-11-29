package com.se.blueboard;

import static android.view.KeyEvent.KEYCODE_BACK;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapter.NotificationAdapter;
import model.Alarm;
import utils.FirebaseController;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import utils.Utils;

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


        // 하단바
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationBar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        // 홈 화면으로 이동
                        Utils.gotoPage(getApplicationContext(), HomePage.class, null);
                        return true;

                    case R.id.menu_Mail:
                        // 메시지 화면으로 이동
                        Utils.gotoPage(getApplicationContext(), MessageBoxPage.class, null);
                        return true;

                    case R.id.menu_Notification:
                        // 알림 화면으로 이동 (현재 화면이므로 아무 동작 안함)
                        return true;

                    case R.id.menu_profile:
                        // 프로필 화면으로 이동
                        Utils.gotoPage(getApplicationContext(), ProfilePage.class, null);
                        return true;
                }
                return false;
            }
        });
    }
}
