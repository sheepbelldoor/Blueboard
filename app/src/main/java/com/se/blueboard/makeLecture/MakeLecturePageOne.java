package com.se.blueboard.makeLecture;

import static com.se.blueboard.HomePage.currentUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.se.blueboard.R;

import java.util.ArrayList;

import model.Lecture;
import utils.Utils;

public class MakeLecturePageOne extends AppCompatActivity {
    public static Lecture makingLecture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_lecture_page_one);
        
        // 추가하는 강의 초기화
        initMakingLecture();
        
        ArrayList<String> lectureList = new ArrayList<>();
        ListView listView = findViewById(R.id.makeLecture_lectureList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lectureList);

        // Test List-------------------------------------
        lectureList.add("14235\tSoftware Engineering");
        lectureList.add("24682\tAlgorithm");
        lectureList.add("73152\tAI");
        lectureList.add("46452\tCompiler");
        lectureList.add("84231\tComputer Network");
        lectureList.add("76431\tOOP");
        //-----------------------------------------------

        // Set search view
        SearchView searchView = (SearchView) findViewById(R.id.makeLecture_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> filteredLecture = new ArrayList<>();
                for (int i = 0; i < lectureList.size(); i++) {
                    String lecture = lectureList.get(i);

                    if (lecture.toLowerCase().contains(s.toLowerCase()))
                        filteredLecture.add(lecture);
                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, filteredLecture);
                listView.setAdapter(adapter);

                return false;
            }
        });

        // Set adapter
        listView.setAdapter(adapter);
        // Click Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLecture = (String) listView.getItemAtPosition(i);
                String[] temp = selectedLecture.split("\t");
                makingLecture.setId(temp[0]);
                makingLecture.setName(temp[1]);
                Utils.gotoPage(getApplicationContext(), MakeLecturePageTwo.class, null);
            }
        });
    }

    // TODO: managerId 해당 유저 아이디로 설정해야 함.
    public void initMakingLecture() {
        makingLecture = Lecture.makeLecture("NONE", "NONE", "16", currentUser.getId(), "40", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

}
