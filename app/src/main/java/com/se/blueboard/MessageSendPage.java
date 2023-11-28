package com.se.blueboard;

import static android.view.KeyEvent.KEYCODE_BACK;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import adapter.DialogRecyclerAdapter;
import io.grpc.okhttp.internal.Util;

import model.User;
import utils.FirebaseController;
import utils.Utils;
import model.Message;

public class MessageSendPage extends AppCompatActivity {
    private User receiver;
    private EditText titleText, contentText;
    private Button recipientText, send, addFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_send);
        receiver = null;
        FirebaseController controller = new FirebaseController();
        recipientText = findViewById(R.id.editTextRecipient);
        titleText = (EditText) findViewById(R.id.editTextTitle);
        contentText = (EditText) findViewById(R.id.editTextContent);

        recipientText.setOnClickListener(view -> {
            showDialog();
        });

        // 창 닫기 버튼
        Button close = findViewById(R.id.icon_close);
        close.setOnClickListener(view -> {
            Utils.gotoPage(this, MessageBoxPage.class, null);
        });

        send = findViewById(R.id.buttonSend);
        send.setOnClickListener(view -> {
            if(receiver == null) {
                Toast.makeText(this,"Please Choose sender.", Toast.LENGTH_SHORT).show();

            } else {
                String title = titleText.getText().toString();
                String content = contentText.getText().toString();

                String senderId = MainActivity.loginUser.getId();
                Date date = new Date();

                Message message = Message.makeMessage(senderId + "-" + date.toString(), receiver.getId(), senderId, title, content, date,null, null, false);
                controller.sendMessageData(message);

            }
        });

        addFile = findViewById(R.id.icon_file);
    };

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);

        RecyclerView recyclerView = dialog.findViewById(R.id.dialog_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = FirebaseFirestore.getInstance().collection("users");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class).build();
        DialogRecyclerAdapter mAdapter = new DialogRecyclerAdapter(options, getApplicationContext());
        mAdapter.startListening();

        mAdapter.setOnItemClickListener(item -> {
            // 다이얼로그에서 전달받은 데이터를 사용하여 id=text를 변경
            TextView textView = findViewById(R.id.editTextRecipient);
            String tmp = item.getName() + " (" + Utils.masking(String.valueOf(item.getStudentId())) + ")";
            textView.setText(tmp);
            receiver = item;

            // 다이얼로그 닫기
            mAdapter.stopListening();
            dialog.dismiss();

        });

        recyclerView.setAdapter(mAdapter);

        dialog.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KEYCODE_BACK) {
            Utils.gotoPage(this, MessageBoxPage.class, null);
        }
        return true;
    }


}
