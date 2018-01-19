package jc.com.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 56553 on 2018/1/19.
 */

@SuppressLint("AppCompatCustomView")
public class Calender_TextView extends TextView {
    public boolean isToday =false;
    private Paint paint = new Paint();



    public Calender_TextView(Context context) {
        super(context);
    }

    public Calender_TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public Calender_TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }
    private void initControl(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#ff0000"));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isToday) {
            canvas.translate(getWidth() / 2, getHeight() / 2);//放在控件中间
            canvas.drawCircle(0, 0, getWidth() / 2, paint);
        }
    }
}
