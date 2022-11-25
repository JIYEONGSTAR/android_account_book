//package com.example.food_account.ui.home;
//
//import com.example.food_account.R;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.food_account.WritePostActivity;
//import com.example.food_account.databinding.FragmentHomeBinding;
//import com.example.food_account.decorators.EventDecorator;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
//
//import java.util.ArrayList;
//
//public class HomeFragment extends Fragment {
//
//    MaterialCalendarView materialCalendarView;
//    private FirebaseFirestore firebaseFirestore;
//
//    private FragmentHomeBinding binding;
//
//    public HomeFragment(){
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        materialCalendarView = (MaterialCalendarView)root.findViewById(R.id.materialCalendar);
//        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
//        calendarDayList.add(CalendarDay.today());
//        calendarDayList.add(CalendarDay.from(2022, 10, 25));
//
//        materialCalendarView.addDecorator(new EventDecorator(calendarDayList,getActivity()));
//
//        return root;
//    }
//
////    View.OnClickListener onClickListener = new View.OnClickListener() {
////        @Override
////        public void onClick(View v) {
////            switch (v.getId()) {
////                case R.id.floatingActionButton:
////                    startActivity(WritePostActivity.class);
////                    break;
////            }
////        }
////    };
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//    private void startActivity(Class c) {
//        Intent intent = new Intent(getActivity(), c);
//        startActivityForResult(intent, 0);
//    }
//}

package com.example.food_account.ui.home;

import com.example.food_account.PopupActivity;
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

import com.example.food_account.WritePostActivity;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    MaterialCalendarView materialCalendarView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private TextView textView_title;
    private FragmentHomeBinding binding;

    public ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
    public HashMap<CalendarDay,String> accountData = new HashMap<>();

    public ArrayList<String> preData = new ArrayList<>();
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
        DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            textView_title.setText(document.getData().get("nickname").toString()+"님의 식비가계부");
                        }
                    }
                }
            }
        });


        binding = FragmentHomeBinding.inflate(inflater, container, false);

        firebaseFirestore.collection("post")
                .whereEqualTo("id", firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("성공","성공했다."+task.getResult());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Object date = document.getData().get("date");
                                int y = Integer.parseInt(date.toString().split("-")[0]);
                                int m = Integer.parseInt(date.toString().split("-")[1])-1;
                                int d = Integer.parseInt(date.toString().split("-")[2]);
                                Log.d(TAG, document.getId() + " => " + y+m+d);
//                                addDate(y,m,d);
                                calendarDayList.add(CalendarDay.from(y, m, d));
                                accountData.put(CalendarDay.from(y, m, d),document.getData().get("price").toString());
                                materialCalendarView.addDecorator(new EventDecorator(accountData,getActivity()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        materialCalendarView = (MaterialCalendarView)view.findViewById(R.id.materialCalendar);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        Log.d(TAG, String.valueOf(calendarDayList));
//        calendarDayList.add(CalendarDay.today());
        materialCalendarView.addDecorators(new SundayDecorator(),new SaturdayDecorator(),new DefaultDecorator());
        materialCalendarView.setOnDateChangedListener((eventDay,w,g)->{
            //클릭 시
            Log.d("", String.valueOf(eventDay.getSelectedDate()));
            Intent intent = new Intent(getActivity(),PopupActivity.class);
            intent.putExtra("data", "Test Popup");
            startActivityForResult(intent, 1);

        });


        return view;
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

//    private void addDate(int y,int m,int d){
//        calendarDayList.add(CalendarDay.from(y,m,d));
//    }
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