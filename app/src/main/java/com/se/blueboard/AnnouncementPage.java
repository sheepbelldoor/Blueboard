package com.se.blueboard;

import static utils.Utils.gotoPage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

import adapter.AnnouncementAdapter;
import model.Announcement;
import utils.FirebaseController;
import utils.Utils;

public class AnnouncementPage extends AppCompatActivity {

    FirebaseController controller = new FirebaseController();

    public ArrayList<Announcement> announcements = new ArrayList<>();

    private ListView listView;
    private AnnouncementAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement);

        adapter = new AnnouncementAdapter(announcements);
        listView = (ListView) findViewById(R.id.announcementList);

        //adapter안에 공지사항 더미데이터 넣기(나중에 파베에서 가져오는 걸로 바꿔)
        Date date = new Date(); // 일단 나중에 수정
        Announcement announcement = new Announcement(null, null, date, null, "공지사항1 test", null);
        Announcement announcement2 = new Announcement(null, null, date, null, "공지사항2 test", null);
        Announcement announcement3 = new Announcement(null, null, date, null, "공지사항3 test", null);

        announcements.add(announcement);
        announcements.add(announcement2);
        announcements.add(announcement3);

        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Announcement clickedAnnouncement = (Announcement) adapter.getItem(i);
                Utils.gotoPage(getApplicationContext(), AnnouncementIn.class, null);
            }
        });

        ImageButton postButton = findViewById(R.id.pen_button);
        postButton.setOnClickListener(view->{
            Utils.gotoPage(getApplicationContext(), UploadContentPage.class, null);
        });


    }

}
