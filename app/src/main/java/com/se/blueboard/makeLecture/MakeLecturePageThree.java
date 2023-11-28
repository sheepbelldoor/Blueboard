package com.se.blueboard.makeLecture;

import static com.se.blueboard.HomePage.currentUser;
import static com.se.blueboard.makeLecture.MakeLecturePageOne.makingLecture;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.se.blueboard.HomePage;
import com.se.blueboard.R;

import java.util.ArrayList;

import model.User;
import utils.FirebaseController;
import utils.Utils;

public class MakeLecturePageThree extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_lecture_page_three);

        // 뒤로 가기 버튼
        Button backButton = (Button) findViewById(R.id.makeLecture_gotoBackThree);
        backButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), MakeLecturePageTwo.class, null);
        });

        // 현재 manager 수 출력
        TextView currentManagers = (TextView) findViewById(R.id.makeLecture_managers);
        // +1은 현재 유저
        currentManagers.setText(Integer.toString(makingLecture.getManagers().size() + 1));

        // 관리자 목록 가져오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> managerList = new ArrayList<>();

        db.collection("users")
                .whereEqualTo("isManager", 1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                            if (document.exists()) {
                                managerList.add(document.toObject(User.class).getStudentId() + "\t" + document.toObject(User.class).getName() + "\t" + document.toObject(User.class).getId());
                                Log.d("successGetManagerList", document.toObject(User.class).toString());

                                // 관리자 ListView
                                ListView listView = findViewById(R.id.makeLecture_setManagers);
                                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, managerList);

                                // 관리자 검색 기능
                                SearchView searchView = (SearchView) findViewById(R.id.makeLecture_searchManager);
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String s) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String s) {
                                        ArrayList<String> filteredManagerList = new ArrayList<>();
                                        for (int i = 0; i < managerList.size(); i++) {
                                            String manager = managerList.get(i);

                                            if (manager.toLowerCase().contains(s.toLowerCase()))
                                                filteredManagerList.add(manager);
                                        }

                                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, filteredManagerList);
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
                                        String selectedUser = (String) listView.getItemAtPosition(i);
                                        String[] temp = selectedUser.split("\t");
                                        if (makingLecture.getManagers().contains(temp[1]))
                                            Utils.toastTest(getApplicationContext(), "이미 존재하는 관리자입니다.");
                                        else {
                                            makingLecture.addManager(temp[2]);
                                            Utils.toastTest(getApplicationContext(), "관리자 " + temp[1] +"이(가)" + "추가되었습니다.");
                                            currentManagers.setText(Integer.toString(makingLecture.getManagers().size() + 1));
                                        }
                                    }
                                });
                            }
                            else
                                Log.d("ManagerList", "No such Document");
                        }
                    }
                });

        // 다음 버튼
        Button makeButton = (Button) findViewById(R.id.makeLecture_nextPageFour);
        makeButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), MakeLecturePageFour.class, null);
        });
    }
}
