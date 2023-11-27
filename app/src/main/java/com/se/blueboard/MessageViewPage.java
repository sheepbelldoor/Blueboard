package com.se.blueboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

import model.Message;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class MessageViewPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);

        FirebaseController controller = new FirebaseController();

        String messageId = getIntent().getStringExtra("key");

        // 메시지 정보 DB로부터 불러오기
        controller.getMessageData(messageId, new MyCallback() {

            @Override
            public void onSuccess(Object object) {
                Message message = (Message) object;

                // 삭제 버튼
                Button delete = findViewById(R.id.icon_delete);
                delete.setOnClickListener(view -> {
                message.deleteMsg(message.getReceiverId()==MainActivity.loginUser.getId());
                });

                // 답장 버튼
                Button reply = findViewById(R.id.icon_reply);
                reply.setOnClickListener(view -> {
                    Utils.gotoPage(MessageViewPage.this, MessageSendPage.class, message.getId());
                });

                // 메시지 정보 불러오기
                // 제목
                TextView title = findViewById(R.id.textViewTitle);
                title.setText(message.getTitle());

                // 발신자 또는 수신자 이름
                if(message.getReceiverId().equals(MainActivity.loginUser.getId())){
                    // 발신자
                    controller.getUserData(message.getSenderId(), new MyCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            TextView sender = findViewById(R.id.textViewPersonName);
                            TextView isSender = findViewById(R.id.textViewSenderOrReceiver);
                            isSender.setText("From :");
                            sender.setText(((model.User) object).getName());
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d("MessageViewPage sender", e.getMessage());
                        }
                    });
                }
                else{
                    // 수신자
                    controller.getUserData(message.getReceiverId(), new MyCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            TextView receiver = findViewById(R.id.textViewPersonName);
                            TextView isReceiver = findViewById(R.id.textViewSenderOrReceiver);
                            isReceiver.setText("To :");
                            receiver.setText(((model.User) object).getName());
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d("MessageViewPage receiver", e.getMessage());
                        }
                    });
                }

                // 날짜
                SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
                TextView date = findViewById(R.id.textViewDate);
                date.setText(newFormat.format(message.getDate()));

                // 내용
                TextView content = findViewById(R.id.textViewMessageContent);
                content.setText(message.getContent());

                // TODO : 프로필 사진
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("MessageViewPage message load", e.getMessage());
            }
        });

        // 뒤로가기 버튼
        Button back = findViewById(R.id.icon_back);
        back.setOnClickListener(view -> {
            Utils.gotoPage(this, MessageBoxPage.class, null);
        });
    }
}
