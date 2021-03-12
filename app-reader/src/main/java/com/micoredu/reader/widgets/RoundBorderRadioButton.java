package com.micoredu.reader.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.micoredu.reader.R;


@SuppressWarnings("ResourceType")
public class RoundBorderRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

    private Paint paint;

    private int gap = 3; // 外环与里面的实心圆的间距

    private int circleWidth = 2; //圆环的线条厚度

    private int bgColor;
    private int solidColor;
    private int checkedColor = 0;

    private String labelText = null;
    private int labelColor = -1;
    private float labelSize = 12;

    public RoundBorderRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

//		String tagColorString = getTag().toString();
//		solidColor = Color.parseColor(tagColorString);
//
//		ColorDrawable background = (ColorDrawable) getBackground();
//		bgColor = background.getColor();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableRadioButton);
        bgColor = typedArray.getColor(R.styleable.DrawableRadioButton_backgroundColor, Color.TRANSPARENT);
        solidColor = typedArray.getColor(R.styleable.DrawableRadioButton_solidColor, Color.TRANSPARENT);
        checkedColor = typedArray.getColor(R.styleable.DrawableRadioButton_checkedColor, 0);

        labelText = getText().toString();
        labelSize = getTextSize();
        labelColor = getTextColors().getDefaultColor();

        typedArray.recycle();
    }



    public void setTagColor(int color){
        solidColor = color;
        invalidate();
    }

    public void setCheckedColor(int color){
        checkedColor = color;
        invalidate();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        bgColor = color;
        invalidate();
    }

    @Override
    public void setTextColor(int color) {
        labelColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        //清除之前的内容
        //canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

        //暂时无法使Canvas透明，只有采取对整个图层绘色的方式，使之与外层的背景融为一体
        canvas.drawColor(bgColor);

        //获取中心的x坐标
        int centre = getWidth()/2;

        //外圆环的半径
        int outsideRadius = centre -5;

        // 是否选中，在外围画一个选中效果的圆环(空心圆环)
        if(isChecked()){
            paint.setColor(checkedColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(circleWidth);
            paint.setAntiAlias(true);
            canvas.drawCircle(centre, centre, outsideRadius, paint);
        }

        //画里面的实心圆
        int inSideRadius = outsideRadius - gap;
        paint.setColor(solidColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(centre, centre, inSideRadius, paint);

        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        if(TextUtils.isEmpty(labelText)){
            paint.setStrokeWidth(0);
            paint.setColor(labelColor);
            paint.setTextSize(labelSize);
            float textWidth = paint.measureText(labelText);
            canvas.drawText(labelText , centre - textWidth/2, centre + labelSize/3, paint);

//            //如果中间有文字的，加个圈
//            paint.reset();
//            paint.setColor(labelColor);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(circleWidth);
//            paint.setAntiAlias(true);
//            canvas.drawCircle(centre, centre, inSideRadius, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, widthSize);
    }


}
