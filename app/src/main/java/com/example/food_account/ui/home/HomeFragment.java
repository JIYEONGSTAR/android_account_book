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

import com.example.food_account.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_account.WritePostActivity;
import com.example.food_account.databinding.FragmentHomeBinding;
import com.example.food_account.decorators.EventDecorator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    MaterialCalendarView materialCalendarView;
    private FirebaseFirestore firebaseFirestore;

    private FragmentHomeBinding binding;
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

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        materialCalendarView = (MaterialCalendarView)view.findViewById(R.id.materialCalendar);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
        calendarDayList.add(CalendarDay.today());
        calendarDayList.add(CalendarDay.from(2022, 10, 25));

        materialCalendarView.addDecorator(new EventDecorator(calendarDayList,getActivity()));


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