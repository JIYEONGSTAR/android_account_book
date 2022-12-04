package com.example.food_account.fragments;

import com.example.food_account.activities.PopupActivity;
import com.example.food_account.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.food_account.activities.WritePostActivity;
import com.example.food_account.databinding.FragmentHomeBinding;
import com.example.food_account.decorators.DefaultDecorator;
import com.example.food_account.decorators.EventDecorator;
import com.example.food_account.decorators.SaturdayDecorator;
import com.example.food_account.decorators.SundayDecorator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    MaterialCalendarView materialCalendarView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private TextView textView_title,textView_left_account;
    private FragmentHomeBinding binding;

    public ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
    public int month_account = 0;
    public String account = "",month_year = "";

    public HomeFragment(){
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // 타이틀 설정
        textView_title = view.findViewById(R.id.textView_title);
        textView_left_account = view.findViewById(R.id.textView_left_account);


        binding = FragmentHomeBinding.inflate(inflater, container, false);


        materialCalendarView = (MaterialCalendarView)view.findViewById(R.id.materialCalendar);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
//        Log.d("calendarday.today", String.valueOf(CalendarDay.today()));
        String myMonthFormat = "yyyy-MM";    // 출력형식   2022-11
        SimpleDateFormat sdf_m = new SimpleDateFormat(myMonthFormat, Locale.KOREA);
        month_year = sdf_m.format(CalendarDay.today().getDate());
        materialCalendarView.addDecorators(new SundayDecorator(),new SaturdayDecorator(),new DefaultDecorator());
        materialCalendarView.setOnMonthChangedListener((widget,date)->{
            String myFormat = "yyyy-MM";    // 출력형식   2022-11
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
            Log.d(TAG, String.valueOf(widget));
            Log.d(TAG,String.valueOf(date.getDate()));
            month_year = sdf.format(date.getDate());
            Log.d(TAG,month_year);
            itemShow();
        });
        materialCalendarView.setOnDateChangedListener((eventDay,w,g)->{
            //클릭 시
            String myFormat = "yyyy-MM-dd";    // 출력형식   2021-07-26
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
            Intent intent = new Intent(getActivity(),PopupActivity.class);
            intent.putExtra("date", sdf.format(eventDay.getSelectedDate().getDate()));
            startActivityForResult(intent, 1);

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        itemShow();
    }

    private void itemShow() {
        DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            account = document.getData().get("food_expense").toString();
                            textView_title.setText(document.getData().get("nickname").toString()+"님의 식비가계부");
                        }
                    }
                }
            }
        });
        firebaseFirestore.collection("post")
                .whereEqualTo("id", firebaseUser.getUid())
                .whereEqualTo("monthAndYear",month_year)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("성공","성공했다."+task.getResult());
                            month_account = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Object date = document.getData().get("date");
                                int y = Integer.parseInt(date.toString().split("-")[0]);
                                int m = Integer.parseInt(date.toString().split("-")[1])-1;
                                int d = Integer.parseInt(date.toString().split("-")[2]);
                                Log.d(TAG, document.getId() + " => " + y+m+d+"keyword"+document.getData().get("keyword"));
                                Log.d(TAG,"외식?"+String.valueOf(document.getData().get("keyword").toString().equals("외식")));
                                calendarDayList.add(CalendarDay.from(y, m, d));
                                materialCalendarView.addDecorator(new EventDecorator(calendarDayList,getActivity()));
                                month_account+=Integer.parseInt(document.getData().get("price").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        Log.d("돈", String.valueOf(month_account));
                        textView_left_account.setText(account+"원 중 "+month_account+"원 사용");
                    }
                });

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floatingActionButton:
                    myStartActivity(WritePostActivity.class);
                    break;
            }
        }
    };

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