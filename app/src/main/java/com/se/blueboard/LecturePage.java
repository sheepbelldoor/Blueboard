package com.se.blueboard;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import model.Lecture;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class LecturePage extends AppCompatActivity {
    FirebaseController controller = new FirebaseController();
    public static Lecture currentLecture = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecture);

        // 전달 받은 intent의 lectureName으로 변경
        setLectureName(getIntent().getExtras().getString("lectureName"));
        // currentLecture 설정
        controller.getLectureData(getIntent().getExtras().getString("lectureID"), new MyCallback() {
            @Override
            public void onSuccess(Object object) {
                currentLecture = (Lecture) object;
                Log.d("getCurrentLecture", "sibal;;");

                // Home button
                Button homeButton = findViewById(R.id.goto_main);
                homeButton.setOnClickListener(view -> {
                    Utils.gotoPage(getApplicationContext(), HomePage.class, null);
                });
                // Announcement button
                Button announcementButton = findViewById(R.id.goto_announcement);
                announcementButton.setOnClickListener(view -> {
                    Utils.gotoPage(getApplicationContext(), AnnouncementPage.class, null);
                });
                // User & Group button
                Button userGroupButton = findViewById(R.id.goto_userGroup);
                userGroupButton.setOnClickListener(view -> {
                    Utils.gotoPage(getApplicationContext(), GroupsPage.class, null);
                });
                // LectureContents button
                Button lectureContentsButton = findViewById(R.id.goto_lectureContents);
                lectureContentsButton.setOnClickListener(view -> {
                    Utils.gotoPage(getApplicationContext(), ContentsPage.class, null);
                });
                // Attendance button
                Button attendanceButton = findViewById(R.id.goto_attendance);
                attendanceButton.setOnClickListener(view -> {
                    Utils.gotoPage(getApplicationContext(), AttendancePage.class, null);
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("failGetCurrentLecture", "siballl;;");
            }
        });

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
                        // 알림 화면으로 이동
                        Utils.gotoPage(getApplicationContext(), NotificationPage.class, null);
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

    // Parameter로 lectureName을 받아 cover의 lecture name을 변경하는 메소드
    public void setLectureName(String lectureName) {
        TextView coverLectureName = findViewById(R.id.cover_lecture_name);
        coverLectureName.setText(lectureName);
    }
}
