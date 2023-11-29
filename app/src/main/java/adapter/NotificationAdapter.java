package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.se.blueboard.R;

import java.util.ArrayList;

import model.Alarm;
import model.Announcement;

public class NotificationAdapter extends BaseAdapter {

    private TextView titleTextView;

    private TextView contentTextView;

    public ArrayList<Alarm> alarms = new ArrayList<>();

    public NotificationAdapter(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }
    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        Alarm alarm = alarms.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notification_listitem, parent, false);
        }
        else{
            View newView = new View(context);
            newView = (View) convertView;
        }

        titleTextView = (TextView) convertView.findViewById(R.id.alarm_title);
        contentTextView = (TextView) convertView.findViewById(R.id.alarm_text);

        titleTextView.setText(alarm.getTitle());
        contentTextView.setText(alarm.getContent());

        return convertView;
    }
}
