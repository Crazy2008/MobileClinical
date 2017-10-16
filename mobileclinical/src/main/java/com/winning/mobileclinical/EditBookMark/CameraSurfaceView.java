package com.winning.mobileclinical.EditBookMark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.OurApplication;
import com.winning.mobileclinical.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 显示图片并且画图的View
 *
 * @author yyw
 */
public class CameraSurfaceView extends SurfaceView implements Runnable {
    private static final String TAG = "CameraSurfaceView";
    public static final int DRAW_NOTHING = 0x00;
    public static final int DRAW_PATH = 0X01;
    public static final int DRAW_WORD = 0x02;
    public static final int DRAW_RECT = 0x03;
    public static final int DRAW_CIRCLE = 0x04;
    public static final int DRAW_OVAL = 0x05;
    public static final int DRAW_ARROW = 0X06;
    public static final float MAX_SCALE = 4;// 最大的放缩比例
    public static final float MIN_SCALE = 1;// 最小的放缩比例
    private int currentDraw = DRAW_NOTHING;
    private Paint paint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ArrayList<Word> words = new ArrayList<Word>();
    private ArrayList<CirclePath> circlePaths = new ArrayList<CirclePath>();
    private ArrayList<RectPath> rectPaths = new ArrayList<RectPath>();
    private ArrayList<OvalPath> ovalPaths = new ArrayList<OvalPath>();
    private ArrayList<ArrowPath> arrowPaths = new ArrayList<ArrowPath>();
    private ArrayList<LinePath> linePaths = new ArrayList<LinePath>();
    private boolean canDraw;
    private PopupWindow popupWindow;
    private LinePath currentLinPath;
    private CirclePath currentCirclePath;
    private RectPath currentRectPath;
    private OvalPath currentOvalPath;
    private ArrowPath currentArrowPath;
    private Path mPath;
    private Matrix matrix = new Matrix();
    private GestureDetector detector;
   


    private List list=new ArrayList();
    private Context context;

    float mx = 0,my=0;

    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setStyle(Style.STROKE);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        detector = new GestureDetector(listener);
    }

    private SimpleOnGestureListener listener = new SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float[] value = new float[9];
            matrix.getValues(value);
            float maxTranX = mBitmap.getWidth() * value[Matrix.MSCALE_X] - getWidth();
            float maxTranY = mBitmap.getHeight() * value[Matrix.MSCALE_X] - getHeight();
            if (value[Matrix.MSCALE_X] >= MIN_SCALE) {
                float dx = -distanceX + value[Matrix.MTRANS_X];//计算出实际会偏移的量
                float dy = -distanceY + value[Matrix.MTRANS_Y];
                if (dx > 0) {//如果实际量超出边框就就把相对偏移量为零
                    dx = 0;
                } else if (-dx > maxTranX) {
                    dx = 0;
                } else {
                    dx = -distanceX;
                }
                if (dy > 0) {
                    dy = 0;
                } else if (-dy > maxTranY) {
                    dy = 0;
                } else {
                    dy = -distanceY;
                }
                matrix.postTranslate(dx, dy);
                Log.i(TAG, "onScroll" + dx + ":" + dy + "mBitmap.getHeight()" + mBitmap.getHeight() + "value[Matrix.MSCALE_X]" + value[Matrix.MSCALE_X]);
                post(CameraSurfaceView.this);
                return true;
            }
            return false;
        }

        public boolean onDoubleTap(MotionEvent e) {
            Log.i(TAG, "onDoubleTap");
            float[] value = new float[9];
            matrix.getValues(value);
            if (value[Matrix.MSCALE_X] > MIN_SCALE) {
                matrix.reset();
            } else {
                float px = e.getX();
                float py = e.getY();
                float sx = MAX_SCALE / value[Matrix.MSCALE_X];
                float sy = MAX_SCALE / value[Matrix.MSCALE_Y];
                matrix.postScale(sx, sy, px, py);
            }
            post(CameraSurfaceView.this);
            return true;
        }
    };
    private float mOldDist = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canDraw) {
            switch (currentDraw) {
                case DRAW_PATH:
                    lineEvevn(event);
                    break;
                case DRAW_WORD:
                    wordEven(event);
                    break;
                case DRAW_CIRCLE:
                    circleEvent(event);
                    break;
                case DRAW_RECT:
                    rectEvent(event);
                    break;
                case DRAW_ARROW:
                    arrowEvent(event);
                    break;
                case DRAW_OVAL:
                    ovalEvent(event);
                    break;
                case DRAW_NOTHING:
//				if (!nothingEvent(event)) {
//					detector.onTouchEvent(event);
//				}
                    break;
                default:
                    break;
            }
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 缩放处理
     *
     * @param event
     * @return
     */
    private boolean nothingEvent(MotionEvent event) {
        int count = event.getPointerCount();
        if (count > 1) {
            int action = event.getAction();
            action = action & MotionEvent.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.i(TAG, "ACTION_DOWN");
                    mOldDist = getDistOfTowPoints(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "ACTION_MOVE");
                    float mNewDist = getDistOfTowPoints(event);
                    if (Math.abs(mNewDist - mOldDist) > 50) {
                        float[] value = new float[9];
                        matrix.getValues(value);
                        Log.i(TAG, "偏移量" + value[Matrix.MTRANS_X] + ":" + value[Matrix.MTRANS_Y]);
                        float scale = value[Matrix.MSCALE_X];//原来的放缩量
                        float px = (event.getX(0) + event.getX(1)) / 2;
                        float py = (event.getY(0) + event.getY(1)) / 2;
                        if (mOldDist > mNewDist) {
                            scale -= Math.abs(mNewDist - mOldDist) / 500f;//计算现在的放缩量
                            // Log.i(TAG, "缩小" + scale + ":" + Math.abs(mNewDist -
                            // mOldDist));
                        } else {
                            scale += Math.abs(mNewDist - mOldDist) / 500f;//计算现在的放缩量
                            // Log.i(TAG, "放大" + scale);
                        }
                        if (scale < MIN_SCALE) {//如果放缩量小于最低的就置为最低放缩比
                            scale = MIN_SCALE;

                        } else if (scale > MAX_SCALE) {//如果放缩量大于最高的就置为最高放缩比
                            scale = MAX_SCALE;
                        }
                        if (scale == MIN_SCALE) {//如果放缩量为最小就把矩阵重置
                            matrix.reset();
                        } else {
                            scale = scale / value[Matrix.MSCALE_X];//计算出相对的放缩量，使矩阵的放缩量为放缩到计算出来的放缩量。
                            matrix.postScale(scale, scale, px, py);
                        }
                        Log.i(TAG, "" + scale / value[Matrix.MSCALE_X] + ":" + Math.abs(mNewDist - mOldDist));
                        mOldDist = mNewDist;
                        post(this);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取两点之间的距离
     */
    private float getDistOfTowPoints(MotionEvent event) {
        float x0 = event.getX(0);
        float y0 = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);
        float lengthX = Math.abs(x0 - x1);
        float lengthY = Math.abs(y0 - y1);
        return (float) Math.sqrt(lengthX * lengthX + lengthY * lengthY);
    }

    /**
     * 当画椭圆时
     *
     * @param event
     */
    private void ovalEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Paint cPaint = new Paint(paint);
                RectF rectF = new RectF(event.getX(), event.getY(), event.getX() + 1, event.getY() + 1);
                currentOvalPath = new OvalPath(rectF, cPaint);

                list.add(DRAW_OVAL);
                break;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
                currentOvalPath.getRectF().right = mX;
                currentOvalPath.getRectF().bottom = mY;
                break;
            case MotionEvent.ACTION_UP:
                float uX = event.getX();
                float uY = event.getY();
                currentOvalPath.getRectF().right = uX;
                currentOvalPath.getRectF().bottom = uY;
                currentOvalPath.reset(matrix);
                ovalPaths.add(currentOvalPath);
                currentOvalPath = null;
                break;
            default:
                break;
        }
        post(this);

    }

    /**
     * 当画箭头
     *
     * @param event
     */
    private int dX = -1;
    private  int dY = -1;
    private  int mX = -1;
    private int mY = -1;
    private void arrowEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Paint cPaint = new Paint(paint);
                dX = (int) event.getX();
                dY = (int) event.getY();
                currentArrowPath = new ArrowPath(dX, dY, dX + 1, dY + 1, cPaint);
                LogUtils.showLog("ACTION_DOWN==X=" + event.getX() + "--------------Y=" + event.getY());
                list.add(DRAW_ARROW);

                break;
            case MotionEvent.ACTION_MOVE:
                mX = (int) event.getX();
                mY = (int) event.getY();
                LogUtils.showLog("ACTION_MOVE==X=" + event.getX() + "--------------Y=" + event.getY());
                currentArrowPath.seteX(mX);
                currentArrowPath.seteY(mY);
                break;
            case MotionEvent.ACTION_UP:
                int uX = (int) event.getX();
                int uY = (int) event.getY();
                LogUtils.showLog("ACTION_Final==dX=" + dX + "--------------mX=" +mX+"--------------uX="+uX);
                LogUtils.showLog("ACTION_Final==dX=" + dY + "--------------mY=" +mY+"--------------uY="+uY);
                currentArrowPath.seteX(uX);
                currentArrowPath.seteY(uY);
                LogUtils.showLog("ACTION_UP==X=" + event.getX() + "--------------Y=" + event.getY());
                currentArrowPath.reset(matrix);
                arrowPaths.add(currentArrowPath);
                currentArrowPath = null;
                break;
            default:
                break;
        }
        post(this);
    }

    /**
     * 画方
     *
     * @param event
     */
    private void rectEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentRectPath = new RectPath();
                Paint cPaint = new Paint(paint);
                currentRectPath.setPaint(cPaint);
                currentRectPath.setLeft(event.getX());
                currentRectPath.setTop(event.getY());
                currentRectPath.setRight(event.getX() + 1);
                currentRectPath.setBottom(event.getY() + 1);
                list.add(DRAW_RECT);

                break;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
                currentRectPath.setRight(mX);
                currentRectPath.setBottom(mY);
                break;
            case MotionEvent.ACTION_UP:
                float uX = event.getX();
                float uY = event.getY();
                currentRectPath.setRight(uX);
                currentRectPath.setBottom(uY);
                currentRectPath.reset(matrix);
                rectPaths.add(currentRectPath);
                currentRectPath = null;
                break;
            default:
                break;
        }
        post(this);
    }

    /**
     * 画圆
     *
     * @param event
     */
    private void circleEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentCirclePath = new CirclePath();
                currentCirclePath.setCx(event.getX());
                currentCirclePath.setCy(event.getY());
                currentCirclePath.setRadius(1);
                Paint cPaint = new Paint(paint);
                currentCirclePath.setPaint(cPaint);
                break;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
                float r = (float) Math.sqrt(Math.pow(Math.abs(mX - currentCirclePath.getCx()), 2) + Math.pow(Math.abs(mY - currentCirclePath.getCy()), 2));
                currentCirclePath.setRadius(r);
                break;
            case MotionEvent.ACTION_UP:
                float uX = event.getX();
                float uY = event.getY();
                float ur = (float) Math.sqrt(Math.pow(Math.abs(uX - currentCirclePath.getCx()), 2) + Math.pow(Math.abs(uY - currentCirclePath.getCy()), 2));
                currentCirclePath.setRadius(ur);
                currentCirclePath.reset(matrix);
                circlePaths.add(currentCirclePath);
                currentCirclePath = null;
                break;
            default:
                break;
        }
        post(this);
    }

    /**
     * 写字
     *
     * @param event
     */
    private void wordEven(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "DRAW_WORD+ACTION_DOWN");
                mx = event.getX();
                my = event.getY();
                showPop();
                LogUtils.showLog("word_size-----x="+mx);
                LogUtils.showLog("word_size-----y="+my);
                break;
            default:
                break;
        }
    }

    /**
     * 划线
     *
     * @param event
     */
    private void lineEvevn(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                currentLinPath = new LinePath(new Path(), new Paint(paint));
                currentLinPath.getPath().moveTo(event.getX(), event.getY());
                pathTo(event);
                list.add(DRAW_PATH);

                break;
            case MotionEvent.ACTION_MOVE:
                currentLinPath.getPath().lineTo(event.getX(), event.getY());
                pathTo(event);
                break;
            case MotionEvent.ACTION_UP:
                pathTo(event);
                currentLinPath.setPath(mPath);
                linePaths.add(currentLinPath);
                currentLinPath = null;
                mPath = null;
                break;
            default:
                break;
        }
        post(this);
    }

    private void pathTo(MotionEvent event) {
        Point pointD = new Point((int) event.getX(), (int) event.getY());
        calculationRealPoint(pointD, matrix);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPath.moveTo(pointD.x, pointD.y);
        } else {
            mPath.lineTo(pointD.x, pointD.y);
        }
    }

    public void calculationRealPoint(Point point, Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        int sX = point.x;
        int sY = point.y;
        point.x = (int) ((sX - values[Matrix.MTRANS_X]) / values[Matrix.MSCALE_X]);
        point.y = (int) ((sY - values[Matrix.MTRANS_Y]) / values[Matrix.MSCALE_Y]);
    }

	/**
	 * 显示popupWindow输入文字
	 *
	 */
	private void showPop() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		View contentView = View.inflate(getContext(), R.layout.popupwindow, null);
		final EditText etWord = (EditText) contentView.findViewById(R.id.et_word);

        etWord.setInputType(InputType.TYPE_CLASS_TEXT);
        Button btnOk = (Button) contentView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = etWord.getText().toString().trim();
                if (!TextUtils.isEmpty(word)) {
                    Paint key = new Paint(paint);
                    key.setStrokeWidth(1);
                    key.setTypeface(Typeface.DEFAULT);
                    key.setStyle(Style.FILL);
                    Word wor = new Word(mx, my, key, word);
                    wor.reset(matrix);
                    words.add(wor);
                    list.add(DRAW_WORD);
                    post(CameraSurfaceView.this);
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        etWord.requestFocus();
        InputMethodManager imm = (InputMethodManager) etWord.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        // PopupWindow没有背景色，必须设置背景色
//		popupWindow.showAtLocation(this, Gravity.CENTER, (int) x + getLeft(), (int) y + getTop());
        int marginLeft = ViewUtil.diptopx(OurApplication.getContext(), 125);
//        int marginTop = ViewUtil.diptopx(OurApplication.getContext(), 60);

        popupWindow.showAtLocation(this, Gravity.TOP | Gravity.LEFT, (int) mx + marginLeft, (int) my+60);
        etWord.setOnTouchListener(new OnTouchListener() {
            int orgX, orgY;
            int offsetX, offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        orgX = (int) event.getX();
                        orgY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - orgX;
                        offsetY = (int) event.getRawY() - orgY;
                        popupWindow.update(offsetX, offsetY, -1, -1, true);
                        break;
                    case MotionEvent.ACTION_UP:
                        mx = (int) event.getRawX() - 2*orgX;
                        my = (int) event.getRawY() - 2*orgY;
                }
                return true;
            }
        });
        ScaleAnimation animation = new ScaleAnimation(0.2f, 1.2f, 0.2f, 1.2f, 0.5f, 0.5f);
        animation.setDuration(100);
        // animation.setFillAfter(true);
        contentView.startAnimation(animation);

    }

    @Override
    public void run() {
        if (mBitmap != null) {
            SurfaceHolder surfaceHolder = getHolder();
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            canvas.save();
            canvas.setMatrix(matrix);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
            drawText(canvas, words);
            drawOval(canvas, ovalPaths);
            drawArrow(canvas, arrowPaths);
//            drawCircle(canvas, circlePaths);
            drawRect(canvas, rectPaths);
            drawLine(canvas, linePaths);
            // canvas.save();
            canvas.restore();
            drawCurrent(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void drawLine(Canvas canvas, ArrayList<LinePath> linePaths2) {
        for (LinePath linePath : linePaths2) {
            canvas.drawPath(linePath.getPath(), linePath.getPaint());
        }

    }

    private void drawArrow(Canvas canvas, ArrayList<ArrowPath> arrowPaths2) {
        for (ArrowPath arrowPath : arrowPaths2) {
            drawAL(arrowPath.getsX(), arrowPath.getsY(), arrowPath.geteX(), arrowPath.geteY(), canvas, arrowPath.getPaint());
        }

    }

    private void drawOval(Canvas canvas, ArrayList<OvalPath> ovalPaths2) {
        for (OvalPath ovalPath : ovalPaths2) {
            canvas.drawOval(ovalPath.getRectF(), ovalPath.getPaint());

        }

    }

    private void drawRect(Canvas mCanvas2, ArrayList<RectPath> rectPaths2) {
        for (RectPath rectPath : rectPaths2) {
            mCanvas2.drawRect(rectPath.getLeft(), rectPath.getTop(), rectPath.getRight(), rectPath.getBottom(), rectPath.getPaint());

        }

    }

    private void drawCircle(Canvas mCanvas2, ArrayList<CirclePath> circlePaths2) {
        for (CirclePath circlePath : circlePaths2) {
            mCanvas2.drawCircle(circlePath.getCx(), circlePath.getCy(), circlePath.getRadius(), circlePath.getPaint());

        }

    }


    private void drawText(Canvas mCanvas2, ArrayList<Word> words2) {
        for (Word word : words2) {
            mCanvas2.drawText(word.getWordString(), word.getLeft(), word.getTop(), word.getPaint());

        }

    }

    /**
     * 画当前的图形
     *
     * @param canvas
     */
    private void drawCurrent(Canvas canvas) {
        switch (currentDraw) {
            case DRAW_CIRCLE:
                if (currentCirclePath != null) {
                    canvas.drawCircle(currentCirclePath.getCx(), currentCirclePath.getCy(), currentCirclePath.getRadius(), currentCirclePath.getPaint());
                }
                break;
            case DRAW_RECT:
                if (currentRectPath != null) {
                    canvas.drawRect(currentRectPath.getLeft(), currentRectPath.getTop(), currentRectPath.getRight(), currentRectPath.getBottom(), currentRectPath.getPaint());
                }
                break;
            case DRAW_ARROW:
                if (currentArrowPath != null) {

                    drawAL(currentArrowPath.getsX(), currentArrowPath.getsY(), currentArrowPath.geteX(), currentArrowPath.geteY(), canvas, currentArrowPath.getPaint());
                }
                break;
            case DRAW_OVAL:
                if (currentOvalPath != null) {

                    canvas.drawOval(currentOvalPath.getRectF(), currentOvalPath.getPaint());
                }
            case DRAW_PATH:
                if (currentLinPath != null) {

                    canvas.drawPath(currentLinPath.getPath(), currentLinPath.getPaint());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 画箭头
     *
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     */
    public void drawAL(int sx, int sy, int ex, int ey, Canvas myCanvas, Paint myPaint) {
        double H = 8; // 箭头高度
        double L = 3.5; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        Double X3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        x3 = X3.intValue();
        Double Y3 = ey - arrXY_1[1];
        y3 = Y3.intValue();
        Double X4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        x4 = X4.intValue();
        Double Y4 = ey - arrXY_2[1];
        y4 = Y4.intValue();
        // 画线
        if(sx==ex&&sy==ey){
            return;
        }
        myCanvas.drawLine(sx, sy, ex, ey, myPaint);
        LogUtils.showLog("drawAL--------sx="+sx);
        LogUtils.showLog("drawAL--------sy="+sy);
        LogUtils.showLog("drawAL--------ex="+ex);
        LogUtils.showLog("drawAL--------ey="+ey);
        Path triangle = new Path();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.close();
        myCanvas.drawPath(triangle, myPaint);

    }

    /**
     * 矢量旋转函数，求出与结束定点的x，y距离
     *
     * @param px      x分量
     * @param py      y分量
     * @param ang     旋转角度
     * @param isChLen 是否改变长度
     * @param newLen  新长度
     * @return
     */
    public double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

        double mathstr[] = new double[2];
        // double len = Math.sqrt(px*px + py*py);
        // double a = Math.acos(px/len);
        // double vx = Math.cos(a+ang)*len
        // double vy = Math.sin(a+ang)*len
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

    public int getCurrentDraw() {
        return currentDraw;
    }

    public void setCurrentDraw(int currentDraw) {
        this.currentDraw = currentDraw;
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 返回一个画完的图片
     *
     * @return
     */
    public Bitmap getmBitmap() {
        mCanvas = new Canvas(mBitmap);
        drawText(mCanvas, words);
//        drawCircle(mCanvas, circlePaths);
        drawRect(mCanvas, rectPaths);
        drawArrow(mCanvas, arrowPaths);
        drawOval(mCanvas, ovalPaths);
        drawLine(mCanvas, linePaths);
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
//		 mCanvas = new Canvas(this.mBitmap);
        // reset();
        post(this);
    }

    //
    // public void reset() {
    // if (bitmap != null) {
    // bitmap.recycle();
    // }
    // bitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
    // mBitmap.getWidth());
    // dCanvas = new Canvas(bitmap);
    // }

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    /**
     * 清除对应的类型的最新的一个图形
     */
    public void back() {
        for (int i = 0; i <list.size() ; i++) {
            int currentBack = (int) list.get(list.size()-1);
            switch (currentBack) {
//                case DRAW_CIRCLE:
//                    if (circlePaths.size() - 1 >= 0) {
//                        circlePaths.remove(circlePaths.size() - 1);
//                    }
//                    break;
                case DRAW_RECT:
                    if (rectPaths.size() - 1 >= 0) {
                        rectPaths.remove(rectPaths.size() - 1);
                    }
                    break;
                case DRAW_WORD:
                    if (words.size() - 1 >= 0) {
                        words.remove(words.size() - 1);
                    }
                    break;
                case DRAW_PATH:
                    if (linePaths.size() - 1 >= 0) {
                        linePaths.remove(linePaths.size() - 1);
                    }
                    break;
                case DRAW_ARROW:
                    if (arrowPaths.size() - 1 >= 0) {
                        arrowPaths.remove(arrowPaths.size() - 1);
                    }
                    break;
                case DRAW_OVAL:
                    if (ovalPaths.size() - 1 >= 0) {
                        ovalPaths.remove(ovalPaths.size() - 1);
                    }
                    break;
                default:
                    break;

        }


           /* case DRAW_NOTHING:
                Builder builder = new Builder(getContext());
                builder.setTitle("清除");
                builder.setMessage("是否要清除全部内容？");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        words.clear();
                        ovalPaths.clear();
                        linePaths.clear();
                        arrowPaths.clear();
                        rectPaths.clear();
                        circlePaths.clear();


                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;*/
//            default:
//                break;
            list.remove(list.size()-1);
            break;
        }
        post(this);
    }

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void clearAll() {

        words.clear();
        ovalPaths.clear();
        linePaths.clear();
        arrowPaths.clear();
        rectPaths.clear();
        circlePaths.clear();
    }


    public void setSvStrokeWidth(int strokeWidth) {
        paint.setStrokeWidth(strokeWidth);
    }
        public void clearListBack(){
            list.clear();
        }

}
