package com.se.blueboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import model.User;
import utils.Utils;

public class GroupsPopUp extends AppCompatActivity {

    User popUpUser = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.groups_popup);

        String userData = getIntent().getExtras().getString("key", null);
        String[] temp = userData.split("  ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("name", temp[1])
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        if (document.exists()) {
                            popUpUser = (User) document.toObject(User.class);

                            // Name TextView
                            TextView name = findViewById(R.id.groupsPopUp_name);
                            name.setText("Name:  " + popUpUser.getName());
                            // studentID TextView
                            TextView studentId = findViewById(R.id.groupsPopUp_studentID);
                            studentId.setText("ID:  " + popUpUser.getStudentId());
                            // 유저 ImageView
                            // 이미지 대충 넣어둠
                            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.profile_two);
                            ImageView imageView = findViewById(R.id.groupsPopUp_image);
                            imageView.setImageBitmap(image);
                            // 확인 Button
                            Button ok = findViewById(R.id.groupsPopUp_OK);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });
                            // 메시지 전송 Button
                            Button sendMessage = findViewById(R.id.groupsPopUp_message);
                            sendMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utils.gotoPage(getApplicationContext(), MessageSendPage.class, popUpUser.getId());
                                }
                            });
                        } // End if
                    } // End onSuccess
                }); // End db.collection
    }
}
