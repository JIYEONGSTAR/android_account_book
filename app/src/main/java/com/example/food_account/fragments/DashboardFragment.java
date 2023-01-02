package com.example.food_account.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.food_account.R;
import com.example.food_account.YearMonthPickerDialog;
import com.example.food_account.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {
    public String TAG = "DashboardFragment";
    private EditText et_month; //월 수정
    private FragmentDashboardBinding binding;
    private Calendar myCalendar = Calendar.getInstance(); //캘린더
    private View view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private String monthAndYear;
    PieChart pieChart;
    BarChart barChart;
    ArrayList<PieEntry> yValues;
    ArrayList<BarEntry> barValues;
    Map<String,Integer> map=new HashMap();	//<키 자료형, 값 자료형>
    Map<String,Integer> barMap=new HashMap();	//<키 자료형, 값 자료형>
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        //year month picker dialog의 OK버튼을 누르면 listener가 불린다.(onDateSet)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "yyyy-MM";    // 출력형식   2022-11
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_month = (EditText) view.findViewById(R.id.edit_month_year);
        et_month.setText(sdf.format(myCalendar.getTime()));
        monthAndYear = et_month.getText().toString();
        itemShow(); // 월 바뀌면 다시 함수 호출
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //초기 날짜
        String myFormat = "yyyy-MM";    // 출력형식   2022-11
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_month = (EditText) view.findViewById(R.id.edit_month_year);
        String today = sdf.format(myCalendar.getTime());
        et_month.setText(today);
        monthAndYear = et_month.getText().toString();
        //날짜 선택
        et_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPickerDialog pd = new YearMonthPickerDialog();
                pd.setListener(myDatePicker);
                pd.show(getFragmentManager(), "YearMonthPickerTest");
            }
        });

        //파이차트
        pieChart = (PieChart)view.findViewById(R.id.chart_percent_keyword);
        pieChart.setUsePercentValues(true);
        yValues = new ArrayList<PieEntry>();

        //바차트
        barChart = (BarChart) view.findViewById(R.id.chart_bar_keyword);
        barValues = new ArrayList<BarEntry>();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        itemShow();

    }


    private void itemShow() {
        //외식, 집밥 비율 불러오기
        firebaseFirestore.collection("post")
                .whereEqualTo("monthAndYear",monthAndYear)
                .whereEqualTo("id", firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //맵 초기화
                            map.put("집밥", 0);
                            map.put("외식", 0);
                            barMap.put("집밥", 0);
                            barMap.put("외식", 0);
                            yValues.clear();
                            barValues.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getData().get("keyword").equals("집밥")){
                                    map.put("집밥",map.get("집밥")+1);
                                    barMap.put("집밥",barMap.get("집밥")+Integer.parseInt(document.getData().get("price").toString()));
                                }else{
                                    map.put("외식",map.get("외식")+1);
                                    barMap.put("외식",barMap.get("외식")+Integer.parseInt(document.getData().get("price").toString()));

                                }
                            }
                            printChart();
                            barChart();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void printChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        yValues.add(new PieEntry(map.get("집밥"),"집밥"));
        yValues.add(new PieEntry(map.get("외식"),"외식"));

        Description description = new Description();
        description.setText("먹은 횟수로 비율구함"); //라벨
        description.setTextSize(10);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"타입");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }

    private void barChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setExtraOffsets(5,10,5,5);

        barValues.add(new BarEntry( 0f, barMap.get("집밥")));
        barValues.add(new BarEntry( 1f, barMap.get("외식")));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("집밥");
        labels.add("외식");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);// 격자선 설정 (default = true)
        xAxis.setDrawAxisLine(false);// x축 선 설정 (default = true)
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);

        YAxis yAxis = barChart.getAxisRight();
        yAxis.setEnabled(false); //오른쪽 Y
        barChart.animateY(1000,Easing.EaseInOutCubic); //에니메이션 효과


        BarDataSet dataSet = new BarDataSet(barValues,"타입");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}