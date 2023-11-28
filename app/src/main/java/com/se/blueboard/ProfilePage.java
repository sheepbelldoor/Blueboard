package com.se.blueboard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.Firebase;
import com.se.blueboard.databinding.ProfileBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class ProfilePage extends AppCompatActivity {

    FirebaseController controller = new FirebaseController();

    public static User currentUser = User.makeUser();

    Utils util = Utils.makeUtils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.profile);
        ProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.profile);

        ImageView imageView = findViewById(R.id.profile_image);

        // image test
        // 1. 이미지를 bitmap으로 가져옴
        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile);
        // 2. bitmap 을 string으로 변환
        String imgString = bitmapToString(image);
        Log.d("Bitmap", imgString);

        // image test

        // TODO: LoginPage에서 id intent로 받아서 parameter 입력
        controller.getUserData("1", new MyCallback() {
            @Override
            public void onSuccess(Object object) {
                currentUser = (User) object;
                Log.d("onSuccessGetUserDataProfile", currentUser.toString());

                // // 관리자면 관리자 버튼 보이고 학번 가림
                if(util.isAdmin(currentUser)){
                  TextView adminView = findViewById(R.id.admin);
                  adminView.setVisibility(View.VISIBLE);

                  TextView studentIdView = findViewById(R.id.studentId);
                  studentIdView.setVisibility(View.INVISIBLE);
                }

                // 학번 마스킹 및 소속
                String maskingStudentId = util.masking(String.valueOf(currentUser.getStudentId()));
                String in = currentUser.getInstitution().concat("\n" + currentUser.getMajor());
                // 프로필 사진 비트맵 변환
                String profile = currentUser.getProfile();
                Bitmap imgBitmap = stringToBitmap(profile);
                imageView.setImageBitmap(imgBitmap);

                binding.setUsername(currentUser.getName());
                Log.d("ProfileName", currentUser.getName());
                binding.setEmail(currentUser.getEmail());
                binding.setStudentId("(" + maskingStudentId + ")");
                binding.setIn(in);

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserDataProfile", e.getMessage());
            }
        });

        // 개인정보 수정 버튼
        Button editButton = findViewById(R.id.edit);
        editButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), EditProfilePage.class, null);
        });

        // 홈 버튼
        Button homeButton = findViewById(R.id.icon_home);
        homeButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), HomePage.class, null);
        });

        // 알림 버튼
        Button notiButton = findViewById(R.id.icon_notification);
        notiButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), NotificationPage.class, null);
        });

        // 메시지 이동
    }
    public String bitmapToString(Bitmap bitmap){
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        image = Base64.getEncoder().encodeToString(byteArray);
        return image;
    }

    public Bitmap stringToBitmap(String data){
        Bitmap bitmap = null;
        byte[] byteArray = Base64.getDecoder().decode(data);
        ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
        bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }
}
