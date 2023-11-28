package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.se.blueboard.MainActivity;
import com.se.blueboard.MessageSendPage;
import com.se.blueboard.MessageViewPage;
import com.se.blueboard.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Message;
import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class MessageListAdapter extends FirestoreRecyclerAdapter<Message, MessageListAdapter.MyViewHolder> {
    private Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView person, subject, date, content, receivedOrSend;
        public ImageView blueCircle;
        private FirebaseController controller = new FirebaseController();
        public MyViewHolder(View v) {
            super(v);
            person = v.findViewById(R.id.Sender);
            subject = v.findViewById(R.id.Subject);
            date = v.findViewById(R.id.Date);
            content = v.findViewById(R.id.Content);
            blueCircle = v.findViewById(R.id.Blue_Circle);
            receivedOrSend = v.findViewById(R.id.receivedOrSend);

            v.setOnClickListener(view -> {
                Message clickedMessage = getItem(getBindingAdapterPosition());
                String messageId = clickedMessage.getId();
                if (clickedMessage.getIsRead() == false) {
                    clickedMessage.setRead();
                    controller.updateData(clickedMessage);
                }
                if (mContext == null)
                    Log.d("MyViewHolder: ", "NULL");
                Utils.gotoPage(mContext, MessageViewPage.class, messageId);
            });
        }
    }

    public MessageListAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, Message message) {
//        super.onBindViewHolder(holder, position);
        SimpleDateFormat newFormat;
        FirebaseController controller = new FirebaseController();
        Log.d("onBindViewHolder: ", message.getId());
        if(message.getSenderId().equals(MainActivity.loginUser.getId())){
            controller.getUserData(message.getReceiverId(), new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    User receiver = (User) object;
                    holder.person.setText(receiver.getName());
                    holder.receivedOrSend.setText("보낸 메시지");
//                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MessageListAdapter receiver", e.getMessage());
                }
            });
        }
        else{
            controller.getUserData(message.getSenderId(), new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    User sender = (User) object;
                    holder.person.setText(sender.getName());
                    holder.receivedOrSend.setText("받은 메시지");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MessageListAdapter sender", e.getMessage());
                }
            });
        }

        Calendar cal1, cal2;
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal1.setTime(message.getDate());

        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            newFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        } else if (cal1.get(Calendar.DATE) != cal2.get(Calendar.DATE)) {
            newFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        } else {
            newFormat = new SimpleDateFormat("HH:mm");
        }

        holder.subject.setText(message.getTitle());
        holder.date.setText(newFormat.format(message.getDate()));
        holder.content.setText(message.getContent());
        if (message.getIsRead() == true || message.getSenderId().equals(MainActivity.loginUser.getId()))
            holder.blueCircle.setVisibility(View.INVISIBLE);
        else
            holder.blueCircle.setVisibility(View.VISIBLE);
    }

    // 새로운 뷰 생성
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 뷰 생성
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
        super.onDataChanged();

    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        // Called when there is an error getting a query snapshot. You may want to update
        // your UI to display an error message to the user.
        // ...
        Log.d("onError: ", e.getMessage());
    }
}