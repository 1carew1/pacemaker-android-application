package org.pacemaker.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * Created by colmcarew on 21/03/16.
 */
public class MyDatePicker extends DatePicker {
    public MyDatePicker(Context context) {
        super(context);
    }

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    //Used so a scrollview can be used with a scroll widget
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
}
