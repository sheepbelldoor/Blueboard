package com.se.blueboard;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.se.blueboard.databinding.EditProfileBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

public class EditProfilePage extends AppCompatActivity {

    FirebaseController controller = new FirebaseController();

    public static User currentUser = User.makeUser();

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

        // TODO: LoginPage에서 id intent로 받아서 parameter 입력
        controller.getUserData("1", new MyCallback() {
            @Override
            public void onSuccess(Object object) {
                currentUser = (User) object;
                Log.d("onSuccessGetUserDataEditProfile", currentUser.toString());

                // 한글 이름 영문 이름 나누기
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

                    if(imgString == null)
                        editProfile = currentUser.getProfile();
                    else
                        editProfile = imgString;

                    // 비어있는 값이 있으면 에러 메시지 띄워주기
                    if(editKorName.length() == 0 || editEngName.length() == 0 || editMail.length() == 0)
                        util.showErrMsg(getApplicationContext(), "값을 모두 입력해주세요");
                    else{
                        String editName = editKorName.getText().toString() + "/" + editEngName.getText().toString();

                        User editUser = User.makeUser(currentUser.getId(), currentUser.getAccountId(), editName, currentUser.getInstitution(),
                                currentUser.getMajor(), editMail.getText().toString(), editProfile, currentUser.getCourses(),
                                currentUser.getSentMessages(), currentUser.getReceivedMessages(), currentUser.getAlarms(),
                                currentUser.getGrade(), currentUser.getStudentId());

                        controller.sendUserData(editUser);
                        Log.d("EditProfile", "sendUserData");

                        // 프로필 페이지로 이동
                        Utils.gotoPage(getApplicationContext(), ProfilePage.class);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserDataEditProfile", e.getMessage());
            }
        });

        Button homeButton = findViewById(R.id.icon_home);
        homeButton.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), HomePage.class);
        });

        // TODO: 아이콘 클릭 시 메시지, 알림 페이지 이동
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
