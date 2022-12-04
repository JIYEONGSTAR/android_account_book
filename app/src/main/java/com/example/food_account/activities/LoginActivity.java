package com.example.food_account.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_account.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.food_account.Util.showToast;


public class LoginActivity extends AppCompatActivity {
    private  static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth; //파이어베이스 myAuth

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_login).setOnClickListener(onClickListener);
        findViewById(R.id.button_gotoSignUp).setOnClickListener(onClickListener);

    }

    @Override public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.button_login:
                        Log.e("click","로그인하기");
                        login();
                        break;
                    case R.id.button_gotoSignUp:
                        Log.e("click","회원가입으로가기");
                        startSignUpActivity();
                        break;
                }
        }
    };
    private void login(){
        String email = ((EditText) findViewById(R.id.editText_email_login)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Password_login)).getText().toString();
        Log.d(TAG, String.valueOf(email.length()));
        Log.d(TAG, String.valueOf(password.length()));

        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                showToast(LoginActivity.this, "로그인에 성공하였습니다.");
                                startMainActivity();
                            } else {
                                if (task.getException() != null) {
                                    showToast(LoginActivity.this, task.getException().toString());
                                }
                            }
                        }
                    });
        } else {
            showToast(LoginActivity.this, "이메일 또는 비밀번호를 입력해 주세요.");
        }
    }

    private void startSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
