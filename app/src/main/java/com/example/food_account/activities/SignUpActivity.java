package com.example.food_account.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_account.R;
import com.example.food_account.activities.InformationActivity;
import com.example.food_account.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG="SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_sign_up).setOnClickListener(onClickListener);
        findViewById(R.id.button_gotoLogin).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_sign_up:
                    signUp();
                    break;
                case R.id.button_gotoLogin:
                    startLoginActivity();
                    break;
            }
        }
    };

    private void signUp(){
        String email = ((EditText) findViewById(R.id.editText_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Password)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.editText_checkPassword)).getText().toString();
        if(email.length()>0&&password.length()>0&&passwordCheck.length()>0) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공시 토스트 보여준 후 정보가입으로
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원 가입이 완료되었습니다.");
                                    startInformationActivity();
                                } else {
                                    if (task.getException() != null) {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        startToast(task.getException().toString());
                                    }

                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startInformationActivity(){
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }

}
