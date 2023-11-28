package com.se.blueboard;

import static com.se.blueboard.LecturePage.currentLecture;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.util.ArrayList;

import model.User;
import utils.FirebaseController;
import utils.MyCallback;

public class GroupsPage extends AppCompatActivity {
    ArrayList<String> managerList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);

        // Set manager ListView
        ArrayAdapter adminListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, managerList);
        // Set student ListView
        ArrayAdapter userListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList);

        // 관리자 목록 추가
        FirebaseController controller = new FirebaseController();
        controller.getUserData(currentLecture.getManagerId(), new MyCallback() {
            @Override
            public void onSuccess(Object object) {
                for (String manager: currentLecture.getManagers()) {
                    String[] temp = manager.split("\t");
                    managerList.add("조교  " + temp[1]);
                }
                User prof = (User) object;
                managerList.add(0, "교수  " + prof.getName());


                ListView adminslistView = findViewById(R.id.userGroups_administratorsList);
                adminslistView.setAdapter(adminListAdapter);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("failGetUserData", "Fail to get User Data in GroupsPage.");
            }
        }); // End controller.getUserData

        // 사용자 목록 추가
        for (String student: currentLecture.getStudents()) {
            String[] temp = student.split("\t");
            studentList.add(temp[0] + "  " + temp[1]);
        }
        ListView userslistView = findViewById(R.id.userGroups_userList);
        userslistView.setAdapter(userListAdapter);
    }
}
