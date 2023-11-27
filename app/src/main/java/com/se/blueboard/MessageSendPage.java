package com.se.blueboard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import model.Message;

import utils.FirebaseController;
import utils.Utils;

public class MessageSendPage extends AppCompatActivity {
    private EditText recipientText, titleText, contentText;
    private Button send, addFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_send);
        FirebaseController controller = new FirebaseController();
        recipientText = (EditText) findViewById(R.id.editTextRecipient);
        titleText = (EditText) findViewById(R.id.editTextTitle);
        contentText = (EditText) findViewById(R.id.editTextContent);

        // 창 닫기 버튼
        Button close = findViewById(R.id.icon_close);
        close.setOnClickListener(view -> {
            finish();
        });

        send = findViewById(R.id.buttonSend);
        send.setOnClickListener(view -> {
            String recipient = recipientText.getText().toString();
            String title = titleText.getText().toString();
            String content = contentText.getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionRef = db.collection("users");
//            Query query = collectionRef.whereEqualTo("email", );
//            Message sendMsg = Message.makeMessage("10", "");

//            controller.sendMessageData();
        });

        addFile = findViewById(R.id.icon_file);
    }
}
