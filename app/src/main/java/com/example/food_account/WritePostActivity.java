package com.example.food_account;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class WritePostActivity extends AppCompatActivity {
    private  static final String TAG="WritePostActivity";
    private FirebaseUser user; //파이어베이스
    private Calendar myCalendar = Calendar.getInstance(); //캘린더
    private RadioGroup radioGroup;
    private RadioButton r_btn1, r_btn2;
    private EditText et_date; //데이트
    private String keyword;
    private Button btn_submit;
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_date = (EditText) findViewById(R.id.Date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        //초기 날짜
        String myFormat = "yyyy-MM-dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_Date = (EditText) findViewById(R.id.Date);
        String today = sdf.format(myCalendar.getTime());
        et_Date.setText(today);
        //날짜 선택
        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(WritePostActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //라디오 버튼 설정
        r_btn1 = (RadioButton) findViewById(R.id.radio_home_food);
        r_btn2 = (RadioButton) findViewById(R.id.radio_out_food);
        r_btn1.setOnClickListener(radioButtonClickListener);
        r_btn2.setOnClickListener(radioButtonClickListener);

        //라디오 그룹 설정
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        findViewById(R.id.button_upload).setOnClickListener(onClickListener); // 수정하기 왜 안되는지 알아보기....

    }
    //라디오 버튼 클릭 리스너
    RadioButton.OnClickListener radioButtonClickListener = new RadioButton.OnClickListener(){
        @Override
        public void onClick(View view) {

            Log.d(TAG, "집밥 : "+r_btn1.isChecked() + "외식 : " +r_btn2.isChecked());
        }
    };

    //라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.radio_home_food){
                keyword = "집밥";
                Toast.makeText(WritePostActivity.this, "집밥이 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(i == R.id.radio_out_food){
                keyword = "외식";
                Toast.makeText(WritePostActivity.this, "외식이 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override public void onBackPressed(){
        super.onBackPressed();
        finish();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_upload:
                    postUpdate();
                    break;
            }
        }
    };

    private void postUpdate(){
        final String title = ((EditText) findViewById(R.id.editTitle)).getText().toString();
        final int price = Integer.parseInt(((EditText) findViewById(R.id.editPrice)).getText().toString());
        final String date = ((EditText) findViewById(R.id.Date)).getText().toString();
        Log.d(TAG,""+title+price+keyword+date);
        if (title.length() > 0 && price>0 && keyword.length()>0) { // 입력이 되면
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String uid = user.getUid();
            PostInfo postInfo = new PostInfo(title,price,date,uid,keyword);
            db.collection("post").document().set(postInfo) //  유저 -> 날짜 별 데이터 생성
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startToast("기록에 성공했습니다.");
                            startMainActivity();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("기록에 실패했습니다.");
                        }
                    });

        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
