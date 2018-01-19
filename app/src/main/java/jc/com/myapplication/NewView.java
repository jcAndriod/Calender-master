package jc.com.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 56553 on 2018/1/18.
 */

public class NewView extends LinearLayout {

    private ImageView btnProve;
    private ImageView btnNext;
    private TextView txtData;
    private GridView gview;
    private String displayFormat;
    private Calendar curData = Calendar.getInstance();//时间的操作类，


    public NewCalenderListener listener;

    public NewView(Context context) {
        super(context);
    }

    public NewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public NewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }


    private void initControl(Context context, AttributeSet attrs) {
        bindControl(context);
        bindControlEvent();


        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NewView);
        try {
            String format = ta.getString(R.styleable.NewView_dateFormat);
            displayFormat =format;
            if (displayFormat == null){
                displayFormat = "MMM yyyy";
            }
        }finally {
            ta.recycle();
        }

        renderCalender();

    }

    private void bindControlEvent() {
        btnProve.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //向前翻一个月   减1
                curData.add(Calendar.MONTH, -1);
                renderCalender();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //向后翻一个月   加1
                curData.add(Calendar.MONTH, 1);
                renderCalender();
            }
        });
    }

    private void bindControl(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);
        btnProve = findViewById(R.id.btlast);
        btnNext = findViewById(R.id.btnext);
        txtData = findViewById(R.id.tv_head);
        gview = findViewById(R.id.calender_grid);
    }

    //渲染视图
    private void renderCalender() {
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        txtData.setText(sdf.format(curData.getTime()));


        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curData.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int prodays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -prodays);

        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        gview.setAdapter(new CalenderAdapter(getContext(), cells));
        gview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener == null){
                    return false;

                }else
                {
                    listener.onItemLongPress((Date) parent.getItemAtPosition(position));
                    return true;
                }

            }
        });
    }
//适配器
    private class CalenderAdapter extends ArrayAdapter<Date> {

        private final LayoutInflater inflater;

        public CalenderAdapter(@NonNull Context context, ArrayList<Date> days) {
            super(context, R.layout.calender_day, days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calender_day, parent, false);

            }
            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));
            Date now = new Date();

            //判断是当前月的day是黑色，不是当前月（上个月或者下个月）的day设置灰色
            boolean isTheSameMonth = false;
            if (date.getMonth() == now.getMonth()) {
                isTheSameMonth = true;
            }

            if (isTheSameMonth) {
                ((TextView) convertView).setTextColor(Color.parseColor("#000000"));
            } else {
                ((TextView) convertView).setTextColor(Color.parseColor("#666666"));
            }
            //判断当前日期设置成红色
            if (now.getDate() == date.getDate() && now.getMonth() == date.getMonth() && now.getYear() == date.getYear()) {
                ((TextView) convertView).setTextColor(Color.parseColor("#ff0000"));
                ((Calender_TextView) convertView).isToday = true;
            }
            return convertView;
        }
    }
    public interface NewCalenderListener{
        void onItemLongPress(Date day);
    }
}
