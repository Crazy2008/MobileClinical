package com.winning.mobileclinical.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.winning.mobileclinical.EditBookMark.CameraSurfaceView;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.NoteAction;
import com.winning.mobileclinical.adapter.EditBookMarkAdapter;
import com.winning.mobileclinical.dialog.DialogHelper;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.BookmarkInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.ServerUtil;
import com.winning.mobileclinical.utils.ViewUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 病历涂鸦
 */

public class EditBookMarkActivity extends FragmentActivity implements SurfaceHolder.Callback, RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private static final int REQUEST_MEDIA_PROJECTION = 21;

    RadioGroup group;
    private Bitmap save_bitmap;
    RadioGroup colorGroup;
    TextView tvSize;
    SeekBar skWordSize;
    Button back;
    Button save;
    CameraSurfaceView sv;
    private Bitmap bitmap;
    private String photoFile;
    private File files;
    private Button chexiao;
    private ListView lv;
    private int syxh;
    private int yexh;
    private int emrid;
    private String today;
    private PatientInfo patient = null;
    private DoctorInfo doctor;
    private String docid;
    private String imageName;
    private List<BookmarkInfo> emrBookmarkList = new ArrayList<>();
    private Bitmap checkedBitmap;
    private ImageView iv_back;
    private ProgressDialog dialog;
    private String xh;
    private ImageView iv_red;
    private ImageView iv_blue;
    private ImageView iv_pale_green;
    private ImageView iv_small;
    private ImageView iv_middle;
    private ImageView iv_large;

    private EditBookMarkAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    BookmarkInfo info = new BookmarkInfo();
                    emrBookmarkList.add(info);
                    adapter = new EditBookMarkAdapter(emrBookmarkList, EditBookMarkActivity.this);
                    lv.setAdapter(adapter);
                    lv.setSelection(lv.getBottom());
                    adapter.setCheckedPostion(adapter.getCount()-1);
                    break;
                case 2:
                    Toast.makeText(EditBookMarkActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    BookmarkInfo info2 = new BookmarkInfo();
                    emrBookmarkList.add(info2);
                    adapter.setDatas(emrBookmarkList);
                    adapter.setCheckedPostion(emrBookmarkList.size()-1);
                    lv.smoothScrollToPosition(adapter.getCount()-1);
                    adapter.notifyDataSetChanged();
                    if(save_bitmap==null){
                        save_bitmap =Bitmap.createBitmap(GlobalCache.getCache().getOriginalBitmap());
                    }
                    sv.setmBitmap(save_bitmap);
                    save_bitmap=null;
                    sv.clearAll();
                    sv.invalidate();
////                    lv.setSelection(emrBookmarkList.size()-1);
//                    finish();

                    break;
                //当点击左侧条目切换surface中的处理
                case 3:
                    sv.setmBitmap(checkedBitmap);
                    sv.clearAll();
                    sv.invalidate();
                    break;
                case 4:
                    emrBookmarkList.remove(del_postion);
                    adapter.notifyDataSetChanged();
                    break;

            }

        }
    };
    private TextView tv_text_size;
    private LinearLayout ll_set_text;
    private LinearLayout ll_color_tool;
    private int screenHeight;
    private int screenWidth;
    private FrameLayout fl_content;
    private TextView tv_14;
    private TextView tv_15;
    private TextView tv_16;
    private TextView tv_17;
    private TextView tv_18;
    private ImageView iv_fenhong;
    private ImageView iv_green;
    private ImageView iv_gray;
    private ImageView iv_back1;
    private ImageView iv_orange;
    private ImageView iv_light_blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_book_mark);



        // 屏幕宽（像素，如：480px）
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        // 屏幕高（像素，如：
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        if (GlobalCache.getCache().getPatient_selected() != null) {
            patient = GlobalCache.getCache().getPatient_selected();
            doctor = GlobalCache.getCache().getDoctor();
            docid = doctor.getId();
        }
        initView();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        today = format.format(date);

//        bitmap = GlobalCache.getCache().getBitmap();
        imageName = new Date().getTime() + ".jpg";
        syxh = getIntent().getIntExtra("syxh", 0);
        yexh = getIntent().getIntExtra("yexh", 0);
        emrid = getIntent().getIntExtra("emrid", 0);
        final String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"emrid\":\"" + emrid + "\"}";
        new Thread(new Runnable() {
            @Override
            public void run() {
                emrBookmarkList = NoteAction.getEmrBookmark(params);
                handler.sendEmptyMessage(1);
            }
        }).start();


    }

    private void initView() {
        // 顶部标题栏的病人信息


        TextView top_head_name = (TextView) findViewById(R.id.top_head_name);
        top_head_name.setText(patient.getName());
        TextView top_head_sex = (TextView) findViewById(R.id.top_head_sex);
        top_head_sex.setText(patient.getSex());
        TextView top_head_age = (TextView) findViewById(R.id.top_head_age);
        top_head_age.setText(patient.getAge());
        TextView top_head_zyh = (TextView) findViewById(R.id.top_head_zyh);
        top_head_zyh.setText(patient.getCwdm());
        TextView top_head_ryrq = (TextView) findViewById(R.id.top_head_ryrq);
        top_head_ryrq.setText(patient.getRyrq());
        TextView top_head_zd = (TextView) findViewById(R.id.top_head_zd);
        top_head_zd.setText(patient.getZdmc());
        iv_back = (ImageView) findViewById(R.id.iv_back);


        //字体大小设置
        tv_14 = (TextView) findViewById(R.id.tv_14);
        tv_15 = (TextView) findViewById(R.id.tv_15);
        tv_16 = (TextView) findViewById(R.id.tv_16);
        tv_17 = (TextView) findViewById(R.id.tv_17);
        tv_18 = (TextView) findViewById(R.id.tv_18);

//        cancle = (Button) findViewById(R.id.cancle);
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        ll_set_text = (LinearLayout) findViewById(R.id.ll_set_text);
        ll_color_tool = (LinearLayout) findViewById(R.id.ll_color_tool);

        tv_text_size = (TextView) findViewById(R.id.tv_text_size);
        iv_small = (ImageView) findViewById(R.id.iv_small);
        iv_middle = (ImageView) findViewById(R.id.iv_middle);
        iv_large = (ImageView) findViewById(R.id.iv_large);

        iv_fenhong = (ImageView) findViewById(R.id.iv_fenhong);
        iv_green = (ImageView) findViewById(R.id.iv_green);

        iv_gray = (ImageView) findViewById(R.id.iv_gray);
        iv_orange = (ImageView) findViewById(R.id.iv_orange);
        iv_light_blue = (ImageView) findViewById(R.id.iv_light_blue);


        iv_red = (ImageView) findViewById(R.id.iv_red);
        iv_red.setImageResource(R.drawable.yes2);
        iv_pale_green = (ImageView) findViewById(R.id.iv_pale_green);
        iv_blue = (ImageView) findViewById(R.id.iv_blue);


        lv = (ListView) findViewById(R.id.lv);
        group = (RadioGroup) findViewById(R.id.group);
        save = (Button) findViewById(R.id.save);
        skWordSize = (SeekBar) findViewById(R.id.sk_word_size);
        sv = (CameraSurfaceView) findViewById(R.id.sv);
        tvSize = (TextView) findViewById(R.id.tv_size);
        back = (Button) findViewById(R.id.back);
        chexiao = (Button) findViewById(R.id.chexiao);
        tv_text_size.setOnClickListener(this);


        iv_back.setOnClickListener(this);
        lv.setOnItemLongClickListener(this);

        iv_light_blue.setOnClickListener(this);
        iv_orange.setOnClickListener(this);
        iv_fenhong.setOnClickListener(this);
        iv_green.setOnClickListener(this);
        iv_gray.setOnClickListener(this);
        iv_small.setOnClickListener(this);
        iv_middle.setOnClickListener(this);
        iv_large.setOnClickListener(this);
        iv_red.setOnClickListener(this);
        iv_pale_green.setOnClickListener(this);
        iv_blue.setOnClickListener(this);

        tv_14.setOnClickListener(this);
        tv_15.setOnClickListener(this);
        tv_16.setOnClickListener(this);
        tv_17.setOnClickListener(this);
        tv_18.setOnClickListener(this);


        chexiao.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        sv.getHolder().addCallback(this);
        group.setOnCheckedChangeListener(this);
        skWordSize.setMax(46);
        skWordSize.setProgress(16);
        skWordSize.setOnSeekBarChangeListener(this);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                adapter.setCheckedPostion(position);
                if (position == emrBookmarkList.size() - 1) {
                    xh = "0";


                    checkedBitmap = Bitmap.createBitmap(GlobalCache.getCache().getOriginalBitmap());
                    sv.setmBitmap(checkedBitmap);
                    sv.clearAll();
                    sv.invalidate();
                } else {
                    BookmarkInfo bookmarkInfo = emrBookmarkList.get(position);
                    xh = bookmarkInfo.getId() + "";
                    final BigDecimal syxh = bookmarkInfo.getSyxh();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            checkedBitmap = Bitmap.createBitmap(ViewUtil.getBitmapForNet(GlobalCache.getCache().getEditBookMarkDownloadUrl() + "&xh=" + xh + "&syxh=" + syxh));
//                            sv.invalidate();
                            Message msg = new Message();
                            msg.what = 3;
                            handler.sendMessage(msg);

                        }
                    }).start();
                }


            }
        });

//        jietu= (Button) findViewById(R.id.jietu);
    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    public Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int statusBarHeights = rect.top;
        int widths = display.getWidth();
        int heights = display.getHeight();
        System.out.println("widths===" + widths);
        System.out.println("heights===" + heights);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, widths, heights - statusBarHeights);
        view.buildDrawingCache();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap drawingCache = view.getDrawingCache();
        int width1 = drawingCache.getWidth();
        int height1 = drawingCache.getHeight();
        Bitmap bmp = Bitmap.createBitmap(drawingCache, 0, statusBarHeights, widths, heights - statusBarHeights);
//         销毁缓存信息
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        return bmp;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //设置字体大小的工具条显示
            case R.id.tv_text_size:
                if (ll_set_text.getVisibility() == View.GONE) {
                    ll_set_text.setVisibility(View.VISIBLE);
                    String text = tv_text_size.getText().toString();
                    if(text.equals("14磅")){
                        setBlueOrBlackColor(tv_14);
                    }else if(text.equals("15磅")){
                        setBlueOrBlackColor(tv_15);
                    }else if(text.equals("16磅")){
                        setBlueOrBlackColor(tv_16);
                    }else if(text.equals("17磅")){
                        setBlueOrBlackColor(tv_17);
                    }else if(text.equals("18磅")){
                        setBlueOrBlackColor(tv_18);
                    }
                } else {
                    ll_set_text.setVisibility(View.GONE);
                }
                break;
            //返回按钮
            case R.id.iv_back:
                finish();
                break;

            //返回的按钮
            case R.id.back:
                finish();
                break;
            case R.id.save:
                bitmap = sv.getmBitmap();
                sv.clearListBack();
                upLoadBookMark(xh);
                break;
            //撤销
            case R.id.chexiao:
                sv.back();
                break;
            //颜色选择red
            case R.id.iv_orange:
                setCheckedImage(iv_orange);
                sv.setColor(Color.parseColor("#FF860B"));
                break;
            case R.id.iv_light_blue:
                setCheckedImage(iv_light_blue);
                sv.setColor(Color.parseColor("#4FC3F7"));
                break;

            case R.id.iv_red:
               setCheckedImage(iv_red);
                sv.setColor(Color.RED);
                break;
            case R.id.iv_blue:
               setCheckedImage(iv_blue);
                sv.setColor(Color.parseColor("#0063D1"));
                break;
            case R.id.iv_pale_green:
                    setCheckedImage(iv_pale_green);
                sv.setColor(Color.parseColor("#38ED25"));
                break;
            case R.id.iv_fenhong:
                setCheckedImage(iv_fenhong);
                sv.setColor(Color.parseColor("#FF6CE1"));
                break;
            case R.id.iv_green:
               setCheckedImage(iv_green);
                sv.setColor(Color.parseColor("#32A626"));
                break;
            case R.id.iv_gray:
              setCheckedImage(iv_gray);
                sv.setColor(Color.parseColor("#8B8B8B"));
                break;


            case R.id.iv_small:
                iv_small.setImageResource(R.drawable.circle_small_pressed);
                iv_middle.setImageResource(R.drawable.circle_middle);
                iv_large.setImageResource(R.drawable.circle_large);
                sv.setSvStrokeWidth(4);

                break;
            case R.id.iv_middle:
                iv_middle.setImageResource(R.drawable.circle_middle_pressed);
                iv_small.setImageResource(R.drawable.circle_small);
                iv_large.setImageResource(R.drawable.circle_large);
                sv.setSvStrokeWidth(6);
                break;
            case R.id.iv_large:
                iv_large.setImageResource(R.drawable.circle_large_pressed);
                iv_small.setImageResource(R.drawable.circle_small);
                iv_middle.setImageResource(R.drawable.circle_middle);
                sv.setSvStrokeWidth(8);
                break;
            case R.id.tv_14:

                tv_text_size.setText("14磅");
                ll_set_text.setVisibility(View.GONE);
                sv.setTextSize(30);
                break;
            case R.id.tv_15:

                tv_text_size.setText("15磅");
                ll_set_text.setVisibility(View.GONE);
                sv.setTextSize(34);
                break;
            case R.id.tv_16:

                tv_text_size.setText("16磅");
                ll_set_text.setVisibility(View.GONE);
                sv.setTextSize(38);
                break;
            case R.id.tv_17:

                tv_text_size.setText("17磅");
                ll_set_text.setVisibility(View.GONE);
                sv.setTextSize(42);

                break;
            case R.id.tv_18:
                tv_text_size.setText("18磅");
                ll_set_text.setVisibility(View.GONE);
                sv.setTextSize(46);
                break;


            default:
                sv.setColor(Color.BLACK);
                break;

        }
    }
        //设置选中的颜色
    private void setCheckedImage(ImageView checkedView) {
        iv_red.setImageBitmap(null);
        iv_orange.setImageBitmap(null);
        iv_fenhong.setImageBitmap(null);
        iv_blue.setImageBitmap(null);
        iv_green.setImageBitmap(null);
        iv_pale_green.setImageBitmap(null);
        iv_gray.setImageBitmap(null);
        iv_light_blue.setImageBitmap(null);
        checkedView.setImageResource(R.drawable.yes2);
    }


    private void setBlueOrBlackColor(TextView tv) {
        tv_14.setTextColor(Color.BLACK);
        tv_15.setTextColor(Color.BLACK);
        tv_16.setTextColor(Color.BLACK);
        tv_17.setTextColor(Color.BLACK);
        tv_18.setTextColor(Color.BLACK);
        tv.setTextColor(Color.BLUE);

    }

    //上传涂鸦图片到服务器
    private void upLoadBookMark(String xh) {
        if (TextUtils.isEmpty(xh)) {
            xh = "0";
        }
        dialog = ProgressDialog.show(EditBookMarkActivity.this, "", "正在上传，请稍后。。。", true, true);
//        BookmarkInfo bookmarkInfo=new BookmarkInfo(new BigDecimal("1"),new BigDecimal("1"),new BigDecimal("1"),"3","ysdm","d","");
//        String str = new Gson().toJson(bookmarkInfo);
        final String finalXh = xh;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerUtil.uploadTuYatoServer(GlobalCache.getCache().getEditBookMarkUrl()+ "&xh=" + finalXh + "&syxh=" + syxh + "&yexh=" + yexh + "&emrid=" + emrid + "&ysdm=" + docid + "&cjrq=" + today + "", imageName, bitmap);

//
                final String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"emrid\":\"" + emrid + "\"}";
                emrBookmarkList.clear();
                emrBookmarkList = NoteAction.getEmrBookmark(params);
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sv.setCanDraw(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

//        boolean b = bitmap.isRecycled();
        sv.setmBitmap(GlobalCache.getCache().getBitmap());
        sv.setCanDraw(true);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


    }

    public Bitmap getZoomBmpByDecodePath(String path, int w, int h) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, w, h);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    private void changeOtherColor(int color) {
        tvSize.setTextColor(color);
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i) instanceof TextView) {
                TextView tvChild = (TextView) group.getChildAt(i);
                tvChild.setTextColor(color);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.equals(this.group)) {
            selectPath(checkedId);
        }

    }


    /**
     * 选择图形
     *
     * @param checkedId
     */
    private void selectPath(int checkedId) {
        iv_small.setVisibility(View.VISIBLE);
        iv_middle.setVisibility(View.VISIBLE);
        iv_large.setVisibility(View.VISIBLE);
        tv_text_size.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.word:
                sv.setCurrentDraw(CameraSurfaceView.DRAW_WORD);
                iv_small.setVisibility(View.GONE);
                iv_middle.setVisibility(View.GONE);
                iv_large.setVisibility(View.GONE);
                tv_text_size.setVisibility(View.VISIBLE);

                break;
            case R.id.line:
                sv.setCurrentDraw(CameraSurfaceView.DRAW_PATH);
                break;
            case R.id.rect:
                sv.setCurrentDraw(CameraSurfaceView.DRAW_RECT);
                break;

            case R.id.arrow:
                sv.setCurrentDraw(CameraSurfaceView.DRAW_ARROW);
                break;
            case R.id.oval:
                sv.setCurrentDraw(CameraSurfaceView.DRAW_OVAL);
                break;
//            case R.id.nothing:
//                sv.setCurrentDraw(CameraSurfaceView.DRAW_NOTHING);
//                break;
//            case R.id.chexiao:
//                sv.back();
//                break;

            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {// 选择字体大小
            sv.setTextSize(progress + 8);
            tvSize.setText(String.valueOf(progress + 8));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 把图片保存到sd卡
     *
     * @param bitmap
     */
    private void savaToSd(Bitmap bitmap) {
        if (files == null) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(files);
            fos.write(baos.toByteArray());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int del_postion;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        del_postion = position;
        BookmarkInfo bookmarkInfo = emrBookmarkList.get(position);
        BigDecimal syxh = bookmarkInfo.getSyxh();
        BigDecimal yexh = bookmarkInfo.getYexh();
        BigDecimal bookId = bookmarkInfo.getId();
        final String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"id\":\"" + bookId + "\"}";
        DialogHelper.showAlert(this, "提醒", "确定删除该图片？", R.drawable.fill4,
                new DialogHelper.dialogListener() {
                    @Override
                    public void onOk(Dialog dialog) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean flag = NoteAction.deleteBookMark(params);
                                if (flag) {
                                    handler.sendEmptyMessage(4);
                                }
                            }
                        }).start();
                        dialog.dismiss();
                    }

                    @Override
                    public void oncancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
        return false;
    }


}
