package adapter;

import static com.se.blueboard.HomePage.currentUser;
import static com.se.blueboard.LecturePage.currentLecture;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.se.blueboard.R;
import com.se.blueboard.UploadContentPage;

import java.util.ArrayList;
import java.util.List;

import model.Announcement;
import model.Assignment;
import model.Exam;
import model.LectureContent;
import model.Post;
import utils.Utils;

public class ContentsWeeksListAdapter extends BaseAdapter {
    ArrayList<Integer> lectureContentsWeekList = new ArrayList<>();
    ArrayList<LectureContent> lectureContentsList = new ArrayList<>();


    public ContentsWeeksListAdapter(ArrayList<Integer> lectureContentsWeekList) {
        this.lectureContentsWeekList = lectureContentsWeekList;
    }

    @Override
    public int getCount() {
        return lectureContentsWeekList.size();
    }

    @Override
    public Object getItem(int position) {
        return lectureContentsWeekList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Integer lectureContentWeek = lectureContentsWeekList.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_list, parent, false);
        }
        else {
            View newView = new View(context);
            newView = (View) convertView;
        }

        // Set Week #
        TextView currentWeek = (TextView) convertView.findViewById(R.id.contentsList_week);
        currentWeek.setText( "Week " + lectureContentWeek);

        // Inner ListView
        ListView contentsListView = convertView.findViewById(R.id.weeks_lectureContentsListView);
        ContentsListAdapter contentsListAdapter = new ContentsListAdapter(lectureContentsList);
        contentsListView.setAdapter(contentsListAdapter);
        contentsListView.setVisibility(View.INVISIBLE);

        // Get Lecture Data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .whereEqualTo("week", lectureContentWeek)
                .whereEqualTo("lectureId", currentLecture.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                      @Override
                      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                          for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                              if (document.exists()) {
                                  lectureContentsList.add(document.toObject(Post.class));
                                  Log.d("successAddPost", document.toObject(Post.class).getTitle());

                                  ContentsListAdapter contentsListAdapter = new ContentsListAdapter(lectureContentsList);
                                  contentsListView.setAdapter(contentsListAdapter);
                              }
                          }
                      }
                  });
        db.collection("exams")
                .whereEqualTo("week", lectureContentWeek)
                .whereEqualTo("lectureId", currentLecture.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            if (document.exists()) {
                                lectureContentsList.add(document.toObject(Exam.class));
                                Log.d("successAddExam", document.toObject(Exam.class).getTitle());

                                ContentsListAdapter contentsListAdapter = new ContentsListAdapter(lectureContentsList);
                                contentsListView.setAdapter(contentsListAdapter);
                            }
                        }
                    }
                });
        db.collection("assignments")
                .whereEqualTo("week", lectureContentWeek)
                .whereEqualTo("lectureId", currentLecture.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            if (document.exists()) {
                                lectureContentsList.add(document.toObject(Assignment.class));
                                Log.d("successAddAssignment", document.toObject(Assignment.class).getTitle());

                                ContentsListAdapter contentsListAdapter = new ContentsListAdapter(lectureContentsList);
                                contentsListView.setAdapter(contentsListAdapter);
                            }
                        }
                    }
                });



        contentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.toastTest(context, "gotoContentsPageTODO");
            }
        });

        // Upload Button
        Button uploadButton = convertView.findViewById(R.id.contentsList_uploadButton);
        if (currentUser.getIsManager() != 1)
            uploadButton.setVisibility(View.INVISIBLE);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lectureContentsWeek", String.valueOf(lectureContentWeek) );
                Utils.gotoPage(context, UploadContentPage.class, String.valueOf(lectureContentWeek));
            }
        });

        // Toggle Button
        ToggleButton toggleButton = convertView.findViewById(R.id.contentsList_toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    contentsListView.setVisibility(View.VISIBLE);
                }
                else {
                    contentsListView.setVisibility(View.INVISIBLE);
                }
            }
        });

        return convertView;
    }
}
