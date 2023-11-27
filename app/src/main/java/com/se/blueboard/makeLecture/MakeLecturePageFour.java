package com.se.blueboard.makeLecture;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.se.blueboard.HomePage;
import com.se.blueboard.R;

import java.util.ArrayList;

import model.Lecture;
import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class MakeLecturePageFour extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_lecture_page_four);

        // 뒤로 가기 버튼
        Button backButton = (Button) findViewById(R.id.makeLecture_gotoBackFour);
        backButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), MakeLecturePageThree.class, null);
        });

        // 현재 수강 인원 수 출력
        TextView learningStudents = (TextView) findViewById(R.id.makeLecture_learningStudents);
        learningStudents.setText(makingLecture.getStudents().size() + " / " + makingLecture.getMaxStudents());

        // 사용자 검색 기능
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> studentList = new ArrayList<>();

        db.collection("users")
                .whereEqualTo("isManager", 0)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                            if (document.exists()) {
                                studentList.add(document.toObject(User.class).getStudentId() + "\t" + document.toObject(User.class).getName() + "\t" + document.toObject(User.class).getId());
                                Log.d("successGetStudentList", document.toObject(User.class).toString());

                                // 관리자 ListView
                                ListView listView = findViewById(R.id.makeLecture_setStudents);
                                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, studentList);

                                // 관리자 검색 기능
                                SearchView searchView = (SearchView) findViewById(R.id.makeLecture_searchStudent);
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String s) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String s) {
                                        ArrayList<String> filteredStudentList = new ArrayList<>();
                                        for (int i = 0; i < studentList.size(); i++) {
                                            String manager = studentList.get(i);

                                            if (manager.toLowerCase().contains(s.toLowerCase()))
                                                filteredStudentList.add(manager);
                                        }

                                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, filteredStudentList);
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
                                        if (makingLecture.getStudents().contains(temp[1]))
                                            Utils.toastTest(getApplicationContext(), "이미 존재하는 사용자입니다.");
                                        else {
                                            // 수강 인원이 최대 인원보다 많은 경우
                                            if (makingLecture.getStudents().size() >= Integer.parseInt(makingLecture.getMaxStudents()))
                                                Utils.toastTest(getApplicationContext(), "수강인원은 " + makingLecture.getMaxStudents() +"를 넘을 수 없습니다.");
                                            else {
                                                makingLecture.addStudent(temp[2]);
                                                Utils.toastTest(getApplicationContext(), "사용자 " + temp[1] +"이(가)" + "추가되었습니다.");
                                                learningStudents.setText(makingLecture.getStudents().size() + " / " + makingLecture.getMaxStudents());
                                            }
                                        }
                                    }
                                });
                            }
                            else
                                Log.d("ManagerList", "No such Document");
                        }
                    }
                });

        // 생성 버튼
        Button makeButton = (Button) findViewById(R.id.makeLecture_makeLectureButton);
        makeButton.setOnClickListener(view -> {
            FirebaseController controller = new FirebaseController();
            controller.updateData(makingLecture);
            updateUserLecture(controller, makingLecture);
        });
    }

    // lecture의 user에 대해 강의 정보를 업데이트 한 뒤 main으로 이동
    private void updateUserLecture(FirebaseController controller, @NonNull Lecture lecture) {

        // Update Manager's Lecture Data
        for (String managerId: lecture.getManagers()) {
            controller.getUserData(managerId, new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    User user = (User) object;
                    if (user.getCourses().contains(lecture.getId()))
                        user.addCourses(lecture.getId());
                    controller.updateData(user);
                }
                @Override
                public void onFailure(Exception e) {
                    Log.d("failAddCourseData", "Fail to add course data.");
                }
            });
        }

        // Update Student's Lecture Data
        for (String studentId: lecture.getStudents()) {
            controller.getUserData(studentId, new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    User user = (User) object;
                    if (user.getCourses().contains(lecture.getId()))
                        user.addCourses(lecture.getId());
                    controller.updateData(user);
                }
                @Override
                public void onFailure(Exception e) {
                    Log.d("failAddCourseData", "Fail to add course data.");
                }
            });
        }
        // 강의 생성한 유저 강의 업데이트
        controller.getUserData(lecture.getManagerId(), new MyCallback() {
            @Override
            public void onSuccess(Object object) {
                User user = (User) object;
                if (user.getCourses().contains(lecture.getId()))
                    user.addCourses(lecture.getId());
                controller.updateData(user);

                Utils.gotoPage(getApplicationContext(), HomePage.class, null);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("failAddCourseData", "Fail to add course data.");
            }
        });
    }
}
