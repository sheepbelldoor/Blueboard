package com.se.blueboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import model.Account;
import model.User;
import utils.FirebaseController;
import utils.Utils;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class Create extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.POST_NOTIFICATIONS
    };

    private Button buttonCompleteRegister;
    private Button buttonBackToLogin;

    // login 작업에 사용
    private EditText newID;
    private EditText newPW;
    private EditText newCheckPW;

    private EditText newName;
    private EditText newGrade;
    private EditText newNum;

    private String newInst;
    private String newDept;
    private String newIsMg;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);


        Spinner spinner = (Spinner)findViewById(R.id.select_institute);
        newInst = spinner.getSelectedItem().toString();

        Spinner spinner2 = (Spinner)findViewById(R.id.select_dept);
        newDept = spinner2.getSelectedItem().toString();

        Spinner spinner3 = (Spinner)findViewById(R.id.select_isMg);
        newIsMg = spinner3.getSelectedItem().toString();

        newID = (EditText) findViewById(R.id.square_id);
        newPW = (EditText) findViewById(R.id.square_pw);
        newCheckPW = (EditText) findViewById(R.id.square_pwcheck);
        newName = (EditText) findViewById(R.id.square_name);
        newGrade = (EditText) findViewById(R.id.square_grade);
        newNum = (EditText) findViewById(R.id.square_num);

        buttonCompleteRegister =  (Button) findViewById(R.id.reg_complete);
        buttonCompleteRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createAccount(
                        newID.getText().toString(), newPW.getText().toString(), newCheckPW.getText().toString(), newName.getText().toString(), newInst, newDept, newGrade.getText().toString(), newNum.getText().toString(), Integer.parseInt(newIsMg)
                );
            }
        });

        buttonBackToLogin = (Button)findViewById(R.id.backToLogin);
        buttonBackToLogin.setOnClickListener(view->{
                Utils.gotoPage(getApplicationContext(), LoginPage.class, null);
        });
    }

    private void createAccount(String id, String pw, String checkpw, String name, String inst, String dept, String grd, String num, int isManager) {

        verifyStoragePermissions(this);

        FirebaseController controller = new FirebaseController();
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
                                Toast.makeText(getApplicationContext(), "중복된 계정입니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                if (!id.equals("") && !pw.equals("")) {
                                    if (!isValidPW(pw)) {
                                        Toast.makeText(
                                                getApplicationContext(), "비밀번호는 영문, 숫자, 특수 문자를 포함한 8자리 문자여야 합니다.’", Toast.LENGTH_SHORT
                                        ).show();
                                        return;
                                    }
                                    if(!pw.equals(checkpw)){
                                        Toast.makeText(
                                                getApplicationContext(), "비밀번호가 일치하지 않습니다.’", Toast.LENGTH_SHORT
                                        ).show();
                                        return;
                                    }

                                    User user = User.makeUser(id, id, name, inst, dept, null, null, null, null, null, null, Integer.parseInt(grd), Integer.parseInt(num), isManager);
                                    Log.d("User", user.toString());
                                    controller.sendUserData(user);
                                    Account userAcc = Account.makeAccount(id, id, pw);
                                    controller.sendAccountData(userAcc);

                                    // 회원가입 완료
                                    Toast.makeText(
                                            getApplicationContext(), "회원가입이 완료되었습니다. 로그인 화면으로 돌아가 로그인하세요.", Toast.LENGTH_SHORT
                                    ).show();
                                    Utils.gotoPage(getApplicationContext(), LoginPage.class, null);
                                }
                            }
                        } else {
                            Log.d("failGetAccountData", "Fail to get accounts data on DB");
                        }
                    }
                });
    }

    private boolean isValidPW(String pw){
        // 영문, 숫자, 특수문자 조합의 8자리 이상인지 확인
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return pw.matches(regex);
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
