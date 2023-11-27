package com.se.blueboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    }

    // Parameter로 lectureName을 받아 cover의 lecture name을 변경하는 메소드
    public void setLectureName(String lectureName) {
        TextView coverLectureName = findViewById(R.id.cover_lecture_name);
        coverLectureName.setText(lectureName);
    }
}
