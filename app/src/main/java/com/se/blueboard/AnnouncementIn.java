package com.se.blueboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.se.blueboard.makeLecture.MakeLecturePageOne;

import model.Announcement;
import model.Lecture;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class AnnouncementIn extends AppCompatActivity {

    FirebaseController controller = new FirebaseController();
    public static Announcement currentAnnouncement = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_in);

        //뒤로 가기 버튼 구현
        ImageButton backButton = findViewById(R.id.announcement_gotoBack);
        backButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), Announcement.class, null);
        });

        controller.getAnnouncementData(getIntent().getExtras().getString("key"), new MyCallback() {


            @Override
            public void onSuccess(Object object) {
                currentAnnouncement = (Announcement) object;

                TextView titleText = findViewById(R.id.announcement_title_header);
                TextView detailText = findViewById(R.id.announcement_detail);

                titleText.setText(currentAnnouncement.getTitle());
                detailText.setText(currentAnnouncement.getDetail());
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("failGetCurrentAnnouncement", "siballl;;");
            }
        });


    }
}
