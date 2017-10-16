package com.winning.mobileclinical.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.winning.mobileclinical.db.dao.NoteDao;
import com.google.gson.Gson;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.NoteAction;
import com.winning.mobileclinical.activity.RecordActivity;
import com.winning.mobileclinical.activity.RecordPlayerActivity;
import com.winning.mobileclinical.activity.ShowPhotoActivity;
import com.winning.mobileclinical.adapter.CFJLAdatpter;
import com.winning.mobileclinical.adapter.HorizontalListViewAdapter;
import com.winning.mobileclinical.adapter.New_RecordAdapter;
import com.winning.mobileclinical.adapter.PictureAdapter;
import com.winning.mobileclinical.db.dao.NoteDao;
import com.winning.mobileclinical.dialog.DialogHelper;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.layout.HorizontalListView;
import com.winning.mobileclinical.layout.HorizontalListView2;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.MediaList;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.BitmapUtils;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.OurApplication;
import com.winning.mobileclinical.utils.ServerUtil;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.winning.mobileclinical.R.id.date_recordlistview;


/**
 * A simple {@link Fragment} subclass.
 */
public class CFJLFragment extends FragmentChild implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    private static final int DEL_MEDIA = 4;
    private static final int RECORD_REQUEST_CODE = 20;
    private static final int RECORD_PLAYER_REQUEST_CODE = 30;

    //记录hlv的条目点击角标
    private int selectIndex;
    //是否是当日日期的标记，默认不是

    private static final int LOAD_NOTE = 3;
    private int LOADMODE = LOADLIST;
    private static final int LOADLISTMX = 2;
    private static final int LOADLIST = 1;


    private int dailyChecked = 0;               //记录Checked查房日期
    private List<DrInfo> cfjls = new ArrayList<>();
    private int del_xh = 0;
    private Bitmap mbitmap;
    private String today_CFXH = "";//今日查房序号
    private List<MediaList> record_cfjlmxs = null;
    private PatientInfo patient = null;            //病人信息
    private DoctorInfo doctor = null;
    private List<MediaList> cfjlmxs = null;
    private HorizontalListView hlv;
    private Boolean hasSdCard;
    private List<Bitmap> bitmaps = null;
    private Thread noteServerThread;
    private Thread noteServerLocalTread;


    private int backXH;

    private CFJLAdatpter cfjlAdatpter;
    private EditText bwl_content;
    private HorizontalListView2 pictureListView;
    private HorizontalListView2 recordListView;
    private ImageView btn_add_camera;
    private ImageView btn_add_audio;
    private File file;
    private String picturePath;
    private PictureAdapter pictureAdapter;
    private int mediaIndex = 1;
    private String photoFileName;
    private List<MediaList> medialist;
    private List<MediaList> medialist_pic;
    private List<MediaList> medialist_audio;
    private String recordPath;
    private MediaRecorder recorder;
    private String recordFileName;
    private New_RecordAdapter new_recordAdapter;


    /*播放录音*/
    private MediaPlayer player;
    private List<MediaList> list;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            proDialog.dismiss();
            int flag = msg.arg1;
            if (flag == 1) {
                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_LONG)
                        .show();
            }
            if (flag == 2) { // 保存成功或更新成功
                //保存成功后删除本地文件
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG)
                        .show();



            }
            switch (msg.what) {
                case 3:
                    break;
                case 2:
                    pictureAdapter.notifyDataSetChanged();
                    break;
            }


        }
    };
    private String date;
    private String today_Text;
    private HorizontalListViewAdapter hListViewAdapter;
    private Button btn_reset;
    private String today;
    private Thread thread;


    private DrInfo cfjl;
    private Button btn_save;
    private DrInfo drinfo;
    private String docid;
    private int mediaId;
    private LinearLayout bottom_head_left;
    private LinearLayout bottom_head_right;
    private List<MediaList> mediaListResultList;
    private String mediaUrl;


    public CFJLFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        panDuanIsToday();
        View view = inflater.inflate(R.layout.fragment_cfjl, container, false);
        hlv = (HorizontalListView) view.findViewById(R.id.hlv);


        if (GlobalCache.getCache().getPatient_selected() != null) {
            patient = GlobalCache.getCache().getPatient_selected();
            doctor = GlobalCache.getCache().getDoctor();
            docid = doctor.getId();
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        today = format.format(date);

        final int[] pic_ids = {};
        bitmaps = new ArrayList<Bitmap>();
        for (int i = 0; i < pic_ids.length; i++) {
            bitmaps.add(BitmapUtils.getDrawableThumnail(OurApplication.getContext(), pic_ids[i], "add"));
        }
        //判断是否有sdcard
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            hasSdCard = true;
        }
        initView(view);
        initClassList();
        initListener();
        pictureAdapter = new PictureAdapter(getActivity(), bitmaps);
        pictureListView.setAdapter(pictureAdapter);

        //初始化控件

        new_recordAdapter = new New_RecordAdapter(medialist_audio);
        recordListView.setAdapter(new_recordAdapter);
        //初始化监听事件
        //文本
        LOADMODE = LOAD_NOTE;
        loadDataWithNothing();


        return view;
    }

    @Override
    protected void loadDate() {

        if (!SystemUtil.isConnect(getActivity())) {

            if (LOADMODE == LOAD_NOTE) {
//				cfjls = noteDao.getNote(context, bqid, czryid, syxh, yexh);
                cfjls = NoteDao.getNote_cfjl(getActivity(), "", doctor.getId(), patient.getSyxh() + "", patient.getYexh() + "");
//                int i = 0;

            } else if (LOADMODE == LOADLISTMX) {
//                List<MediaList> list = noteDao.getMediaList_cfjl(getActivity(), cfjl.getXh(), "", "", today_CFXH);
                LogUtils.showLog("xhtoday_CFXH+XH=" + cfjl.getXh());
                LogUtils.showLog("backXH+XH=" + backXH);
                LogUtils.showLog("today_CFXH=" + today_CFXH);
                List<MediaList> list = NoteDao.getMediaList_cfjl(getActivity(), cfjl.getXh() + "", "", "", today_CFXH);
                cfjlmxs.addAll(list);
            } else if (LOADMODE == DEL_MEDIA) {
                NoteDao.delete_Cfjl_Media(getActivity(), mediaUrl);
            }


        } else {
            if (LOADMODE == LOAD_NOTE) {
//				cfjls = noteDao.getNote(context, bqid, czryid, syxh, yexh);
                String syxh = "" + patient.getSyxh();
                String yexh = "" + patient.getYexh();
                String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"ysdm\":\"" + docid + "\"}";
//				System.out.println("params========="+params);
                cfjls = NoteAction.getDoctorDailyRecordList(getActivity(), params);
            } else if (LOADMODE == LOADLISTMX) {
               
                if(cfjl.getXh()==null){
                    cfjl.setXh("0");
                }
                List<MediaList> list = NoteAction.getMediaInfo(getActivity(), "{\"cfxh\":" + cfjl.getXh() + "}");
                LogUtils.showLog("cfjl.getXh()=" + cfjl.getXh());
                for (int i=0;i<list.size();i++){
                    list.get(i).setUrl(GlobalCache.getCache().getCFJLDownloadUrl()+"&file="+list.get(i).getXh());
                }
                cfjlmxs.addAll(list);
            } else if (LOADMODE == DEL_MEDIA) {
                NoteAction.deleteMedia(getActivity(), "{\"xhs\":" + del_xh + "}");
            }
        }
    }

    @Override
    protected void afterLoadData() {

        if (LOADMODE == LOAD_NOTE) {
            //加载显示服务器数据
            if (cfjls != null) {
                //设置HorizontalListView条目在屏幕中间
                setItemInHorizontalListViewCenter(cfjls);
                if (cfjls.size() > 0) {
                    //判断当天是否有查房记录，有新增时返回CFXH，否则置为0
                    for (int i = 0; i < cfjls.size(); i++) {
                        if (cfjls.get(i).getCfsj().startsWith(today)) {
                            today_CFXH = cfjls.get(i).getXh();
                            today_Text = cfjls.get(i).getMemo();
                            setContentValue(today_Text);

                        }
                    }
//                    hListViewAdapter = new HorizontalListViewAdapter(getActivity(),cfjls);

                    cfjlAdatpter = new CFJLAdatpter(cfjls);
                    hlv.setAdapter(cfjlAdatpter);
                    hlv.setDividerWidth(ViewUtil.pxtodip(getActivity(), 18));
                    hlv.setKeepScreenOn(true);

                    ListAdapter adapter = hlv.getAdapter();
                    int measuredWidth = 0;
                    if (adapter != null) {
                        View view = adapter.getView(0, null, hlv);
                        view.measure(0, 0);
                        measuredWidth = view.getMeasuredWidth();
                        LogUtils.showLog("measuredWidth=======" + measuredWidth);
                    }
                    hlv.scrollTo(measuredWidth * cfjls.size());

                    //判断list中最后的数据是不是今天，不是今天就加上去
                    if (!cfjls.get(cfjls.size() - 1).getCfsj().startsWith(today)) {
                        drinfo.setCfsj(today);
                        cfjls.add(drinfo);

                    }


                    dailyChecked = cfjls.size() - 1;
                    cfjlAdatpter.changeSelected(dailyChecked);
                    cfjl = cfjls.get(cfjls.size() - 1);
                    setContentValue(cfjl.getMemo());
                    LOADMODE = LOADLISTMX;
                    loadDataWithProgressDialog();
                } else {
//                   backXH=0;
                    cfjl = new DrInfo();
                    cfjl.setXh(0 + "");
//                    hListViewAdapter = new HorizontalListViewAdapter(getActivity(),cfjls);
                    //如果获取数据为空就生成当天日期可编辑
                    drinfo.setCfsj(today);
                    cfjls.add(drinfo);


                    cfjlAdatpter = new CFJLAdatpter(cfjls);
                    hlv.setAdapter(cfjlAdatpter);
                    cfjlAdatpter.notifyDataSetChanged();

                }
                //cfjls==null的时候
            } else {

            }
        } else if (LOADMODE == LOADLISTMX) {


            if (cfjlmxs != null) {
                if (cfjlmxs.size() > 0) {
                    for (int i = 0; i < cfjlmxs.size(); i++) {
//						.replace("\\", "").replace("localhost", "192.168.10.57");
                        String url = cfjlmxs.get(i).getUrl();
                        if (cfjlmxs.get(i).getFlag() == 0) {
//                        String url = WebUtils.CFJLDownloadUrl+"&file="+cfjlmxs.get(i).getXh();
                            /*有网络的情况下去服务器取图片*/
                            if (SystemUtil.isConnect(getActivity())) {
                                GetImage getimage = new GetImage(url, cfjlmxs.get(i));
                                new Thread(getimage).start();
                            } else {
                                BitmapFactory.Options opt = new BitmapFactory.Options();
                                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                                opt.inPurgeable = true;
                                opt.inSampleSize = 10;
                                opt.inInputShareable = true;
//                                mbitmap = BitmapFactory.decodeFile(cfjlmxs.get(i).getUrl(), opt);
                                mbitmap = BitmapFactory.decodeFile(ServerUtil.getMkDir(hasSdCard)+"/"+cfjlmxs.get(i).getName(), opt);
                                bitmaps.add(BitmapUtils.getBitmapThumnail(OurApplication.getContext(), mbitmap, "add"));
                                medialist_pic.add(cfjlmxs.get(i));
                                Message msg = new Message();
//                                msg.arg1 = 1;
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }

                        }
                        //录音
                        else if (cfjlmxs.get(i).getFlag() == 1) {
                            record_cfjlmxs.add(cfjlmxs.get(i));

                        }
                    }
                    //添加到录音列表 刷新数据
                    medialist_audio.clear();
                    if (record_cfjlmxs.size() > 0) {
                        for (int i = 0; i < record_cfjlmxs.size(); i++) {
                            medialist_audio.add(record_cfjlmxs.get(i));
                        }
                        new_recordAdapter.setDatas(medialist_audio);
                    }

                } else {

                }
            }
        }
    }

    /**
     * 设置HorizontalListView条目在屏幕中间
     *
     * @param cfjls
     */
    private void setItemInHorizontalListViewCenter(List<DrInfo> cfjls) {
        int size = cfjls.size();
        LinearLayout.LayoutParams lp_left = new LinearLayout.LayoutParams(ViewUtil.pxtodip(CFJLFragment.this.getActivity(), 50), ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lp_right = new LinearLayout.LayoutParams(ViewUtil.pxtodip(CFJLFragment.this.getActivity(), 50), ViewGroup.LayoutParams.MATCH_PARENT);
        if (size == 0) {
            lp_left.setMargins(ViewUtil.diptopx(CFJLFragment.this.getActivity(), 450), 0, 0, 0);
            lp_right.setMargins(0, 0, ViewUtil.diptopx(CFJLFragment.this.getActivity(), 450), 0);
        } else if (size == 1) {
            lp_left.setMargins(ViewUtil.diptopx(CFJLFragment.this.getActivity(), 420), 0, 0, 0);
            lp_right.setMargins(0, 0, ViewUtil.diptopx(CFJLFragment.this.getActivity(), 420), 0);
        } else if (size == 2) {
            lp_left.setMargins(ViewUtil.diptopx(CFJLFragment.this.getActivity(), 350), 0, 0, 0);
            lp_right.setMargins(0, 0, ViewUtil.diptopx(CFJLFragment.this.getActivity(), 350), 0);
        } else if (size > 2 && size <= 4) {
            lp_left.setMargins(ViewUtil.diptopx(CFJLFragment.this.getActivity(), 300), 0, 0, 0);
            lp_right.setMargins(0, 0, ViewUtil.diptopx(CFJLFragment.this.getActivity(), 300), 0);
        } else if (size > 4 && size <= 7) {
            lp_left.setMargins(ViewUtil.diptopx(CFJLFragment.this.getActivity(), 250), 0, 0, 0);
            lp_right.setMargins(0, 0, ViewUtil.diptopx(CFJLFragment.this.getActivity(), 250), 0);

        } else if (size > 7) {
            lp_left.setMargins(ViewUtil.diptopx(CFJLFragment.this.getActivity(), 200), 0, 0, 0);
            lp_right.setMargins(0, 0, ViewUtil.diptopx(CFJLFragment.this.getActivity(), 200), 0);
        }


        bottom_head_left.setLayoutParams(lp_left);
        bottom_head_right.setLayoutParams(lp_right);
    }

    //隐藏一些控件
    private void hideSomeView() {
        btn_save.setVisibility(View.GONE);
        btn_add_audio.setVisibility(View.GONE);
        btn_add_camera.setVisibility(View.GONE);
        btn_reset.setVisibility(View.GONE);
        bwl_content.setEnabled(false);
    }//显示一些控件

    private void showSomeView() {
        btn_save.setVisibility(View.VISIBLE);
        btn_add_audio.setVisibility(View.VISIBLE);
        btn_add_camera.setVisibility(View.VISIBLE);
        btn_reset.setVisibility(View.VISIBLE);
        bwl_content.setEnabled(true);
    }


    //初始化list集合
    private void initClassList() {


        mediaListResultList = new ArrayList<>();

        drinfo = new DrInfo();
        player = new MediaPlayer();

        bitmaps = new ArrayList<>();
        //
        medialist = new ArrayList<>();
        //图片
        medialist_pic = new ArrayList<>();
        //录音
        medialist_audio = new ArrayList<>();
        /*上传媒体数据的集合*/
        cfjlmxs = new ArrayList<>();

        record_cfjlmxs = new ArrayList<>();
    }

    private void initView(View view) {

        bottom_head_left = (LinearLayout) view.findViewById(R.id.bottom_head_left);
        bottom_head_right = (LinearLayout) view.findViewById(R.id.bottom_head_right);
        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        bwl_content = (EditText) view.findViewById(R.id.bwl_content);
        //相片的listView
        pictureListView = (HorizontalListView2) view.findViewById(R.id.add_picturelistview);
        //录音的listview
        recordListView = (HorizontalListView2) view.findViewById(date_recordlistview);
        //点击照相
        btn_add_camera = (ImageView) view.findViewById(R.id.add_camera);
        //点击录音
        btn_add_audio = (ImageView) view.findViewById(R.id.add_audio);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initListener() {

            /*动态上传文本数据*/
        bwl_content.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                    Toast.makeText(getActivity(),"onLayoutChange",Toast.LENGTH_SHORT).show();
                String content = bwl_content.getText().toString().trim();

                if (bwl_content.hasFocus()) {
                    if (!content.equals(today_Text)&& !TextUtils.isEmpty(content)) {
//                            bwl_content.setFocusable(!bwl_content.hasFocusable());
                        /*上传文本数据*/
                        medialist.clear();
                        saveData();
//                        bwl_content.setFocusable(false);
                    }
                }
            }
        });


        btn_save.setOnClickListener(this);
        btn_add_camera.setOnClickListener(this);
        btn_add_audio.setOnClickListener(this);
        //录音的条目点击
        recordListView.setOnItemClickListener(this);
        recordListView.setOnItemLongClickListener(this);
        //图片列表的点击事件
        pictureListView.setOnItemClickListener(this);

        pictureListView.setOnItemLongClickListener(this);

        hlv.setOnItemClickListener(this);
        //条目选中事件
        hlv.setOnItemSelectedListener(this);
    }


    @Override
    public void switchPatient() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //照相功能点击事件
            case R.id.add_camera:
                takePhone();
                break;
            //录音功能点击事件
            case R.id.add_audio:
                takeAudio();
                break;
            /*保存功能*/
            case R.id.btn_save:
                saveData();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            //长按删除图片
            case R.id.add_picturelistview:

                if (bitmaps.size() > 0) {
                    //长按弹窗提醒
                    final int index = position;
                    DialogHelper.showAlert(getActivity(), "提醒", "确定删除该图片？", R.drawable.fill4,
                            new DialogHelper.dialogListener() {
                                @Override
                                public void onOk(Dialog dialog) {
                                    //调用删除服务
                                    if (SystemUtil.isConnect(CFJLFragment.this.getActivity())) {
                                        del_xh = medialist_pic.get(index).getXh();
                                        LOADMODE = DEL_MEDIA;
                                        loadDataWithNothing();

                                    } else {
                                        MediaList mediaList = medialist_pic.get(position);
                                        mediaUrl = mediaList.getUrl();
                                        LOADMODE = DEL_MEDIA;
                                        loadDataWithNothing();
                                    }
                                    bitmaps.remove(index);
                                    pictureAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }

                                @Override
                                public void oncancel(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            }
                    );
                }
                break;
            case R.id.date_recordlistview:
                if (medialist_audio.size() > 0) {
                    //长按弹窗提醒
                    final int index = position;
                    DialogHelper.showAlert(getActivity(), "提醒", "确定删除该音频？", R.drawable.fill4,
                            new DialogHelper.dialogListener() {
                                @Override
                                public void onOk(Dialog dialog) {
                                    if (SystemUtil.isConnect(CFJLFragment.this.getActivity())) {
                                        //调用删除服务
                                        del_xh = medialist_audio.get(index).getXh();
                                        LOADMODE = DEL_MEDIA;
                                        loadDataWithNothing();
                                    } else {
                                        MediaList mediaList = medialist_audio.get(position);
                                        mediaUrl = mediaList.getUrl();
                                        LOADMODE = DEL_MEDIA;
                                        loadDataWithNothing();
                                    }
                                    medialist_audio.remove(index);
                                    new_recordAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                                @Override
                                public void oncancel(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            }
                    );
                }
                break;

        }


        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View vew, int position, long id) {
        System.out.println("parent = [" + parent + "], vew = [" + vew + "], position = [" + position + "], id = [" + id + "]");

        if(!SystemUtil.isConnect(getActivity())){
            switch (parent.getId()) {
            /*录音的listview列表*/
                case R.id.date_recordlistview:
                    MediaList mediaList = medialist_audio.get(position);
                    String record_url =  ServerUtil.getMkDir(hasSdCard)+"/"+mediaList.getName();
                    Intent recordPlayerIntent = new Intent(getActivity(), RecordPlayerActivity.class);
                    recordPlayerIntent.putExtra("record_url", record_url);
                    startActivity(recordPlayerIntent);
                    break;
                //图片列表
                case R.id.add_picturelistview:
                    MediaList mediaList_pic = medialist_pic.get(position);
                    String pic_url = ServerUtil.getMkDir(hasSdCard)+"/"+mediaList_pic.getName();
//                String xh = mediaList_pic.getXh()+"";
                    Intent showPhotoIntent = new Intent(getActivity(), ShowPhotoActivity.class);
                    showPhotoIntent.putExtra("pic_url", pic_url);
                    startActivity(showPhotoIntent);
                    break;
                case R.id.hlv:
                    if (dailyChecked == position) {
                        return;
                    } else {
                        dailyChecked = position;
                        clearMediaList();
                        cfjl = cfjls.get(position);

                        if (!cfjl.getCfsj().equals(today)) {
                            hideSomeView();
                        } else {
                            showSomeView();
                        }
                        cfjlAdatpter.changeSelected(dailyChecked);
                        setContentValue(cfjl.getMemo());
                        if (cfjl.getXh() != null) {
                            LOADMODE = LOADLISTMX;
                            loadDataWithProgressDialog();
                        }
                        medialist_pic.clear();
                        medialist_audio.clear();
                        pictureAdapter.notifyDataSetChanged();
                        new_recordAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }else{
            switch (parent.getId()) {
            /*录音的listview列表*/
                case R.id.date_recordlistview:
                    MediaList mediaList = medialist_audio.get(position);
                    String record_url = mediaList.getUrl();
                    Intent recordPlayerIntent = new Intent(getActivity(), RecordPlayerActivity.class);
                    recordPlayerIntent.putExtra("record_url", record_url);
                    startActivity(recordPlayerIntent);
                    break;
                //图片列表
                case R.id.add_picturelistview:
                    MediaList mediaList_pic = medialist_pic.get(position);
                    String pic_url = mediaList_pic.getUrl();
//                String xh = mediaList_pic.getXh()+"";

                    Intent showPhotoIntent = new Intent(getActivity(), ShowPhotoActivity.class);
                    showPhotoIntent.putExtra("pic_url", pic_url);
                    startActivity(showPhotoIntent);

                    break;
                case R.id.hlv:
                    if (dailyChecked == position) {
                        return;
                    } else {
                        dailyChecked = position;
                        clearMediaList();
                        cfjl = cfjls.get(position);

                        if (!cfjl.getCfsj().equals(today)) {
                            hideSomeView();
                        } else {
                            showSomeView();
                        }
                        cfjlAdatpter.changeSelected(dailyChecked);
                        setContentValue(cfjl.getMemo());
                        if (cfjl.getXh() != null) {
                            LOADMODE = LOADLISTMX;
                            loadDataWithProgressDialog();
                        }
                        medialist_pic.clear();
                        medialist_audio.clear();
                        pictureAdapter.notifyDataSetChanged();
                        new_recordAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.hlv:
                if (!cfjls.get(position).getCfsj().startsWith(today)) {
                    hideSomeView();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*提交数据*/
    private void saveData() {


        //数据拼接
        DoctorInfo doctor = GlobalCache.getCache().getDoctor();
        String contents = "";
        try {
            contents = java.net.URLDecoder.decode(bwl_content.getText().toString().trim(),"UTF-8");
//            contents = new String(contents.getBytes("gbk"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        drinfo.setXh(today_CFXH == "" ? "0" : today_CFXH);
        drinfo.setYsdm(doctor.getId());
        drinfo.setSyxh("" + patient.getSyxh());
        drinfo.setYexh("" + patient.getYexh());
        drinfo.setMemo(contents);
        drinfo.setCfsj(today);
        //提交开启
        thread = new Thread(new SubmitThread());
        boolean alive = thread.isAlive();
        if (!alive) {
            thread.start();
        }
        proDialog = ProgressDialog.show(getActivity(), "",
                "正在传输数据...", true, true);
    }

    //录音
    private void takeAudio() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recordFileName = getRecordFileName(patient.getSyxh() + "");
        recordPath = ServerUtil.getMkDir(hasSdCard) + "/" + recordFileName;
        recorder.setOutputFile(recordPath);
        try {
            if (recorder != null) {
                recorder.prepare();
                recorder.start();   // Recording is now started

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent recordIntent = new Intent(getActivity(), RecordActivity.class);
        startActivityForResult(recordIntent, RECORD_REQUEST_CODE);
    }

    //照相
    private void takePhone() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String dir = ServerUtil.getMkDir(hasSdCard);
        photoFileName = getPhotoFileName(patient.getSyxh() + "");
        picturePath = dir + "/" + photoFileName;
        file = new File(picturePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Activity.DEFAULT_KEYS_SEARCH_LOCAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.DEFAULT_KEYS_SEARCH_LOCAL && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = null;
            if (hasSdCard) {
                bitmap = getBitmapFromFile(picturePath, 200, 100);
                bitmaps.add(bitmaps.size(), BitmapUtils.getBitmapThumnail(OurApplication.getContext(), bitmap, "add"));
                pictureAdapter.notifyDataSetChanged();
                MediaList detail = new MediaList();
                detail.setCfsj(today);
                detail.setXh(mediaIndex);
                detail.setName(photoFileName);
                detail.setUrl(picturePath);
                detail.setFlag(0);

                medialist.clear();
                medialist.add(detail);
                medialist_pic.add(detail);
                mediaIndex++;
                saveData();
            }
        }
        //保存录音
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RECORD_REQUEST_CODE) {
            saveRecord();
        }
       /* 当录音界面finsh或者点击关闭后，通知关闭recorder*/
        if (resultCode == 30) {
            recorder.stop();
            recorder.release();
        }
        //录音界面点击暂停的时候，让MediaRecord暂停
//        if(requestCode==RECORD_REQUEST_CODE&&resultCode==40){
//
//        }

    }

    // 获取录音文件名
    public String getRecordFileName(String syxh) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                syxh + "_'V'_yyyyMMddHHmmss");
        return dateFormat.format(date) + ".3gp";
    }

    // 获取文件名图片名
    public String getPhotoFileName(String syxh) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                syxh + "_'P'_yyyyMMddHHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public Bitmap getBitmapFromFile(String path, int width, int height) {
        if (null != path) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts);
                opts.inJustDecodeBounds = false;
                opts.inPurgeable = true;
                opts.inSampleSize = 10;
                opts.inInputShareable = true;
            }
            try {
                return BitmapFactory.decodeFile(path, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //保存录音的回调方法
    public void saveRecord() {
        if (hasSdCard) {
            MediaList media = new MediaList();
//            media.setCfsj(patient.getSyxh() + "");
            media.setCfsj(today);
            media.setFlag(1);
            media.setName(recordFileName);
            media.setUrl(recordPath);
            media.setXh(mediaIndex);
            medialist_audio.add(media);
            medialist.clear();
            medialist.add(media);
            recorder.stop();

            recorder.release();
            new_recordAdapter.notifyDataSetChanged();
            mediaIndex++;
            //上传录音数据
            saveData();

        }

    }


    public class GetImage implements Runnable {
        private String imageurl;
        private MediaList mediainfo;

        public GetImage(String imageurl, MediaList mediainfo) {
            // TODO Auto-generated constructor stub
            this.imageurl = imageurl;
            this.mediainfo = mediainfo;
        }

        @Override
        public void run() {
            URL url;
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inSampleSize = 10;
                opt.inInputShareable = true;

                url = new URL(imageurl);
                InputStream is = url.openStream();
//					mbitmap = BitmapFactory.decodeStream(is,null,opt);

                mbitmap = BitmapFactory.decodeStream(is, null, opt);

                bitmaps.add(BitmapUtils.getBitmapThumnail(OurApplication.getContext(), mbitmap, "add"));

                medialist_pic.add(mediainfo);
//					saveImage(bitmap, imgName);
                //			handler.sendEmptyMessage(GETPIC_OK);
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }
    }

    class SubmitThread implements Runnable {


        @Override
        public void run() {
            //本地保存
//			localPersist();
            /* 判断网络连接状态  */
            /* 离线状态查房数据保存在本地  */
            if (SystemUtil.isConnect(getActivity())) {
                //服务器新上传
                noteServerThread = new Thread(new NoteServerSubmit());
                noteServerThread.start();
            } else {
                //本地保存
                noteServerLocalTread = new Thread(new NoteServerlocalSubmit());
                noteServerLocalTread.start();
            }
        }
    }

    //本地保存
    class NoteServerlocalSubmit implements Runnable {

        @Override
        public void run() {
            try {
                int xh = NoteDao.add_cfjl(getActivity(), drinfo);
                LogUtils.showLog("xh_add_cfjl=" + xh);
                if (xh > 0) {
                    if (medialist.size() > 0) {
                        for (MediaList media : medialist) {
                            if (media.getFlag() == 0) {    //图片

                                NoteDao.add_cfjlMedia(getActivity(), media, xh);
                                LogUtils.showLog("add_picit_cfjlMedia=" + xh);
//                                cfjl.setXh(xh+"");
//                                backXH=xh;
                                cfjl.setXh(xh + "");
                            } else if (media.getFlag() == 1) {   //录音
                                NoteDao.add_cfjlMedia(getActivity(), media, xh);
                                LogUtils.showLog("add_cfjlMedia=" + xh);
//                                cfjl.setXh(xh+"");
                                cfjl.setXh(xh + "");
//                                backXH=xh;
                            }
                        }
                    }
                    drinfo.setXh("" + xh);
                       /* 当单个文件保存成功后，查询数据库，拿到返回的XH，保证文本、图片、录音保存在一天的表的记录里*/
                    cfjls = NoteDao.getNote_cfjl(getActivity(), "", doctor.getId(), patient.getSyxh() + "", patient.getYexh() + "");

                    for (int i = 0; i < cfjls.size(); i++) {
                        if (cfjls.get(i).getCfsj().startsWith(today)) {
                            today_CFXH = cfjls.get(i).getXh();
                            today_Text = cfjls.get(i).getMemo();
                        }
                    }

                    Message msg = new Message();
                    msg.arg1 = 2;
                    msg.arg2 = 1;
                    handler.sendMessage(msg);
                } else {
                    Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                    Message msg = new Message();
                    msg.arg1 = 2;
                    msg.arg2 = 2;
                    handler.sendMessage(msg);
                }


            } catch (Exception e) {
                LogUtils.showLog("操作数据库错误");
                e.printStackTrace();
            }
        }
    }
    /**
     * 更新服务端数据
     *
     * @author Administrator
     */
    class NoteServerSubmit implements Runnable {
        @Override
        public void run() {
            Map<String, File> fileMap = new HashMap<String, File>();
            String json = "";
            drinfo.setMediaList(medialist);
            json = new Gson().toJson(drinfo);
            //上传至服务器
//            System.out.println("json=======" + SystemUtil.getValue("SYS02") + "?drInfo=" + json);
            //public static  String CFJLUploadUrl = "http://192.168.155.1:8787/UploadServer.ashx";
//            ServerUtil.uploadParamstoServer(WebUtils.CFJLUploadUrl+ "&drInfo=" + json, "", "");
              LogUtils.showLog("1次");
//            for (int i = 0; i < medialist.size(); i++) {
            if(medialist.size()==0){
                ServerUtil.uploadtoServer(GlobalCache.getCache().getCFJLUploadUrl()+ "&drInfo=" + json,"","");
            }else{
                ServerUtil.uploadtoServer(GlobalCache.getCache().getCFJLUploadUrl()+ "&drInfo=" + json, medialist.get(0).getName(), medialist.get(0).getUrl());
            }
              LogUtils.showLog("几次");
//            }

            //当单个文件保存成功后，查询数据库，拿到返回的XH，保证文本、图片、录音保存在一天的表的记录里
            String syxh = "" + patient.getSyxh();
            String yexh = "" + patient.getYexh();
            String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"ysdm\":\"" + docid + "\"}";
            cfjls = NoteAction.getDoctorDailyRecordList(getActivity(), params);

            for (int i = 0; i < cfjls.size(); i++) {
                if (cfjls.get(i).getCfsj().startsWith(today)) {
                    today_CFXH = cfjls.get(i).getXh();
                    today_Text = cfjls.get(i).getMemo();
                 }
            }



            Message msg = new Message();
            msg.arg1 = 2;
            msg.arg2 = 1;
            proDialog.dismiss();
            handler.sendMessage(msg);
        }
    }

    //文字备忘赋值
    public void setContentValue(String text) {
        bwl_content.setMovementMethod(new ScrollingMovementMethod());
        bwl_content.setText(text);
//		bwl_content.setText("炎症（活检小标本）（宫颈）鳞状上皮及柱状上皮粘膜慢性炎；部分腺体及表面柱状上皮鳞化、增生；部分鳞状上皮增生明显呈现 CINⅠ、Ⅱ、Ⅲ级改变；部分腺体潴留、扩张；鳞状上皮基底细胞增生；棘层内可见一些挖空细胞，建议进一步行HPV检测。");
    }

    /* 清除媒介缓存  */
    private void clearMediaList() {
        if (mbitmap != null && !mbitmap.isRecycled()) {
            mbitmap.recycle();
        }
        bitmaps.clear();
        cfjlmxs.clear();
        record_cfjlmxs.clear();
        pictureAdapter.notifyDataSetChanged();
        new_recordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (proDialog != null && proDialog.isShowing()) {
            proDialog.dismiss();
        }
        if (thread != null) {
            thread.interrupt();
        }


        if (noteServerThread != null) {
            noteServerThread.interrupt();
        }
        if (noteServerLocalTread != null) {
            noteServerLocalTread.interrupt();
        }
    }


}
