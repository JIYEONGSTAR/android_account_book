package com.example.food_account.decorators;

import com.example.food_account.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    Map<CalendarDay,String> priceMap;
    private int color;
    private HashSet<CalendarDay> dates;
    private TextView textView;
    public EventDecorator( Map<CalendarDay,String> priceMap, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.calendar_background);
        this.priceMap = priceMap;
//        this.textView = textView;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day){
        return priceMap.containsKey(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);

    }


}

