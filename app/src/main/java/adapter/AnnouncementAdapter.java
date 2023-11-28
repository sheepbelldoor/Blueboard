package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import com.se.blueboard.R;

import java.util.ArrayList;
import java.util.Date;

import model.Announcement;

//공지사항 리스트뷰 띄우기 위한 Adapter.
public class AnnouncementAdapter extends BaseAdapter {

    private TextView titleTextView;
    private TextView dateTextView;

    public ArrayList<Announcement> announcements = new ArrayList<>();

    public AnnouncementAdapter(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    @Override
    public int getCount() {
        return announcements.size();
    }

    @Override
    public Object getItem(int position) {
        return announcements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final int pos = position;
        final Context context = parent.getContext();
        Announcement announcement = announcements.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.announcement_listitem, parent, false);
        }
        else {
            View newView = new View(context);
            newView = (View) convertView;
        }

        titleTextView = (TextView) convertView.findViewById(R.id.announcement_title);
        dateTextView = (TextView) convertView.findViewById(R.id.announcement_text);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd. '게시됨'");

        titleTextView.setText(announcement.getTitle());
        String currentTimetoString = dateFormat.format(announcement.getUploadTime());
        dateTextView.setText(currentTimetoString);

        return convertView;



    }
}
