package com.example.food_account;

import static com.example.food_account.Util.showToast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_account.Listener.OnPostListener;
import com.example.food_account.decorators.EventDecorator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {
    private static final String TAG = "PopupActivity";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private PopupAdapter popupAdapter;
    private ArrayList<PostInfo> postList;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //데이터 가져오기
        Intent intent = getIntent();
        date = intent.getStringExtra("date"); //받아온 날짜
        Log.d(TAG,"date"+date);
        TextView popup_title = (TextView)findViewById(R.id.text_popup_title);
        popup_title.setText(""+date+"의 기록");
        //어댑터

        recyclerView = (RecyclerView) findViewById(R.id.recycleView_popup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PopupActivity.this));
        postList = new ArrayList<>();
        popupAdapter = new PopupAdapter(PopupActivity.this, postList);
        popupAdapter.setOnPostListener(onPostListener);
        recyclerView.setAdapter(popupAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        itemShow();

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(String id) {
            Log.d(TAG,"document_id: "+id);
            firebaseFirestore.collection("post").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(PopupActivity.this, "삭제하였습니다.");
                            startMainActivity();
//                            itemShow();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }


    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    private void itemShow(){
        firebaseFirestore.collection("post")
                .whereEqualTo("id",firebaseUser.getUid())
                .whereEqualTo("date",date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(new PostInfo(
                                        document.getData().get("title").toString(),
                                        Integer.parseInt(document.getData().get("price").toString()),
                                        document.getData().get("date").toString(),
                                        document.getData().get("id").toString(),
                                        document.getData().get("keyword").toString(),
                                        document.getId()
                                ));
                            }
                            popupAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
//        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
