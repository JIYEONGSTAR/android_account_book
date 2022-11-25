// 기본 데코레이터, 기록 없다면 클릭 안되게
package com.example.food_account.decorators;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class DefaultDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();

    public DefaultDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == (int)weekDay;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}