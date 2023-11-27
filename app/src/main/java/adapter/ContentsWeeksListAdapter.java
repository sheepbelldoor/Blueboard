package adapter;

import android.content.Context;
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

import com.se.blueboard.R;
import com.se.blueboard.UploadContentPage;

import java.util.ArrayList;
import java.util.List;

import model.LectureContent;
import utils.Utils;

public class ContentsWeeksListAdapter extends BaseAdapter {
    ArrayList<Integer> lectureContentsWeekList = new ArrayList<>();

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

        // Test Dummy Data
        LectureContent lectureContent1 = LectureContent.makeLectureContent("test1", "test1", 1, null, null, null, null);
        LectureContent lectureContent2 = LectureContent.makeLectureContent("test2", "test2", 2, null, null, null, null);
        LectureContent lectureContent3 = LectureContent.makeLectureContent("test3", "test3", 3, null, null, null, null);
        LectureContent lectureContent4 = LectureContent.makeLectureContent("test4", "test4", 4, null, null, null, null);

        ArrayList<LectureContent> lectureContentsList = new ArrayList<>();
        lectureContentsList.add(lectureContent1);
        lectureContentsList.add(lectureContent2);
        lectureContentsList.add(lectureContent3);
        lectureContentsList.add(lectureContent4);


        // Inner ListView
        ListView contentsListView = convertView.findViewById(R.id.weeks_lectureContentsListView);
        ContentsListAdapter contentsListAdapter = new ContentsListAdapter(lectureContentsList);
        contentsListView.setAdapter(contentsListAdapter);
        contentsListView.setVisibility(View.INVISIBLE);

        contentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.toastTest(context, "gotoContentsPageTODO");
            }
        });

        // Upload Button
        Button uploadButton = convertView.findViewById(R.id.contentsList_uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoPage(context, UploadContentPage.class, null);
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
