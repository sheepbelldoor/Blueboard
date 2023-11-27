package com.se.blueboard;

import static com.se.blueboard.LecturePage.currentLecture;
import static com.se.blueboard.makeLecture.MakeLecturePageOne.makingLecture;
import static utils.Utils.gotoPage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import adapter.AnnouncementAdapter;
import model.Announcement;
import model.User;
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("announcements")
                .whereEqualTo("lectureId", currentLecture.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                            if (document.exists()) {
                                announcements.add(document.toObject(Announcement.class));
                                Log.d("successGetAnnouncementList", document.toObject(Announcement.class).toString());

                                // 관리자 검색 기능
                                SearchView searchView = (SearchView) findViewById(R.id.announcement_search);
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String s) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String s) {
                                        ArrayList<Announcement> filteredAnnouncementList = new ArrayList<>();
                                        for (int i = 0; i < announcements.size(); i++) {
                                            String announcementTitle = announcements.get(i).getTitle();

                                            if (announcementTitle.toLowerCase().contains(s.toLowerCase()))
                                                filteredAnnouncementList.add(announcements.get(i));
                                        }

                                        AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(filteredAnnouncementList);
                                        listView.setAdapter(announcementAdapter);

                                        return false;
                                    }
                                });
                                // Set adapter
                                listView.setAdapter(adapter);
                            }
                            else
                                Log.d("ManagerList", "No such Document");
                        }
                    }
                });
        listView.setAdapter(adapter);

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
