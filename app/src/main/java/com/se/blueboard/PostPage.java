package com.se.blueboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import model.Announcement;
import model.LectureContent;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class PostPage extends AppCompatActivity {
    FirebaseController controller = new FirebaseController();
    LectureContent currentContent = LectureContent.makeLectureContent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        //뒤로 가기 버튼
        ImageButton backButton = findViewById(R.id.post_gotoBack);
        backButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), ContentsPage.class, null);
        });
    }
}
