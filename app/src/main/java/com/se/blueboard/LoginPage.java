package com.se.blueboard;

import static com.se.blueboard.HomePage.currentUser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import model.Account;
import model.User;
import utils.FirebaseController;
import utils.MyCallback;
import utils.Utils;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginPage extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.POST_NOTIFICATIONS
    };

    private FirebaseAuth auth = null;
    private FirebaseAuth firebaseAuth;

    private Button buttonRegister;
    private Button buttonLogin;
    
    // login 작업에 사용
    private EditText userID;
    private EditText userPW;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth = FirebaseAuth.getInstance();

        // 회원가입
        buttonRegister = (Button) findViewById(R.id.register_bt);
        buttonRegister.setOnClickListener(view -> {
            Utils.gotoPage(getApplicationContext(), Create.class, null);
        });

        // 로그인
        userID = (EditText) findViewById(R.id.user_id);
        userPW = (EditText) findViewById(R.id.user_pw);
        buttonLogin =  (Button) findViewById(R.id.login_bt);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                login(userID.getText().toString(), userPW.getText().toString(), buttonLogin);
            }
        });



    }

    public void login(String id, String pw, Button btLogin){

        verifyStoragePermissions(this);

        FirebaseController controller = new FirebaseController();

        final Account[] currentAcc = {Account.makeAccount()};

        if(!id.equals("") && !pw.equals("")){
            controller.getAccountData(id, new MyCallback() {
                @Override
                public void onSuccess(Object object) {
                    currentAcc[0] = (Account) object;

                    // ID
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("accounts")
                            .document(id)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()) {
                                            Log.d("successGetAccountData", "Success to get accounts data on DB");
                                            if(!currentAcc[0].getAccountPw().equals(pw))
                                            {
                                                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                currentUser.setId(id);
                                                Utils.gotoPage(getApplicationContext(), HomePage.class, id);
                                            }
                                        } else {

                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
                                        Log.d("failGetAccountData", "Fail to get accounts data on DB");
                                    }
                                }
                            });
                    // ID

                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("failGetAccountData", e.getMessage());
                }
            });
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
