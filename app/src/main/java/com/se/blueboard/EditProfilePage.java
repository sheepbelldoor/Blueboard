package com.se.blueboard;

import static com.se.blueboard.HomePage.currentUser;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.se.blueboard.databinding.EditProfileBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.regex.Pattern;

import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class EditProfilePage extends AppCompatActivity {

    FirebaseController controller = new FirebaseController();

    Utils util = Utils.makeUtils();
    ImageView image;
    String imgString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.edit_profile);
        EditProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.edit_profile);

        Button uploadButton = findViewById(R.id.upload);
        Button saveButton = findViewById(R.id.save);

        controller.getUserData(currentUser.getId(), new MyCallback() {
            @Override
            public void onSuccess(Object object) {
                currentUser = (User) object;
                Log.d("onSuccessGetUserDataEditProfile", currentUser.toString());

                // 한글 이름 영문 이름 나누기
                // firebase의 name에 "(한글 이름)/(영문 이름)" 으로 안 돼있으면 오류남 (테스트 시 참고)
                String username = currentUser.getName();
                String[] stSplit = username.split("/");

                // 원래 값 hint로 띄워줌
                binding.setOriginEngName(stSplit[1]);
                binding.setOriginMail(currentUser.getEmail());
                binding.setOriginName(stSplit[0]);

                // 사진 업로드하기
                uploadButton.setOnClickListener(view -> {
                    // 갤러리에서 사진 선택
                    openGallery();
                });

                // 입력 값 저장
                saveButton.setOnClickListener(view -> {
                    EditText editKorName = findViewById(R.id.edit_kor_name);
                    EditText editEngName = findViewById(R.id.edit_eng_name);
                    EditText editMail = findViewById(R.id.edit_mail);
                    String editProfile;

                    String strKorName = editKorName.getText().toString();
                    String strEngName = editEngName.getText().toString();
                    String strMail = editMail.getText().toString();

                    if(imgString == null)
                        editProfile = currentUser.getProfile();
                    else
                        editProfile = imgString;

                    // 이메일 검증
                    Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

                    // 비어있는 값이 있으면 에러 메시지 띄워주기
                    if(editKorName.length() == 0 || editEngName.length() == 0 || editMail.length() == 0)
                        util.showErrMsg(getApplicationContext(), "값을 모두 입력해주세요");
                    else if(!util.lengthAvail(strKorName, 20) || !util.lengthAvail(strEngName, 20) || !pattern.matcher(strMail).matches()){
                        util.showErrMsg(getApplicationContext(), "입력한 값이 올바른지 확인하세요");
                    }
                    else{
                        String editName = strKorName + "/" + strEngName;

                        User editUser = User.makeUser(currentUser.getId(), currentUser.getAccountId(), editName, currentUser.getInstitution(),
                                currentUser.getMajor(), editMail.getText().toString(), editProfile, currentUser.getCourses(),
                                currentUser.getSentMessages(), currentUser.getReceivedMessages(), currentUser.getAlarms(),
                                currentUser.getGrade(), currentUser.getStudentId(), currentUser.getIsManager());

                        controller.sendUserData(editUser);
                        Log.d("EditProfile", "sendUserData");

                        // 프로필 페이지로 이동
                        Utils.gotoPage(getApplicationContext(), ProfilePage.class, null);
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserDataEditProfile", e.getMessage());
            }
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
                        // 프로필 화면으로 이동 (현재 화면이므로 아무 동작 안함)
                        return true;
                }
                return false;
            }
        });

    }

    // 사진 선택하면 imageview로 보여주기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = findViewById(R.id.edit_image);
        if(requestCode == 101) {
            if (resultCode == RESULT_OK){
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try{
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    image.setImageBitmap(imgBitmap);
                    imgString = bitmapToString(imgBitmap);
                    instream.close();
                } catch(Exception e){
                    Log.d("EditProfileImage", e.getMessage());
                }
            }
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    public String bitmapToString(Bitmap bitmap){
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        image = Base64.getEncoder().encodeToString(byteArray);
        return image;
    }
}
