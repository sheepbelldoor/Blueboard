package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.se.blueboard.MainActivity;
import com.se.blueboard.MessageSendPage;
import com.se.blueboard.MessageViewPage;
import com.se.blueboard.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Message;
import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Message> mDataset = new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView person, subject, date, content;
        public ImageView blueCircle;

        public MyViewHolder(View v) {
            super(v);
            person = v.findViewById(R.id.Sender);
            subject = v.findViewById(R.id.Subject);
            date = v.findViewById(R.id.Date);
            content = v.findViewById(R.id.Content);
            blueCircle = v.findViewById(R.id.Blue_Circle);

            v.setOnClickListener(view -> {
                Message clickedMessage = mDataset.get(getBindingAdapterPosition());
                String messageId = clickedMessage.getId();
                Utils.gotoPage(mContext, MessageViewPage.class, messageId);
            });
        }
    }
    // 배열 데이터를 받는 생성자
    public MessageListAdapter(Context myContext, List<Message> myDataset) {
        mContext = myContext;
        mDataset = myDataset;
    }

    // 새로운 뷰 생성
    @Override
    public MessageListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 뷰 생성
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // 뷰 내용 수정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // 현재 포지션에 해당하는 데이타 값을 가져옴
        // 해당 값을 뷰에 적용
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy");
        Message myMessage = mDataset.get(position);

        FirebaseController controller = new FirebaseController();
        if(myMessage.getSenderId().equals(MainActivity.loginUser.getId())){
            controller.getUserData(myMessage.getReceiverId(), new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    User receiver = (User) object;
                    holder.person.setText(receiver.getName());
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MessageListAdapter receiver", e.getMessage());
                }
            });
        }
        else{
            controller.getUserData(myMessage.getSenderId(), new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    User sender = (User) object;
                    holder.person.setText(sender.getName());
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MessageListAdapter sender", e.getMessage());
                }
            });
        }
        holder.subject.setText(myMessage.getTitle());
        holder.date.setText(newFormat.format(myMessage.getDate()));
        holder.content.setText(myMessage.getContent());
        if (myMessage.getIsRead() == true || myMessage.getSenderId().equals(MainActivity.loginUser.getId()))
            holder.blueCircle.setVisibility(View.INVISIBLE);
        else
            holder.blueCircle.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
