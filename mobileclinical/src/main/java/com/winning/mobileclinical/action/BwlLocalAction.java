package com.winning.mobileclinical.action;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.db.dao.BwlDao;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.Bwl;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.MCS_CFJL;
import com.winning.mobileclinical.model.MediaModel;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.HttpResult;
import com.winning.mobileclinical.web.WebUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BwlLocalAction {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HHmmss");
	public static BwlDao bwlDao=BwlDao.getInstance();
	/**
	 * 更新备忘录
	 * @param context
	 * @param bwl
	 */
	public static void updateBwl(Context context, Bwl bwl,String syxh,String yexh) {
		// TODO Auto-generated method stub
		Date date = new Date();
		String time = format1.format(date);
		bwl.setGxsj(time);
		bwlDao.updateBwl(context, bwl);
	}
	
	/*
	 * 添加查房记录
	 */
	public static int addCFJL(Context context, MCS_CFJL cfjl){
		Date date = new Date();
		String time = format.format(date);
		int result=0;
		result=bwlDao.insertCFJL(context,cfjl);
		
		return result;
	}
	
	public static int addCfjl(Context context,DrInfo drinfo, String syxh, String yexh,DoctorInfo doctorInfo){
		Date date = new Date();
		
		 Bwl bwl = new Bwl();
		String time = format.format(date);

		drinfo.setCfsj(time);
		
		bwl.setCjhsid(doctorInfo.getId());
		bwl.setCjhsname(doctorInfo.getName());
		bwl.setJlzt("1");
		bwl.setBq_id(doctorInfo.getBqdm());
		bwl.setBq_mc("");
		bwl.setSave_type(1);
		bwl.setSyxh(syxh);
		bwl.setYexh(yexh);
		
		int result=0;
		result=bwlDao.addBwl(context,bwl);
		
		return result;
	}
	
	
	
	
	/*
	 * 添加数据库信息
	 */
	public static int addBwl(Context context, Bwl bwl, String syxh, String yexh){
		Date date = new Date();
		String time = format.format(date);

		bwl.setCjsj(time);
		DoctorInfo accountDTO=GlobalCache.getCache().getDoctor();
		
		bwl.setCjhsid(accountDTO.getId());
		bwl.setCjhsname(accountDTO.getName());
		bwl.setJlzt("1");
		bwl.setBq_id(accountDTO.getBqdm());
		bwl.setBq_mc("");
		bwl.setSave_type(1);
		bwl.setSyxh(syxh);
		bwl.setYexh(yexh);
		
		int result=0;
		result=bwlDao.addBwl(context,bwl);
		
		return result;
	}
	/*
	 * 添加数据库信息
	 */
	public static  int upLoad(Context context,String LB,String LBSM,String path,String bwlid,String description,String fileName,String syxh,String yexh){
		int result=bwlDao.upload(context,LB,LBSM,path, bwlid,description,fileName,syxh,yexh); 
		return result;
	}
	
	
	public static  int upLoadCfjl(Context context,String LB,String LBSM,String path,String bwlid,String description,String fileName,String syxh,String yexh){
		int result=bwlDao.upload(context,LB,LBSM,path, bwlid,description,fileName,syxh,yexh); 
		return result;
	}
	
	
	//备忘录删除:先删除备忘录，然后删除对应媒体库
	public static int deleteMedia(Context context,String id){
		int result=bwlDao.deleteMediaById(context, id);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
//	/*
//	 * 添加数据库信息
//	 */
//	public static int addBwl(Context context,String note,String title){
//		Date date = new Date();
//		String time = format.format(date);
//		
//		Bwl bwl=new Bwl();
//		bwl.setTitle(title);
//		bwl.setContents(note);
//		bwl.setCjsj(time);
//		Nurse nurse=GlobalCache.getCache().getNurse();
//		
//		bwl.setCjhsid(nurse.getId());
//		bwl.setCjhsname(nurse.getName());
//		bwl.setJlzt("1");
//		bwl.setBq_id(nurse.getBqid());
//		bwl.setBq_mc(nurse.getBqmc());
//		
//		int result=0;
//		result=bwlDao.addBwl(context,bwl);
//		
//		return result;
//	}
//	
//	

//	
//	public static  int upLoadUpdate(Context context,String path,String id) {
//		
//		int result=bwlDao.upLoadUpdate(context,path,id); 
//		
//		return result;
//	}
//	
//	
//	/*
//	 * 更新照片及录音的描述
//	 */
//	public static int upLoadUpdateDescription(Context context,String text,String id) {
//		int result=bwlDao.updateDescription(context,text,id); 
//		
//		return result;
//		
//	}
//	/*
//	 * 
//	 */
	public static List<MediaModel> getMediaId(Context context,String xh, String syxh, String yexh){
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		
		List<MediaModel> list=new ArrayList<MediaModel>();
		try {
			list = bwlDao.getMedia(db,xh,syxh,yexh);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
		return list;
	}
	
	
	public static ArrayList<MediaModel> getMediaId2(Context context,String xh){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("xh",xh));
		params.add(new BasicNameValuePair("method","getMedia"));
		
//		String res = HTTPGetTool.getTool().postJsonString(context,
//				WebUtilsHOST +WebUtils.BWL,params);
//		
//		System.out.println(res);
////		
		HttpResult res = HTTPGetTool.getTool().post2server(context,
				WebUtils.HOST +WebUtils.BWL, params);
		ArrayList<MediaModel> list=new ArrayList<MediaModel>();
		try {
			if (res.getCode() == 1){
				JSONArray medias=res.getJson().getJSONArray("medias");
				for(int i=0;i<medias.length();i++){
					JSONObject media=medias.getJSONObject(i);
					list.add(new MediaModel(media));
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
//	/*
//	 * 下载内容
//	 */
///*	public static InputStream downLoad(String path){
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("path",path));
//		params.add(new BasicNameValuePair("method","download"));
//		
//		
//		return HTTPGetTool.getTool().downLoad(WebUtilsHOST +WebUtils.BWL, params);
//	}*/
//	
//	
	/*
	 * 获取bwl数据库内容
	 */
	public static  List<Bwl> getMyBwl(Context context,String syxh, String yexh){
		
		DoctorInfo accountDTO=GlobalCache.getCache().getDoctor();
		List<Bwl>list=bwlDao.getMyBwl(context,accountDTO.getId(),syxh,yexh);
		return list;
	}
//
//	
//	//获取备忘录
//	public List<Bwl> getBqBwl(Context context){
//		
//		Nurse nurse=GlobalCache.getCache().getNurse();
//		List<Bwl>list=bwlDao.getBqBwl(context,nurse.getBqid(), nurse.getId());
//		
//		return list;
//	}
//
//	//lp yong
//	public int  updateBwl(Context context,String xh,String txsj,String contents,String kjbz){
//		
//		/*Date txdate;
//		try {
//			txdate=format.parse(txsj);
//			txsj=format.format(txdate);
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//		}*/
//		/*List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("xh",xh));
//		params.add(new BasicNameValuePair("txbz",txbz));
//		params.add(new BasicNameValuePair("txsj",""));
//		params.add(new BasicNameValuePair("cfbz",cfbz ));
//		params.add(new BasicNameValuePair("contents",contents ));
//		params.add(new BasicNameValuePair("kjbz",kjbz));
//		params.add(new BasicNameValuePair("method", "updatebwl"));
//		
//		JSONObject res = HTTPGetTool.getTool().post(
//				WebUtilsHOST +WebUtils.BWL, params);
//		int result;
//		try {
//			if (HTTPGetTool.checkResult(res) == 0){
//				result=Integer.parseInt(res.getString("result"));
//				return result;
//			}
//		} catch (JSONException  e) {
//			e.printStackTrace();
//			return 0;
//		}*/
//		
//	
//		String title="";
//		if(contents.length()>5)
//			title=contents.substring(0, 5);
//		else{
//			title=contents;
//		} 
//			
//		
//		Bwl bwl=new Bwl();
//		bwl.setTitle(title);
//		bwl.setXh(xh);
//		bwl.setType(kjbz);
//		bwl.setContents(contents);
//		bwl.setGxsj(txsj);
//		
//		int result=bwlDao.updateBwl(context,bwl);
//		return result;
//	}
//
//	//gutao yong
//	public static  int updateBwl(Context context,String note,String title,String xh){
//		Date date = new Date();
//		String time = format.format(date);
//	/*	List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("content",note));
//		params.add(new BasicNameValuePair("title",title));
//		params.add(new BasicNameValuePair("time",time));
//		params.add(new BasicNameValuePair("id",xh));
//		params.add(new BasicNameValuePair("method", "updatebwlTitleOrContents"));
//		
//		JSONObject res = HTTPGetTool.getTool().post(
//				WebUtilsHOST +WebUtils.BWL, params);
//		int result;
//		try {
//			if (HTTPGetTool.checkResult(res) == 0){
//				result=Integer.parseInt(res.getString("result"));
//				return result;
//			}
//		} catch (JSONException  e) {
//			e.printStackTrace();
//			return 0;
//		}*/
//		int result=bwlDao.updateBwl(context, xh, title, note, time);
//		return result;
//	}
//	
//	/*
//	 * 删除bwl内容
//	 */
//	/*public  int deleteBwl(Context context,String xh){
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("xh",xh));
//		params.add(new BasicNameValuePair("method", "deletebwl"));
//		
//		JSONObject res = HTTPGetTool.getTool().post(
//				WebUtilsHOST +WebUtils.BWL, params);
//		int result;
//		try {
//			if (HTTPGetTool.checkResult(res) == 0){
//				result=Integer.parseInt(res.getString("result"));
//				return result;
//			}
//		} catch (JSONException  e) {
//			e.printStackTrace();
//			return 0;
//		}
//		
//		int result=bwlDao.deleteBwlById(context, xh);
//		return result;
//	}*/
//
//	public int updateBwlTitle(Context context,String id,String title){
//	/*	List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id",id));
//		params.add(new BasicNameValuePair("title",title));
//		params.add(new BasicNameValuePair("method", "updatebwltitle"));
//		
//		JSONObject res = HTTPGetTool.getTool().post(
//				WebUtilsHOST +WebUtils.BWL, params);
//		int result;
//		try {
//			if (HTTPGetTool.checkResult(res) == 0){
//				result=Integer.parseInt(res.getString("result"));
//				return result;
//			}
//		} catch (JSONException  e) {
//			e.printStackTrace();
//			return 0;
//		}*/
//		int result=bwlDao.updateBwlTitle(context, id, title);
//		return result;
//	}
//	
	//备忘录删除:修改状态
	public static int deleteBwl(Context context,String xh){
		int result=bwlDao.deleteBwlById(context, xh);
		return result ;
	}


}
