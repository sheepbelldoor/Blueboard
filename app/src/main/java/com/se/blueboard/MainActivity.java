package com.se.blueboard;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Message;
import utils.MyCallback;
import utils.Utils;
import model.User;
import utils.FirebaseController;
import utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    public static User loginUser;
    // 앱 실행시 권한 허가 필요
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.POST_NOTIFICATIONS
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // main screen (9 buttons)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        List<String> courses = new ArrayList<>();
        courses.add("t1");
        courses.add("t2");
        List<String> sent = new ArrayList<>();
        sent.add("2");
        List<String> received = new ArrayList<>();
        received.add("1");
        loginUser = User.makeUser("abc2", "test22222", "짱구", "test", "test", "test", "test", courses, sent, received, null, 1, 1, 1);

        // Firestore test
        Button firestoreButton = findViewById(R.id.firestore);
        firestoreButton.setOnClickListener(v -> {
            // FirebaseController test
            List<String> listA = new ArrayList<String>();
            listA.add("t1");
            listA.add("t2");

            User user = new User("abc5", "test", "test", "test", "test", "test", "test", listA, null, null, null, 1, 1, 1);
            // User user = new User("abc1", "test", "test", "test", "test", "test", "test", listA, null, null, null, 1, 1, 1);
            FirebaseController controller = new FirebaseController();
            Message m1 = Message.makeMessage("1", "abc2", "abc5", "과제 안내", "과제가 일주일 연장 되었습니다.", new Date(), null, null, true);
            controller.sendMessageData(m1);

            Message m2 = Message.makeMessage("2", "abc5", "abc2", "과제 제출", "과제 제출합니다.", new Date(), null, null, false);
            controller.sendMessageData(m2);
            // delete test
            // controller.deleteData("users", "abc1");
            // send test
            //controller.sendUserData(user);
            // get test
//            controller.getUserData("abc1", new MyCallback() {
//                @Override
//                public void onSuccess(Object object) {
//                    User user = (User) object;
//                    Log.d("onSuccess", user.toString());
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Log.d("GetUserData", e.getMessage());
//                }
//            });
            // update test
//            controller.updateData(user);
            // upload test

//            File dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            File file = dir.listFiles()[0];

//            File file = new File("/storage/emulated/0/Pictures/IMG_20231123_182503.jpg");
//            controller.uploadFile(file);
//            String name = "1-CourseIntro.pdf";
//            controller.downloadFile(name);
//            Log.d("TestTest", Environment.DIRECTORY_DOWNLOADS);
        });

        // Login button
        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), LoginPage.class, null);
        });

        // Announcement button
        Button announcementButton = findViewById(R.id.announcement);
        announcementButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), AnnouncementPage.class, null);

        });

        // Attendance button
        Button attendanceButton = findViewById(R.id.attendance);
        attendanceButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), AttendancePage.class, null);
        });

        // Contents button
        Button contentsButton = findViewById(R.id.contents);
        contentsButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), ContentsPage.class, null);
        });

        // Groups button
        Button groupsButton = findViewById(R.id.groups);
        groupsButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), GroupsPage.class, null);
        });

        // Home button
        Button homeButton = findViewById(R.id.home);
        homeButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), HomePage.class, null);
        });

        // Lecture button
        Button lectureButton = findViewById(R.id.lecture);
        lectureButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), LecturePage.class, null);
        });

        // Message button
        Button messageButton = findViewById(R.id.message);
        messageButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), MessageBoxPage.class, null);
        });

        // Profile button
        Button profileButton = findViewById(R.id.profile);
        profileButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), ProfilePage.class, null);
        });

        Button pushButton = findViewById(R.id.push);
        pushButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            Utils.makeUtils().pushAlarm(getApplicationContext(), "test_channel", 1, "title", "text", pendingIntent);
        });

    }

    public static Context getContext(){
        return MainActivity.context;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}