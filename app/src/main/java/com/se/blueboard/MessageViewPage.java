package com.se.blueboard;

import static android.view.KeyEvent.KEYCODE_BACK;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
                message.deleteMsg(message.getReceiverId()==HomePage.currentUser.getId());
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
                if(message.getReceiverId().equals(HomePage.currentUser.getId())){
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
                SimpleDateFormat newFormat;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KEYCODE_BACK) {
            Utils.gotoPage(this, MessageBoxPage.class, null);
        }
        return true;
    }
}
