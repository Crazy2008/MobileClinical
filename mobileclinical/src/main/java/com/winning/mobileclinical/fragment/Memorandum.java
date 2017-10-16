package com.winning.mobileclinical.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.BwlLocalAction;
import com.winning.mobileclinical.action.NewBwlAction;
import com.winning.mobileclinical.activity.NewBWLEditActivity;
import com.winning.mobileclinical.adapter.BWLListAdapter;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.layout.ScrollListView;
import com.winning.mobileclinical.layout.ScrollListView.OnRefreshListener;
import com.winning.mobileclinical.model.Bwl;
import com.winning.mobileclinical.model.MediaModel;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.MessageUtils;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.web.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 备忘录
 * @author liu
 *
 */
public class Memorandum extends FragmentChild {
	private static final int MODE_ADD = 0;
	private static final int MODE_UPDATE = 1;
	private static final int MODE_DELETE = 2;
	public final static int EDIT_RESULT = 200;
	private PatientInfo patient = null;			//病人信息
	private ScrollListView bwlListView = null;
//	private List<TouchNoteDTO> bwlList = null;
	private List<Bwl> bwlList = null;
	private int submitmode = MODE_ADD;
	private ImageButton bwl_btn_add = null;
	private String notestatus = "1";   //1私人 2公开
	private Context context = null;
	PopupWindow mPopup = null;
	ListView mList = null;
	private ProgressDialog dialog;
	private BWLListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SystemUtil.getConnect(this.getActivity());
		View view = inflater.inflate(R.layout.bwlfragment, container, false);
		context = this.getActivity();
		bwlListView = (ScrollListView) view.findViewById(R.id.bwl_listview);
//		bwl_mbadd = (TextView) view.findViewById(R.id.bwl_mbadd);
		bwl_btn_add = (ImageButton) view.findViewById(R.id.bwl_btn_add);
		dialog = ProgressDialog.show(getActivity(), "", "加载数据中，请稍后...",true, false);
		adapter = new BWLListAdapter(getActivity());
		bwlListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		/**
//		 * 下拉刷新
//		 */
		 bwlListView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadDataWithProgressDialog();
			}
		});
		 
		 bwlListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					
					Bwl bwl = bwlList.get(position-1);
					Intent intent = new Intent(getActivity(), NewBWLEditActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("bwl", bwl);
//					intent.putExtra("bwlxh", MathHelp.parseInt(bwl.getXh()));
//					intent.putExtra("savetype", bwl.getSave_type());
//					Memorandum.getInstance().setBwl(bwl);
					startActivityForResult(intent, EDIT_RESULT);
					
				}
			});
		
		registerForContextMenu(bwlListView); 
		 
//		if(GlobalCache.getCache().getPatient_selected() != null)
//		{
//			patient = GlobalCache.getCache().getPatient_selected();
//		}
		bwl_btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,NewBWLEditActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("bwl", new Bwl().setXh("0").setSave_type(1));
//				intent.putExtra("type", "1");
				startActivityForResult(intent, EDIT_RESULT);
			}
		});
		LoadThread load = new LoadThread();
		new Thread(load).start();
		
		return view;
	}
	
	//弹出菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Bwl bwl=bwlList.get(info.position-1);
		
		if(bwl.getSave_type()==1){
			menu.add(0, 1, 1, "上传");
		}
		menu.add(0, 2, 2, "删除");
		menu.add(0, 3, 3, "取消");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getMenuInfo() instanceof AdapterContextMenuInfo) { 
            AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
            int position=menuInfo.position-1;
            final Bwl bwl=bwlList.get(position);
            
            switch (item.getItemId()) { 
            	//上传
            	case 1:upload(bwl);break;
            	//删除
            	case 2:
            		delete(bwl);
            	break;
            	//取消
            	case 3:break;
            		
            }
        }
		return super.onContextItemSelected(item);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == EDIT_RESULT) {
			dialog = ProgressDialog.show(getActivity(), "", "加载数据中，请稍后...",true, false);
			LoadThread load = new LoadThread();
			new Thread(load).start();
		}else{
				getActivity().finish();
			}
		}
	
	class LoadThread implements Runnable{
		@Override
		public void run() {
			bwlList=new ArrayList<Bwl>();
			//获取服务器数据
			//List<Bwl> bwlServerList=BwlAction.getMyBwl(BWLNew.this);
//			List<Bwl> bwlServerList=NewBwlAction.getBqBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+""); //bwl001
//			//获取本地数据
//			List<Bwl> bwlLocalList=BwlLocalAction.getMyBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+"");
//			bwlList.addAll(bwlServerList);
//			bwlList.addAll(bwlLocalList);
//			
//			if(bwlList.size()>0){
//				Collections.sort(bwlList);
//			}
			
			if(bwlList != null && bwlList.size() >= 0) {
				Message msg = new Message();
				msg.arg1 = 1;
				handler.sendMessage(msg);
			}
		}
		
	}
	
	
//	class LoadThreadDelete implements Runnable{
//		@Override
//		public void run() {
//			int result=0;
//			result = NewBwlAction.deleteBwl(getActivity(),bwl.getXh());
//			
//			
//			if(bwlList != null && bwlList.size() >= 0) {
//				Message msg = new Message();
//				msg.arg1 = 1;
//				handler.sendMessage(msg);
//			}
//		}
//		
//	}
	
	
	/*
	 * 备忘录列表值
	 */
	private void setListViewValue() {
		adapter.setList(bwlList);
		bwlListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int flag = msg.arg1;
			if(flag == 1) {
				setListViewValue();
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
			else if(flag == 2)
			{
				if(dialog.isShowing()){
					dialog.setMessage("加载数据中，请稍后...");
				}
			}
		}
	};
	
	
	//删除
	public void delete(Bwl bwl){
		int type=bwl.getSave_type();
		
		int result=0;
		//删除本地记录
		if(type==1){
			result=BwlLocalAction.deleteBwl(getActivity(), bwl.getXh());
			
			if(result>0){
				//获取备忘录对应资源
				List<MediaModel> medaModel=BwlLocalAction.getMediaId(getActivity(), bwl.getXh(),patient.getSyxh()+"",patient.getYexh()+"");
				for(MediaModel media:medaModel){
					deleteLocalFile(media);
				}
			}
			//重新载入数据
			dialog = ProgressDialog.show(getActivity(), "", "加载数据中，请稍后...",true, false);
			
			new Thread(new LoadThread()).start();
			
		}
		//删除服务器数据，发送HTTP请求道后台
		if(type==2){
			/* BWL001 start */
			DoctorInfo nurse=GlobalCache.getCache().getDoctor();
			if(nurse != null && !bwl.getCjhsid().trim().equals(nurse.getId().trim()))
			{
				Toast.makeText(getActivity(), "删除失败,非创建者本人操作", Toast.LENGTH_SHORT).show();
				return;
			}
			
			deleteFwq(bwl);
//			result = NewBwlAction.deleteBwl(getActivity(),bwl.getXh());
		}
		
		
//		LoadThread load = new LoadThread();
//		new Thread(load).start();
	}
	
	
	
	//删除
	
	public void deleteFwq(final Bwl bwl) {
		
	dialog = ProgressDialog.show(getActivity(), "", "删除数据中，请稍后...",true, false);
	
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			int result=0;
			// TODO Auto-generated method stub
			result = NewBwlAction.deleteBwl(getActivity(),bwl.getXh());
			//重新载入数据
			
			if(result>0){
				//获取备忘录对应资源
				List<MediaModel> medaModel=BwlLocalAction.getMediaId(getActivity(), bwl.getXh(),patient.getSyxh()+"",patient.getYexh()+"");
				for(MediaModel media:medaModel){
					deleteLocalFile(media);
				}
			}
			
			Message msg = new Message();
			msg.arg1 = 2;
			handler.sendMessage(msg);
			LoadThread load = new LoadThread();
			new Thread(load).start();
		}
	}).start();}
	
	
	//上传
	public void upload(final Bwl bwl){
		dialog = ProgressDialog.show(getActivity(), "", "上传数据中，请稍后...",true, false);
		//本地存储的才上传到服务器
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(bwl.getSave_type() == 1)
				{
					uploadServer(bwl);
				}
				//重新载入数据
				Message msg = new Message();
				msg.arg1 = 2;
				handler.sendMessage(msg);
				LoadThread load = new LoadThread();
				new Thread(load).start();
			}
		}).start();
	}
	
	
	//服务器上传：1、资源上传 2、数据上传
	public void uploadServer(Bwl bwl){
		//获取备忘录对应资源
		List<MediaModel> medaModel=BwlLocalAction.getMediaId(getActivity(), bwl.getXh(),patient.getSyxh()+"",patient.getYexh()+"");
		//存在媒体资源，先上传
		if(medaModel.size()>0){
			Map<String, File> fileMap = new HashMap<String, File>();
			
			for(int i=0;i<medaModel.size();i++){
				MediaModel media=medaModel.get(i);
				String path=media.getSrc()+media.getFileName();
				//图像
				if("1".equals(media.getLb())){
					fileMap.put("photo" + i, new File(path));
				}
				//录音
				if("2".equals(media.getLb())){
					fileMap.put("record", new File(path));
				}
				//涂鸦
				if("4".equals(media.getLb())){
					fileMap.put("tuya", new File(path));
				}
			}
			
			HTTPGetTool upload = HTTPGetTool.getTool();
			JSONObject json = upload.post(WebUtils.HOST	+ WebUtils.UPLOAD, fileMap);
			
			try {
				//上传成功，同步数据库
				if(json!=null&&json.get("success").equals("true")){
					uploadData(bwl,medaModel);
				}else{
//					DialogHelper.showAlert(this, "", "上传失败");
					Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_LONG)
					.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			uploadData(bwl,medaModel);
		}
		
	}
	
	//本地数据上传
	public void uploadData(Bwl bwl,List<MediaModel> medaModel){
		int bwlid = NewBwlAction.addBwl(getActivity(),bwl,patient.getSyxh()+"",patient.getYexh()+"");
		//添加成功
		if(bwlid>0&&medaModel.size()>0){
			for(MediaModel media:medaModel){
				String fileName=media.getFileName();
				
				int result=0;
				//图像
				if("1".equals(media.getLb())){
					
					result=NewBwlAction.upLoad(getActivity(),"1", "拍照", "D:/doctouch/photo/",
								     bwlid + "",media.getDescription(),fileName,patient.getSyxh()+"",patient.getYexh()+"");
					
				}
				//录音
				if("2".equals(media.getLb())){
					result=NewBwlAction.upLoad(getActivity(),"2", "录音", "D:/doctouch/record/",
						     bwlid + "",media.getDescription(),fileName,patient.getSyxh()+"",patient.getYexh()+"");
					
				}
				//涂鸦
				if("4".equals(media.getLb())){
					result=NewBwlAction.upLoad(getActivity(),"4", "涂鸦", "D:/doctouch/tuya/",
						     bwlid + "",media.getDescription(),fileName,patient.getSyxh()+"",patient.getYexh()+"");
				}
				
				if(result>0){
					deleteLocalFile(media);
				}
			}
			
		}
		
		//删除本地数据
		BwlLocalAction.deleteBwl(getActivity(), bwl.getXh());
	}
	
	//删除本地媒体资源
	public void deleteLocalFile(MediaModel media){
		String path=media.getSrc()+media.getFileName();
		new File(path).delete();
	}

	
	public void reFresh() {
		bwlList=new ArrayList<Bwl>();
		List<Bwl> bwlServerList=NewBwlAction.getBqBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+""); //bwl001
		//获取本地数据
		List<Bwl> bwlLocalList=BwlLocalAction.getMyBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+"");
//		System.out.println("备忘录返回"+new Gson().toJson(bwlServerList));
//		System.out.println("备忘录返回本地"+new Gson().toJson(bwlLocalList));
		
		if(bwlServerList.size()>0) {
			bwlList.addAll(bwlServerList);
		}
		if(bwlLocalList.size()>0) {
			
			bwlList.addAll(bwlLocalList);
		}
	}
	
	
	
	protected void submitDate() {
		if(submitmode == MODE_ADD) {
			bwlList=new ArrayList<Bwl>();
			List<Bwl> bwlServerList=NewBwlAction.getBqBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+""); //bwl001
			//获取本地数据
			List<Bwl> bwlLocalList=BwlLocalAction.getMyBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+"");
			
//			System.out.println("备忘录返回"+new Gson().toJson(bwlServerList));
//			System.out.println("备忘录返回本地"+new Gson().toJson(bwlLocalList));
			
			if(bwlServerList.size()>0) {
				bwlList.addAll(bwlServerList);
			}
			if(bwlLocalList.size()>0) {
				
				bwlList.addAll(bwlLocalList);
			}
			
		} 
//		else if(submitmode == MODE_DELETE) {
//			
//			BwlAction.deleteNote(getActivity(), String.valueOf(theMemo.getId()));
//			bwlList = BwlAction.getNotesbyys(getActivity());
//		} else if(submitmode == MODE_UPDATE) {
//			
//			BwlAction.updateNote(getActivity(), String.valueOf(theMemo.getId()), bwlpop_content.getText().toString(), patient.getSyxh().toString(), patient.getHzxm(), notestatus);
//			
//			bwlList = BwlAction.getNotesbyys(getActivity());
//		}
	}
	
	
	protected void afterSubmit() {
		
		if(submitmode == MODE_ADD) {
//			MessageUtils.showToast(getActivity(),"刷新成功！");
			
		} else if(submitmode == MODE_DELETE) {
			MessageUtils.showToast(getActivity(),"删除成功！");
		} else if(submitmode == MODE_UPDATE) {
			MessageUtils.showToast(getActivity(),"修改成功！");
		}
		
		if(bwlList != null) {
			
//			adapter = new BwlAdapter(getActivity(), bwlList);
			adapter = new BWLListAdapter(getActivity());
			adapter.setList(bwlList);
			bwlListView.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
			
			bwlListView.onRefreshComplete();
		}
		
	}
	
	@Override
	protected void loadDate() {
		
		bwlList=new ArrayList<Bwl>();
		
	//	bwlList = BwlAction.getNotesbyhz(getActivity(), ""+patient.getSyxh());
//		bwlList = BwlAction.getNotesbyys(getActivity());
		
		List<Bwl> bwlServerList=NewBwlAction.getBqBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+""); //bwl001
		//获取本地数据
		List<Bwl> bwlLocalList=BwlLocalAction.getMyBwl(getActivity(),patient.getSyxh()+"",patient.getYexh()+"");
		
		
//		System.out.println("备忘录返回"+new Gson().toJson(bwlServerList));
//		System.out.println("备忘录返回本地"+new Gson().toJson(bwlLocalList));
		
		if(bwlServerList.size()>0) {
			bwlList.addAll(bwlServerList);
		}
		if(bwlLocalList.size()>0) {
			
			bwlList.addAll(bwlLocalList);
		}
		
		
		
	}
	@Override
	protected void afterLoadData() {
		// TODO Auto-generated method stub
		if(bwlList != null)
		{
			adapter = new BWLListAdapter(getActivity());
			adapter.setList(bwlList);
			bwlListView.setAdapter(adapter);
			
			bwlListView.onRefreshComplete();
			
//			bwlListView.setOnItemClickListener(clickListener);
		}
	}
	
	
	
	@Override
	public void switchPatient() {
		// TODO Auto-generated method stub
		
	}
}
