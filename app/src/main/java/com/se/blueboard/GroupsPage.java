package com.se.blueboard;

import static com.se.blueboard.LecturePage.currentLecture;
import static com.se.blueboard.HomePage.currentUser;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Array;
import java.util.ArrayList;

import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class GroupsPage extends AppCompatActivity {
    ArrayList<String> managerList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.groups);

        // Set manager ListView
        ArrayAdapter adminListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, managerList);
        ListView adminslistView = findViewById(R.id.userGroups_administratorsList);
        // Set student ListView
        ArrayAdapter userListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList);
        ListView userslistView = findViewById(R.id.userGroups_userList);

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
            if (currentUser.getIsManager() != 1)
                studentList.add(Utils.masking(temp[0]) + "  " + temp[1]);
            else
                studentList.add(temp[0] + "  " + temp[1]);
        }
        userslistView.setAdapter(userListAdapter);

        // 유저 클릭 시 정보 출력
        adminslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.gotoPage(getApplicationContext(), GroupsPopUp.class, managerList.get(i));
            }
        });

        // 유저 클릭 시 정보 출력
        userslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.gotoPage(getApplicationContext(), GroupsPopUp.class, studentList.get(i));
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
}
