package views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication.R;



public class ScaleView extends View {

    private final Paint paint = new Paint();
    float scaleFactor=0.9f;

    float width;
    Float currText=0f;
    float rMin;
    float rMax;
    Float prevCur;
    Float currShow=0f;
    Float diff=0f;
    public ScaleView(Context context) {
        super(context);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width =  canvas.getWidth() * scaleFactor;
        float originX = this.getX();
        float originY = this.getY();
        float height = 100;
        paint.setColor(R.color.currStep);
        paint.setAlpha(64);
        //canvas.drawRect(originX + 1 * height, originY + 1 * height, originX + (1 + 1) * height, originY + (1 + 1) * height, paint);
        canvas.drawRect(0,0,1100,1000,paint);

        if(currText!=null) {

            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            canvas.drawText(String.valueOf(currText), width / 2, height / 2, paint);
            paint.setColor(R.color.purple_700);
            paint.setAlpha(32);

            canvas.drawRect(0 , 0, originX+currLength(width,currText), width, paint);
//            if(prevCur==null){
//                canvas.drawRect(0 , 0, currLength(width,currText), width, paint);
//            }else {
////                for (currShow = prevCur; currShow <= currText; currShow += 0.01f) {
//                Log.v("HHHH", String.valueOf(currLength(width,currShow)));
//                System.out.println(currLength(width,currShow));
//                    canvas.drawRect(0, 0, currLength(width,currShow), width, paint);
//                    if(diff<0){
//                        if(currShow>currText){
//
//                            currShow+=diff;
//                            invalidate();
//                        }
//                    }else if(diff>0){
//                        if(currShow<currText){
//
//                            currShow+=diff;
//                            invalidate();
//                        }
//                    }
//
//
//
//            }

        }

    }
public float currLength(float width, Float currVal){
        if(currVal>3){
            return 1000f;
        }
        if(currVal<-3){
            return 0;
        }

        return normalize(currVal, -3f, 3f, width, 0f);
}

public float normalize(float old_value, float old_min, float old_max, float new_max, float new_min)
{
    return ( (old_value - old_min) / (old_max - old_min) ) * (new_max - new_min) + new_min;
}

    public synchronized void setCurrText(Float currText){
        prevCur=this.currText;
        currShow=prevCur;
        this.currText=currText;
        if(prevCur>currShow){
            diff=-0.01f;
        }else{
            diff=0.01f;
        }
        invalidate();
    }
}
