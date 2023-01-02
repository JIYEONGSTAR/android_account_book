package com.example.food_account.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.food_account.Information;
import com.example.food_account.R;
import com.example.food_account.activities.LoginActivity;
import com.example.food_account.activities.SignUpActivity;
import com.example.food_account.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationFragment";
    private FragmentNotificationsBinding binding;
    private String nickName, foodExpense;
    private EditText e_nick_name,e_food_expense;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser==null){
            //firebaseUser가 없을 때 로그인 액티비티로 넘어가기
            myStartActivity(LoginActivity.class);
        }

        DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            nickName = document.getData().get("nickname").toString();
                            foodExpense = document.getData().get("food_expense").toString();
                            setText(nickName,foodExpense);
                        }
                    }

                }
            }
        });

        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        view.findViewById(R.id.button_logout).setOnClickListener(onClickListener);
        view.findViewById(R.id.button_change_info).setOnClickListener(onClickListener);
        e_nick_name = (EditText) view.findViewById(R.id.edit_info_nickname);
        e_food_expense = (EditText) view.findViewById(R.id.edit_info_food_expense);
        return view;
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.button_logout:
                    Log.e("click","로그아웃하기");
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(LoginActivity.class);
                case R.id.button_change_info:
                    if(firebaseUser==null){
                        //firebaseUser가 없을 때 로그인 액티비티로 넘어가기
                        myStartActivity(LoginActivity.class);
                    }else{
                        changeInfo();
                    }

            }
        }
    };

    private void changeInfo(){
        String nickname = e_nick_name.getText().toString();
        int food_expense = Integer.parseInt(e_food_expense.getText().toString());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            //firebaseUser가 없을 때 로그인 액티비티로 넘어가기
            myStartActivity(LoginActivity.class);
            return;
        }
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Information information = new Information(nickname,food_expense,uid);
        db.collection("users").document(user.getUid()).set(information)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "회원 정보 수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        e_nick_name.clearFocus();
                        e_food_expense.clearFocus();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "회원 정보 수정에 실패습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setText(String nickName,String foodExpense){

        e_nick_name.setText(nickName);
        e_food_expense.setText(foodExpense);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}